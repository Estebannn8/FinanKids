package com.universidad.finankids.events

sealed class AvatarEvent {
    object LoadAllAvatars : AvatarEvent()
    data class LoadAvatarById(val avatarId: String) : AvatarEvent()

    object ClearError : AvatarEvent()
}
