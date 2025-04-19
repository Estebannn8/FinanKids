package com.universidad.finankids.data.model

data class Avatar(
    val name: String = "",
    val imageUrl: String = "",
    val rarity: String = "",
    val unlockType: String = "",
    val price: Int? = null,
    val achievementId: String? = null,
    val active: Boolean = true,
    val id: String = ""
)
