package com.universidad.finankids.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.universidad.finankids.data.model.Avatar
import com.universidad.finankids.data.model.UserData
import com.universidad.finankids.events.UserEvent
import com.universidad.finankids.state.UserState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    // Estado actual usando UserState
    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    // Canal para eventos
    private val _events = Channel<UserEvent>(Channel.UNLIMITED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            events.collect { event ->
                handleEvent(event)
            }
        }
    }

    fun sendEvent(event: UserEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }

    private fun handleEvent(event: UserEvent) {
        when (event) {
            is UserEvent.LoadUser -> loadUserData(event.uid)
            is UserEvent.ChangeAvatar -> changeAvatar(event.avatarId)
            is UserEvent.ChangeMarco -> changeMarco(event.marcoId)
            is UserEvent.ChangeNickname -> changeNickname(event.newName)
            UserEvent.Logout -> logout()
            is UserEvent.LoadingSuccess -> updateUserData(event.userData)
            is UserEvent.LoadingFailed -> setError(event.error)
            UserEvent.LoadingStarted -> setLoading(true)
            UserEvent.ClearError -> clearError()
            is UserEvent.BuyAvatar -> buyAvatar(event.avatar)

        }
    }

    fun loadUserData(uid: String) {
        if (uid.isEmpty()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            Log.d("UserFlow", "Intentando cargar datos para UID: $uid")
            try {
                val snapshot = firestore.collection("usuarios").document(uid).get().await()
                Log.d("UserFlow", "Datos obtenidos: ${snapshot.data}")
                val userData = snapshot.toObject(UserData::class.java)
                    ?: throw Exception("Documento de usuario no encontrado")

                _state.update {
                    it.copy(
                        userData = userData,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("UserFlow", "Error al cargar usuario", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al cargar datos: ${e.message}"
                    )
                }
            }
        }
    }

    private fun changeNickname(newName: String) {
        viewModelScope.launch {
            try {
                // 🔹 Validación de longitud
                if (newName.length > 13) {
                    _state.update { it.copy(errorMessage = "El nombre no puede superar los 13 caracteres") }
                    return@launch
                }

                val uid = _state.value.userData.uid
                if (uid.isEmpty()) return@launch

                // 🔹 Validar que no esté en uso
                val query = firestore.collection("usuarios")
                    .whereEqualTo("nickname", newName)
                    .get()
                    .await()

                val alreadyTaken = query.documents.any { it.id != uid }
                if (alreadyTaken) {
                    _state.update { it.copy(errorMessage = "Ese nombre ya está en uso") }
                    return@launch
                }

                // 🔹 Si todo bien, guardar
                val userRef = firestore.collection("usuarios").document(uid)
                userRef.update("nickname", newName).await()

                // Actualizar estado local
                _state.update { state ->
                    state.copy(
                        userData = state.userData.copy(nickname = newName),
                        errorMessage = null
                    )
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(errorMessage = "Error al cambiar nombre: ${e.message}")
                }
            }
        }
    }

    private fun changeAvatar(avatarId: String) {
        viewModelScope.launch {
            try {
                val uid = _state.value.userData.uid
                val userRef = firestore.collection("usuarios").document(uid)

                userRef.update("avatarActual", avatarId).await()

                _state.update { state ->
                    state.copy(
                        userData = state.userData.copy(avatarActual = avatarId)
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = "Error al cambiar avatar: ${e.message}") }
            }
        }
    }

    private fun changeMarco(marcoId: String) {
        viewModelScope.launch {
            try {
                val uid = _state.value.userData.uid
                val userRef = firestore.collection("usuarios").document(uid)

                userRef.update("marcoActual", marcoId).await()

                _state.update { state ->
                    state.copy(
                        userData = state.userData.copy(marcoActual = marcoId)
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = "Error al cambiar marco: ${e.message}") }
            }
        }
    }

    private fun buyAvatar(avatar: Avatar) {
        viewModelScope.launch {
            try {
                val user = _state.value.userData
                if (user.avataresDesbloqueados.contains(avatar.id)) {
                    _state.update { it.copy(errorMessage = "Ya tienes este avatar") }
                    return@launch
                }

                val price = avatar.price ?: 0
                if (user.dinero < price) {
                    _state.update { it.copy(errorMessage = "No tienes suficiente dinero") }
                    return@launch
                }

                val userRef = firestore.collection("usuarios").document(user.uid)

                firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(userRef)
                    val currentDinero = snapshot.getLong("dinero")?.toInt() ?: 0
                    val currentAvatars =
                        (snapshot.get("avataresDesbloqueados") as? List<String>) ?: emptyList()

                    if (currentDinero < price) throw Exception("Dinero insuficiente")

                    val updatedAvatars = currentAvatars + avatar.id
                    transaction.update(
                        userRef,
                        "dinero", currentDinero - price,
                        "avataresDesbloqueados", updatedAvatars
                    )
                }.await()

                // actualizar estado local
                _state.update { st ->
                    st.copy(
                        userData = st.userData.copy(
                            dinero = user.dinero - price,
                            avataresDesbloqueados = user.avataresDesbloqueados + avatar.id
                        ),
                        errorMessage = null
                    )
                }

            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = "Error al comprar: ${e.message}") }
            }
        }
    }

    private fun updateUserData(userData: UserData) {
        _state.update {
            it.copy(
                userData = userData,
                isLoading = false,
                errorMessage = null
            )
        }
    }

    fun markLessonAsCompleted(lessonId: String, expEarned: Int, dineroEarned: Int) {
        viewModelScope.launch {
            try {
                // 1. Obtener la categoría actual basada en el índice de sección
                val categoryId = when (_state.value.currentSectionIndex) {
                    0 -> "ahorro"
                    1 -> "compra"
                    2 -> "basica"
                    3 -> "inversion"
                    else -> "ahorro" // Default
                }

                Log.d("UserVM", "Guardando lección $lessonId en categoría $categoryId")

                val userRef = firestore.collection("usuarios").document(_state.value.userData.uid)

                // 2. Transacción para actualizar datos
                firestore.runTransaction { transaction ->
                    val userDoc = transaction.get(userRef)
                    val currentExp = userDoc.getLong("exp")?.toInt() ?: 0
                    val currentDinero = userDoc.getLong("dinero")?.toInt() ?: 0

                    // Estructura anidada: leccionesCompletadas/{categoriaID}/{leccionID}
                    val completedLessons =
                        userDoc.get("leccionesCompletadas") as? Map<String, Any> ?: emptyMap()
                    val categoryLessons =
                        completedLessons[categoryId] as? Map<String, Any> ?: emptyMap()

                    // Actualizar lecciones completadas para la categoría
                    val updatedCategoryLessons = categoryLessons.toMutableMap().apply {
                        this[lessonId] = true
                    }

                    // Actualizar progreso de categoría
                    val currentProgress =
                        userDoc.get("progresoCategorias") as? Map<String, Any> ?: emptyMap()
                    val updatedProgress = currentProgress.toMutableMap().apply {
                        this[categoryId] = (this[categoryId] as? Long ?: 0) + 1
                    }

                    transaction.update(
                        userRef,
                        "exp",
                        currentExp + expEarned,
                        "dinero",
                        currentDinero + dineroEarned,
                        "progresoCategorias",
                        updatedProgress,
                        "leccionesCompletadas.$categoryId",
                        updatedCategoryLessons // Notación de puntos
                    )
                }.await()

                Log.d("UserVM", "Lección $lessonId completada en categoría $categoryId")
                loadUserData(_state.value.userData.uid) // Refrescar datos

            } catch (e: Exception) {
                Log.e("UserVM", "Error al guardar progreso: ${e.message}", e)
                _state.update { it.copy(errorMessage = "Error al guardar progreso: ${e.message}") }
            }
        }
    }

    private fun setError(message: String) {
        _state.update {
            it.copy(
                isLoading = false,
                errorMessage = message
            )
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    private fun setLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }

    fun logout() {
        _state.update { UserState() }
    }

    fun clearState() {
        _state.value = UserState() // tu estado inicial
    }


    fun setCurrentSection(index: Int) {
        _state.update { it.copy(currentSectionIndex = index) }
        Log.d("ViewModel", "Section set to: $index")
    }
}