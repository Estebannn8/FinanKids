package com.universidad.finankids.viewmodel

import android.util.Log
import androidx.compose.runtime.IntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.universidad.finankids.data.model.Avatar
import com.universidad.finankids.events.UserEvent
import com.universidad.finankids.data.model.UserData
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

    // Estado actual
    private val _state = MutableStateFlow(UserData())
    val state: StateFlow<UserData> = _state.asStateFlow()

    // Canal para eventos
    private val _events = Channel<UserEvent>(Channel.UNLIMITED)
    val events = _events.receiveAsFlow()

    // Add this for section tracking
    private val _currentSectionIndex = mutableIntStateOf(0)
    val currentSectionIndex: IntState = _currentSectionIndex

    // Avatar
    private val _avatarData = MutableStateFlow<Avatar?>(null)
    val avatarData: StateFlow<Avatar?> = _avatarData.asStateFlow()


    fun setCurrentSection(index: Int) {
        _currentSectionIndex.intValue = index
    }

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
            is UserEvent.LoadAvatar -> loadAvatarData(event.avatarId)
            // is UserEvent.LoadMarco -> loadMarcoData(event.marcoId)
            UserEvent.Logout -> logout()
            is UserEvent.LoadingSuccess -> updateUserData(event.userData)
            is UserEvent.LoadingFailed -> setError(event.error)
            UserEvent.LoadingStarted -> setLoading(true)
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

                _state.value = userData.copy(isLoading = false)
            } catch (e: Exception) {
                Log.e("UserFlow", "Error al cargar usuario", e)
                _state.value = UserData(
                    isLoading = false,
                    errorMessage = "Error al cargar datos: ${e.message}"
                )
            }
        }
    }

    fun loadAvatarData(avatarId: String) {
        if (avatarId.isEmpty()) return

        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("avatars").document(avatarId).get().await()
                val avatar = snapshot.toObject(Avatar::class.java)
                if (avatar != null) {
                    _avatarData.value = avatar
                    Log.d("UserFlow", "Avatar cargado: $avatar")
                } else {
                    Log.w("UserFlow", "Avatar no encontrado con ID: $avatarId")
                }
            } catch (e: Exception) {
                Log.e("UserFlow", "Error al cargar avatar", e)
            }
        }
    }

    fun loadMarcoData(){
        // Funcion para cargar marco
    }


    private fun updateUserData(userData: UserData) {
        _state.update {
            userData.copy(
                isLoading = false,
                errorMessage = null
            )
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

    private fun setLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }

    private fun logout() {
        _state.update { UserData() }
    }


}