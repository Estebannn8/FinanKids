package com.universidad.finankids.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.universidad.finankids.data.model.Avatar
import com.universidad.finankids.events.AvatarEvent
import com.universidad.finankids.state.AvatarState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AvataresViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    // Estado inicial
    private val _state = MutableStateFlow(AvatarState())
    val state: StateFlow<AvatarState> = _state.asStateFlow()

    private val _events = Channel<AvatarEvent>(Channel.UNLIMITED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is AvatarEvent.LoadAllAvatars -> loadAllAvatars()
                    is AvatarEvent.LoadAvatarById -> loadAvatarById(event.avatarId)
                    is AvatarEvent.ClearError -> clearError()
                }
            }
        }
    }

    fun sendEvent(event: AvatarEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }

    private suspend fun loadAllAvatars() {
        _state.update { it.copy(isLoading = true, error = null) }
        try {
            val result = firestore.collection("avatars").get().await()
            val avatars = result.documents.mapNotNull {
                it.toObject(Avatar::class.java)?.copy(id = it.id)
            }
            _state.update {
                it.copy(
                    avatarList = avatars,
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar avatares"
                )
            }
        }
    }

    private suspend fun loadAvatarById(id: String) {
        _state.update { it.copy(isLoading = true, error = null) }
        try {
            val doc = firestore.collection("avatars").document(id).get().await()
            val avatar = doc.toObject(Avatar::class.java)?.copy(id = doc.id)
            _state.update {
                it.copy(
                    currentAvatar = avatar,
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar el avatar"
                )
            }
        }
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
