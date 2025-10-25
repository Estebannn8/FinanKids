package com.universidad.finankids.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.universidad.finankids.data.model.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserSettingsViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val _settings = MutableStateFlow(UserSettings())
    val settings: StateFlow<UserSettings> = _settings.asStateFlow()

    fun loadUserSettings(uid: String) {
        if (uid.isEmpty()) return

        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("userSettings").document(uid).get().await()
                val userSettings = snapshot.toObject(UserSettings::class.java)
                    ?: createDefaultSettings(uid)

                _settings.value = userSettings
            } catch (e: Exception) {
                // Si no existe, crear settings por defecto
                createDefaultSettings(uid)
            }
        }
    }

    private suspend fun createDefaultSettings(uid: String): UserSettings {
        val defaultSettings = UserSettings(uid = uid)
        firestore.collection("userSettings").document(uid).set(defaultSettings).await()
        _settings.value = defaultSettings
        return defaultSettings
    }

    fun updateBackgroundColor(uid: String, colorHex: String) {
        viewModelScope.launch {
            try {
                firestore.collection("userSettings").document(uid)
                    .update("colorFondoPerfil", colorHex).await()

                _settings.value = _settings.value.copy(colorFondoPerfil = colorHex)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleMusica(uid: String) {
        viewModelScope.launch {
            try {
                val newValue = !_settings.value.musicaActiva
                firestore.collection("userSettings").document(uid)
                    .update("musicaActiva", newValue).await()

                _settings.value = _settings.value.copy(musicaActiva = newValue)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleSonido(uid: String) {
        viewModelScope.launch {
            try {
                val newValue = !_settings.value.sonidoActiva
                firestore.collection("userSettings").document(uid)
                    .update("sonidoActiva", newValue).await()

                _settings.value = _settings.value.copy(sonidoActiva = newValue)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearState() {
        _settings.value = UserSettings()
    }
}