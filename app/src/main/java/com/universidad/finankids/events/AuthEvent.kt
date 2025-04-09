package com.universidad.finankids.events

sealed class AuthEvent {
    // Eventos de formulario
    data class EmailChanged(val email: String) : AuthEvent()
    data class PasswordChanged(val password: String) : AuthEvent()
    data class UsernameChanged(val username: String) : AuthEvent()
    data class TermsAcceptedChanged(val accepted: Boolean) : AuthEvent()
    data class RecoveryEmailChanged(val email: String) : AuthEvent()

    // Eventos de acciones
    object Login : AuthEvent()
    object Register : AuthEvent()
    object SignInWithGoogle : AuthEvent()
    object SignInWithFacebook : AuthEvent()
    object NavigateToRegister : AuthEvent()
    object NavigateToLogin : AuthEvent()
    object ForgotPassword : AuthEvent()
    object SendPasswordReset : AuthEvent()
}