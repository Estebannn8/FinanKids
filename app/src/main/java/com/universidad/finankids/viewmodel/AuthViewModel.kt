package com.universidad.finankids.viewmodel

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.universidad.finankids.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Estados para manejar la autenticación
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successEvent = MutableStateFlow(false)
    val successEvent: StateFlow<Boolean> = _successEvent.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    // Función para resetear los estados
    fun resetAuthState() {
        _errorMessage.value = null
        _successEvent.value = false
        _loading.value = false
    }

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _termsAccepted = MutableStateFlow(false)
    val termsAccepted: StateFlow<Boolean> = _termsAccepted

    fun onTermsAcceptedChanged(accepted: Boolean) {
        _termsAccepted.value = accepted
    }

    fun onUsernameChanged(newUsername: String) {
        _username.value = newUsername
    }

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun register(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentEmail = email.value.trim()
        val currentPassword = password.value
        val currentUsername = username.value.trim()

        if (currentEmail.isEmpty() || currentPassword.isEmpty() || currentUsername.isEmpty()) {
            onError("Por favor completa todos los campos.")
            return
        }

        if (!termsAccepted.value) {
            onError("Debes aceptar los términos y condiciones.")
            return
        }

        if (!isValidPassword(currentPassword)) {
            onError("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.")
            return
        }

        if (!isValidEmail(currentEmail)) {
            onError("Por favor ingresa un correo electrónico válido.")
            return
        }

        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(currentEmail, currentPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.updateProfile(
                            userProfileChangeRequest {
                                displayName = currentUsername
                            }
                        )

                        val userId = user?.uid ?: return@addOnCompleteListener

                        val userData = hashMapOf(
                            "uid" to userId,
                            "nickname" to currentUsername,
                            "correo" to currentEmail,
                            "avatarActual" to "avatar_01",
                            "marcoActual" to "clasico",
                            "avataresDesbloqueados" to listOf("avatar_01"),
                            "marcosDesbloqueados" to listOf("clasico"),
                            "nivel" to 1,
                            "exp" to 0,
                            "dinero" to 0,
                            "logros" to emptyList<String>(),
                            "insignias" to emptyList<String>(),
                            "progresoCategorias" to mapOf(
                                "ahorro" to 0,
                                "inversion" to 0,
                                "deudas" to 0
                            ),
                            "leccionesCompletadas" to emptyMap<String, Any>(),
                            "racha" to mapOf(
                                "actual" to 0,
                                "maxima" to 0,
                                "ultimoRegistro" to null
                            )
                        )

                        firestore.collection("usuarios")
                            .document(userId)
                            .set(userData)
                            .addOnSuccessListener {
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                onError("Error al guardar usuario: ${e.message}")
                            }

                    } else {
                        onError(task.exception?.localizedMessage ?: "Error al registrar usuario.")
                    }
                }
        }
    }

    fun login(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentEmail = email.value.trim()
        val currentPassword = password.value

        if (currentEmail.isEmpty() || currentPassword.isEmpty()) {
            onError("Por favor completa todos los campos.")
            return
        }

        viewModelScope.launch {
            auth.signInWithEmailAndPassword(currentEmail, currentPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onError(task.exception?.localizedMessage ?: "Error al iniciar sesión.")
                    }
                }
        }
    }

    // Función para iniciar el flujo de Google Sign-In
    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            try {
                val credentialManager = CredentialManager.create(context)

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .setAutoSelectEnabled(true)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val response = credentialManager.getCredential(
                    request = request,
                    context = context
                )

                handleGoogleSignInResult(response.credential)
            } catch (e: GetCredentialException) {
                _errorMessage.value = "Error en autenticación: ${e.message}"
            } catch (e: Exception) {
                _errorMessage.value = "Error desconocido: ${e.message}"
            }
        }
    }

    private fun handleGoogleSignInResult(credential: Credential) {
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

            try {
                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                val firebaseCredential = GoogleAuthProvider.getCredential(
                    googleIdTokenCredential.idToken,
                    null
                )

                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            handleNewGoogleUser(task.result?.additionalUserInfo?.isNewUser ?: false)
                        } else {
                            _errorMessage.value = task.exception?.message ?: "Error en autenticación"
                        }
                    }
            } catch (e: Exception) {
                _errorMessage.value = "Error procesando credencial: ${e.message}"
            }
        } else {
            _errorMessage.value = "Tipo de credencial no soportado"
        }
    }

    private fun handleNewGoogleUser(isNewUser: Boolean) {
        if (isNewUser) {
            saveNewGoogleUser(
                onSuccess = { _successEvent.value = true },
                onError = { error -> _errorMessage.value = error }
            )
        } else {
            _successEvent.value = true
        }
    }

    private fun saveNewGoogleUser(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val user = auth.currentUser ?: run {
            onError("Usuario no encontrado")
            return
        }

        viewModelScope.launch {
            try {
                val uniqueNickname = generateUniqueNickname()

                val userData = hashMapOf(
                    "uid" to user.uid,
                    "nickname" to uniqueNickname,
                    "correo" to (user.email ?: ""),
                    "avatarActual" to "avatar_01",
                    "marcoActual" to "clasico",
                    "avataresDesbloqueados" to listOf("avatar_01"),
                    "marcosDesbloqueados" to listOf("clasico"),
                    "nivel" to 1,
                    "exp" to 0,
                    "dinero" to 0,
                    "logros" to emptyList<String>(),
                    "insignias" to emptyList<String>(),
                    "progresoCategorias" to mapOf(
                        "ahorro" to 0,
                        "inversion" to 0,
                        "deudas" to 0
                    ),
                    "leccionesCompletadas" to emptyMap<String, Any>(),
                    "racha" to mapOf(
                        "actual" to 0,
                        "maxima" to 0,
                        "ultimoRegistro" to null
                    )
                )

                firestore.collection("usuarios")
                    .document(user.uid)
                    .set(userData)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e ->
                        onError("Error al guardar usuario: ${e.message}")
                    }

            } catch (e: Exception) {
                onError("Error al generar nickname: ${e.message}")
            }
        }
    }


    private suspend fun generateUniqueNickname(): String {
        var nickname: String
        var exists: Boolean

        do {
            nickname = generateRandomNickname()
            val result = firestore.collection("usuarios")
                .whereEqualTo("nickname", nickname)
                .get()
                .await()
            exists = !result.isEmpty
        } while (exists)

        return nickname
    }


    // Función para generar un nickname aleatorio
    private fun generateRandomNickname(): String {
        val prefixes = listOf("FinanKid", "MoneyHero", "CashMaster", "Piggy", "SaveBoss")
        val randomPrefix = prefixes.random()
        val randomNumber = (100..999).random()
        return "$randomPrefix$randomNumber"
    }

    fun clearFields() {
        _username.value = ""
        _email.value = ""
        _password.value = ""
        _termsAccepted.value = false
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-={}:;<>?,.]).{8,}\$")
        return passwordRegex.matches(password)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        return emailRegex.matches(email)
    }
}
