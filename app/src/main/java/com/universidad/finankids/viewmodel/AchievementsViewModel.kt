package com.universidad.finankids.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.universidad.finankids.data.model.AchievementNotification
import com.universidad.finankids.events.AchievementTrigger
import com.universidad.finankids.data.model.Logro
import com.universidad.finankids.data.model.LogroUI
import com.universidad.finankids.data.model.UserAchievement
import com.universidad.finankids.events.AchievementsEventBus
import com.universidad.finankids.state.AchievementsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AchievementsViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _state = MutableStateFlow(AchievementsState())
    val state = _state.asStateFlow()

    init {
        // Escuchamos TODOS los triggers globales de logros
        viewModelScope.launch {
            AchievementsEventBus.events.collect { trigger: AchievementTrigger  ->
                when (trigger) {
                    is AchievementTrigger.LessonCompleted -> handleLessonCompleted(trigger)
                    is AchievementTrigger.StreakChanged -> handleStreakChanged(trigger)
                    is AchievementTrigger.LevelChanged -> handleLevelChanged(trigger)
                    is AchievementTrigger.BankBalanceChanged -> handleBankBalanceChanged(trigger)
                    is AchievementTrigger.AvatarPurchased -> handleAvatarPurchased(trigger)
                    is AchievementTrigger.ProfileOpenedFirstTime -> handleProfileOpened(trigger)
                    is AchievementTrigger.ManualProgress -> handleManualProgress(trigger)
                }
            }
        }
    }

    // ---------------------------------------------------------
    // Carga inicial de logros globales y del usuario
    // ---------------------------------------------------------
    fun load(uid: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)

            try {
                val globalSnap = firestore.collection("logros")
                    .orderBy("orden")
                    .get().await()
                val global = globalSnap.toObjects(Logro::class.java)

                val userSnap = firestore.collection("usuarios")
                    .document(uid)
                    .collection("logrosUsuario")
                    .get().await()
                val user = userSnap.toObjects(UserAchievement::class.java)

                _state.value = _state.value.copy(
                    globalAchievements = global,
                    userAchievements = user,
                    uiAchievements = fuseAchievements(global, user),
                    loading = false
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message,
                    loading = false
                )
            }
        }
    }

    // ---------------------------------------------------------
    // Fusión Global + Usuario → LogroUI para la pantalla
    // ---------------------------------------------------------
    private fun fuseAchievements(
        global: List<Logro>,
        user: List<UserAchievement>
    ): List<LogroUI> = global.map { logro ->
        val userAch = user.find { it.logroId == logro.id }

        val unlocked = userAch?.unlocked ?: false
        val claimed = userAch?.claimed ?: false
        val progressActual = userAch?.progress ?: 0
        val progresoTotal = logro.progresoRequerido ?: 1

        LogroUI(
            logro = logro,
            desbloqueado = unlocked,
            reclamado = claimed,
            progresoActual = progressActual,
            progresoTotal = progresoTotal
        )
    }

    // ---------------------------------------------------------
    // Helpers generales
    // ---------------------------------------------------------
    private suspend fun incrementProgress(uid: String, logroId: String, amount: Int) {
        val doc = firestore.collection("usuarios")
            .document(uid)
            .collection("logrosUsuario")
            .document(logroId)

        val snapshot = doc.get().await()
        val ua = snapshot.toObject(UserAchievement::class.java)

        val newProgress = (ua?.progress ?: 0) + amount
        val logro = _state.value.globalAchievements.find { it.id == logroId }
        val required = logro?.progresoRequerido ?: 1
        val unlocked = newProgress >= required

        doc.set(
            UserAchievement(
                logroId = logroId,
                progress = newProgress,
                unlocked = unlocked,
                claimed = ua?.claimed ?: false
            ),
            SetOptions.merge()
        ).await()

        refresh(uid)
    }

    private suspend fun unlockInstant(uid: String, logroId: String) {
        firestore.collection("usuarios")
            .document(uid)
            .collection("logrosUsuario")
            .document(logroId)
            .set(
                UserAchievement(
                    logroId = logroId,
                    progress = 1,
                    unlocked = true,
                    claimed = false
                ),
                SetOptions.merge()
            ).await()

        val logro = _state.value.globalAchievements.find { it.id == logroId }

        logro?.let {
            addNotification(
                AchievementNotification(
                    logroId = logroId,
                    titulo = "¡Logro desbloqueado!",
                    recompensa = it.dineroRecompensa,
                    icono = it.iconoUrl
                )
            )
        }

        refresh(uid)
    }

    private fun refresh(uid: String) = load(uid)

    // ---------------------------------------------------------
    // HANDLERS DE EVENTOS (tu lógica real de logros)
    // ---------------------------------------------------------

    private suspend fun handleLessonCompleted(e: AchievementTrigger.LessonCompleted) {
        // Progreso genérico de lecciones
        incrementProgress(e.uid, "escalador_financiero", 1)
        incrementProgress(e.uid, "maquina_financiera", 1)

        // Logro al completar tu primera lección
        unlockInstant(e.uid, "lanzamiento_epico")

        // Perfección: sin perder vidas
        if (e.perfectLesson) {
            unlockInstant(e.uid, "tiro_perfecto")
        }

        // Categorías: completarlas todas
        incrementProgress(e.uid, "explorador_global", 1)
    }

    private suspend fun handleStreakChanged(e: AchievementTrigger.StreakChanged) {
        incrementProgress(e.uid, "constancia_dorada", 1)
        incrementProgress(e.uid, "racha_ardiente", 1)
        incrementProgress(e.uid, "agenda_inteligente", 1)
    }

    private suspend fun handleLevelChanged(e: AchievementTrigger.LevelChanged) {
        if (e.newLevel >= 5) unlockInstant(e.uid, "medalla_bronce")
        if (e.newLevel >= 20) unlockInstant(e.uid, "campeon_dorado")
    }

    private suspend fun handleBankBalanceChanged(e: AchievementTrigger.BankBalanceChanged) {
        // Logro 1: primer ahorro (1000)
        incrementProgress(e.uid, "primer_ahorro", e.montoDepositado)

        // Logro 2: caja fuerte (25000)
        incrementProgress(e.uid, "caja_fuerte", e.montoDepositado)
    }

    private suspend fun handleAvatarPurchased(e: AchievementTrigger.AvatarPurchased) {

        // 1) Comprador inteligente → solo una vez
        if (!isAchievementUnlocked(e.uid, "comprador_inteligente")) {
            unlockInstant(e.uid, "comprador_inteligente")
        }

        // 2) Progreso acumulativo para “desbloqueador”
        incrementProgress(e.uid, "desbloqueador", 1)
    }


    private suspend fun handleProfileOpened(e: AchievementTrigger.ProfileOpenedFirstTime) {
        unlockInstant(e.uid, "cientifico_dinero")
    }

    private suspend fun handleManualProgress(e: AchievementTrigger.ManualProgress) {
        incrementProgress(e.uid, e.logroId, e.amount)
    }

    // ---------------------------------------------------------
    // Reclamación de recompensa
    // ---------------------------------------------------------
    fun claimReward(uid: String, logroId: String) {
        viewModelScope.launch {
            try {
                firestore.collection("usuarios")
                    .document(uid)
                    .collection("logrosUsuario")
                    .document(logroId)
                    .update("claimed", true).await()

                val logro = _state.value.globalAchievements.find { it.id == logroId }

                logro?.let {
                    addNotification(
                        AchievementNotification(
                            logroId = logroId,
                            titulo = "¡Recompensa reclamada!",
                            recompensa = it.dineroRecompensa,
                            icono = it.iconoUrl
                        )
                    )
                }

                refresh(uid)

            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    private suspend fun setProgressTo(uid: String, logroId: String, value: Int) {
        val doc = firestore.collection("usuarios")
            .document(uid)
            .collection("logrosUsuario")
            .document(logroId)

        val logro = _state.value.globalAchievements.find { it.id == logroId }
        val required = logro?.progresoRequerido ?: 1
        val unlocked = value >= required

        doc.set(
            UserAchievement(
                logroId = logroId,
                progress = value,
                unlocked = unlocked,
                claimed = false, // puedes poner false si SÓLO desbloquea
            ),
            SetOptions.merge()
        ).await()

        refresh(uid)
    }


    suspend fun isAchievementUnlocked(uid: String, logroId: String): Boolean {
        val doc = firestore.collection("usuarios")
            .document(uid)
            .collection("logrosUsuario")
            .document(logroId)
            .get()
            .await()

        return doc.getBoolean("unlocked") == true
    }


    private fun addNotification(notif: AchievementNotification) {
        _state.value = _state.value.copy(
            notifications = _state.value.notifications + notif
        )
    }

    fun removeNotification(notif: AchievementNotification) {
        _state.value = _state.value.copy(
            notifications = _state.value.notifications - notif
        )
    }

}


