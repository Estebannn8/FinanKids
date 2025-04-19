package com.universidad.finankids.events

import com.universidad.finankids.data.model.UserData

sealed class UserEvent {
    data class LoadUser(val uid: String) : UserEvent()
    data class LoadAvatar(val avatarId: String) : UserEvent()
    // data class LoadMarco(val marcoId: String) : UserEvent()
    object Logout : UserEvent()

    // Estados de carga como eventos
    object LoadingStarted : UserEvent()
    data class LoadingSuccess(val userData: UserData) : UserEvent()
    data class LoadingFailed(val error: String) : UserEvent()
}