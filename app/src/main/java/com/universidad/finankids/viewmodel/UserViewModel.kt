package com.universidad.finankids.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
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
            UserEvent.Logout -> logout()
            is UserEvent.LoadingSuccess -> updateUserData(event.userData)
            is UserEvent.LoadingFailed -> setError(event.error)
            UserEvent.LoadingStarted -> setLoading(true)
            UserEvent.ClearError -> clearError()
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

    private fun updateUserData(userData: UserData) {
        _state.update {
            it.copy(
                userData = userData,
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

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    private fun setLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }

    private fun logout() {
        _state.update { UserState() }
    }

    fun setCurrentSection(index: Int) {
        _state.update { it.copy(currentSectionIndex = index) }
        Log.d("ViewModel", "Section set to: $index")
    }
}