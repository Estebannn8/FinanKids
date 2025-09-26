package com.universidad.finankids.events

import com.universidad.finankids.data.model.Avatar
import com.universidad.finankids.data.model.UserData

sealed class UserEvent {
    data class LoadUser(val uid: String) : UserEvent()
    object Logout : UserEvent()

    // Estados de carga como eventos
    object LoadingStarted : UserEvent()
    data class LoadingSuccess(val userData: UserData) : UserEvent()
    data class LoadingFailed(val error: String) : UserEvent()

    object ClearError : UserEvent()

    data class ChangeAvatar(val avatarId: String) : UserEvent()
    data class ChangeMarco(val marcoId: String) : UserEvent()

    data class ChangeNickname(val newName: String) : UserEvent()

    data class BuyAvatar(val avatar: Avatar) : UserEvent()

}