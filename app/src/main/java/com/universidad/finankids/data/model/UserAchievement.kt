package com.universidad.finankids.data.model

data class UserAchievement(
    val logroId: String = "",
    val unlocked: Boolean = false,
    val claimed: Boolean = false,
    val progress: Int = 0
)
