package com.universidad.finankids.state

import com.universidad.finankids.data.model.Avatar

data class AvatarState(
    val avatarList: List<Avatar> = emptyList(),
    val currentAvatar: Avatar? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
