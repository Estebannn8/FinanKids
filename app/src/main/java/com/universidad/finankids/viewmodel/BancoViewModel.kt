package com.universidad.finankids.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.universidad.finankids.data.model.BancoData
import com.universidad.finankids.data.model.Transaccion
import com.universidad.finankids.events.BancoEvent
import com.universidad.finankids.state.BancoState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class BancoViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow(BancoState())
    val state: StateFlow<BancoState> = _state

    private val uid: String?
        get() = auth.currentUser?.uid

    // -----------------------------------------------------------
    // 1. Control de eventos principales
    // -----------------------------------------------------------
    fun onEvent(event: BancoEvent) {
        when (event) {
            is BancoEvent.CargarBanco -> cargarBanco()
            is BancoEvent.CrearBancoInicial -> crearBancoInicial()
            is BancoEvent.RegistrarPin -> registrarPin(event.pin)
            is BancoEvent.ValidarPin -> validarPin(event.pin)
            is BancoEvent.Depositar -> depositar(event.monto)
            is BancoEvent.Retirar -> retirar(event.monto)
            is BancoEvent.CalcularIntereses -> calcularIntereses()
            is BancoEvent.ResetValidacion -> resetValidacion()
            is BancoEvent.Error -> mostrarError(event.mensaje)
            is BancoEvent.LimpiarMensajeOperacion -> {
                _state.value = _state.value.copy(
                    mensajeOperacion = null,
                    errorOperacion = null
                )
            }
        }
    }

    // -----------------------------------------------------------
    // 2. Cargar datos del banco
    // -----------------------------------------------------------
    private fun cargarBanco() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, mensaje = null, errorMessage = null)
            try {
                val userId = uid ?: return@launch
                val docRef = firestore.collection("usuarios")
                    .document(userId)
                    .collection("banco")
                    .document("info")

                val snapshot = docRef.get().await()
                if (snapshot.exists()) {
                    val banco = snapshot.toObject(BancoData::class.java)
                    _state.value = _state.value.copy(
                        banco = banco,
                        loading = false,
                        pinValidado = false
                    )
                } else {
                    crearBancoInicial()
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(loading = false, errorMessage = e.message)
            }
        }
    }

    // -----------------------------------------------------------
    // 3. Crear banco inicial (sin PIN)
    // -----------------------------------------------------------
    private fun crearBancoInicial() {
        viewModelScope.launch {
            val userId = uid ?: return@launch
            val bancoInicial = BancoData()
            try {
                firestore.collection("usuarios")
                    .document(userId)
                    .collection("banco")
                    .document("info")
                    .set(bancoInicial)
                    .await()

                _state.value = _state.value.copy(
                    banco = bancoInicial,
                    mensaje = "Banco creado. Ahora registra un PIN."
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(errorMessage = e.message)
            }
        }
    }

    // -----------------------------------------------------------
    // 4. Registrar PIN por primera vez
    // -----------------------------------------------------------
    private fun registrarPin(pin: String) {
        if (pin.length != 4) {
            _state.value = _state.value.copy(errorMessage = "El PIN debe tener 4 dígitos.")
            return
        }

        viewModelScope.launch {
            try {
                val userId = uid ?: return@launch
                val docRef = firestore.collection("usuarios")
                    .document(userId)
                    .collection("banco")
                    .document("info")

                docRef.update("pin", pin).await()
                _state.value = _state.value.copy(
                    mensaje = "PIN registrado correctamente.",
                    pinValidado = true
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(errorMessage = e.message)
            }
        }
    }

    // -----------------------------------------------------------
    // 5. Validar PIN al ingresar
    // -----------------------------------------------------------
    private fun validarPin(pin: String) {
        val banco = _state.value.banco
        if (banco == null) {
            _state.value = _state.value.copy(errorMessage = "Banco no cargado.")
            return
        }

        if (banco.pin == pin) {
            _state.value = _state.value.copy(
                pinValidado = true,
                mensaje = "PIN correcto. Bienvenido a tu banco."
            )
            calcularIntereses()
        } else {
            _state.value = _state.value.copy(errorMessage = "PIN incorrecto.")
        }
    }

    // -----------------------------------------------------------
    // 6. Reset de validación
    // -----------------------------------------------------------
    private fun resetValidacion() {
        _state.value = _state.value.copy(pinValidado = false, mensaje = null, errorMessage = null)
    }

    // -----------------------------------------------------------
    // 7. Mostrar error manual
    // -----------------------------------------------------------
    private fun mostrarError(msg: String) {
        _state.value = _state.value.copy(errorMessage = msg)
    }

    // -----------------------------------------------------------
    // 8. Depositar dinero
    // -----------------------------------------------------------
    private fun depositar(monto: Int) {
        if (monto <= 0) {
            _state.value = _state.value.copy(errorOperacion = "Monto inválido.")
            return
        }

        viewModelScope.launch {
            try {
                val userId = uid ?: return@launch
                val bancoActual = _state.value.banco ?: return@launch

                // Validar que el usuario tenga suficiente dinero
                val userDoc = firestore.collection("usuarios").document(userId).get().await()
                val dineroUsuario = userDoc.getLong("dinero")?.toInt() ?: 0

                if (monto > dineroUsuario) {
                    _state.value = _state.value.copy(errorOperacion = "No tienes suficiente dinero")
                    return@launch
                }

                // Transacción para actualizar banco Y usuario
                firestore.runTransaction { transaction ->
                    // Actualizar banco
                    val nuevoSaldo = bancoActual.saldo + monto
                    val nuevaTransaccion = Transaccion("Depósito", monto)
                    val nuevoHistorial = bancoActual.historial + nuevaTransaccion

                    val bancoActualizado = bancoActual.copy(
                        saldo = nuevoSaldo,
                        historial = nuevoHistorial,
                        ultimaActualizacion = System.currentTimeMillis()
                    )

                    // Actualizar usuario (restar dinero)
                    val userDoc = transaction.get(firestore.collection("usuarios").document(userId))
                    val currentDinero = userDoc.getLong("dinero")?.toInt() ?: 0

                    if (currentDinero < monto) {
                        throw Exception("Saldo insuficiente")
                    }

                    // Guardar cambios
                    transaction.set(
                        firestore.collection("usuarios").document(userId).collection("banco")
                            .document("info"),
                        bancoActualizado
                    )
                    transaction.update(
                        firestore.collection("usuarios").document(userId),
                        "dinero", currentDinero - monto
                    )
                }.await()

                _state.value = _state.value.copy(
                    banco = bancoActual.copy(
                        saldo = bancoActual.saldo + monto,
                        historial = bancoActual.historial + Transaccion("Depósito", monto),
                        ultimaActualizacion = System.currentTimeMillis()
                    ),
                    mensajeOperacion = "Depósito exitoso de $monto pesitos."
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(errorOperacion = e.message ?: "Error al depositar")
            }
        }
    }

    // -----------------------------------------------------------
    // 9. Retirar dinero
    // -----------------------------------------------------------
    private fun retirar(monto: Int) {
        if (monto <= 0) {
            _state.value = _state.value.copy(errorOperacion = "Monto inválido.")
            return
        }

        viewModelScope.launch {
            try {
                val userId = uid ?: return@launch
                val bancoActual = _state.value.banco ?: return@launch

                if (monto > bancoActual.saldo) {
                    _state.value =
                        _state.value.copy(errorOperacion = "Saldo insuficiente en el banco")
                    return@launch
                }

                // Transacción para actualizar banco Y usuario
                firestore.runTransaction { transaction ->
                    // Actualizar banco
                    val nuevoSaldo = bancoActual.saldo - monto
                    val nuevaTransaccion = Transaccion("Retiro", monto)
                    val nuevoHistorial = bancoActual.historial + nuevaTransaccion

                    val bancoActualizado = bancoActual.copy(
                        saldo = nuevoSaldo,
                        historial = nuevoHistorial,
                        ultimaActualizacion = System.currentTimeMillis()
                    )

                    // Actualizar usuario (sumar dinero)
                    val userDoc = transaction.get(firestore.collection("usuarios").document(userId))
                    val currentDinero = userDoc.getLong("dinero")?.toInt() ?: 0

                    // Guardar cambios
                    transaction.set(
                        firestore.collection("usuarios").document(userId).collection("banco")
                            .document("info"),
                        bancoActualizado
                    )
                    transaction.update(
                        firestore.collection("usuarios").document(userId),
                        "dinero", currentDinero + monto
                    )
                }.await()

                _state.value = _state.value.copy(
                    banco = bancoActual.copy(
                        saldo = bancoActual.saldo - monto,
                        historial = bancoActual.historial + Transaccion("Retiro", monto),
                        ultimaActualizacion = System.currentTimeMillis()
                    ),
                    mensajeOperacion = "Retiro exitoso de $monto pesitos."
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(errorOperacion = e.message ?: "Error al retirar")
            }
        }
    }

    // -----------------------------------------------------------
    // 10. Calcular intereses automáticos (al acceder)
    // -----------------------------------------------------------
    private fun calcularIntereses() {
        viewModelScope.launch {
            val banco = _state.value.banco ?: return@launch
            val dias =
                ((System.currentTimeMillis() - banco.ultimaActualizacion) / (1000 * 60 * 60 * 24)).toInt()
            if (dias <= 0) return@launch

            val interesMensual = banco.interes
            val interesesGanados = (banco.saldo * interesMensual / 30 * dias).toInt()
            val nuevoSaldo = banco.saldo + interesesGanados

            val bancoActualizado = banco.copy(
                saldo = nuevoSaldo,
                ultimaActualizacion = System.currentTimeMillis()
            )

            try {
                val userId = uid ?: return@launch
                firestore.collection("usuarios")
                    .document(userId)
                    .collection("banco")
                    .document("info")
                    .set(bancoActualizado)
                    .await()

                _state.value = _state.value.copy(
                    banco = bancoActualizado,
                    mensaje = "Tu ahorro creció en +$interesesGanados pesitos por intereses."
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(errorMessage = e.message)
            }
        }
    }
}
