package com.universidad.finankids.state

data class AuthState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val termsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSelected: Boolean = true,
    val isSuccess: Boolean = false,
    val recoveryEmail: String = "",
    val isRecoverySuccess: Boolean = false,
    val recoveryMessage: String? = null
)