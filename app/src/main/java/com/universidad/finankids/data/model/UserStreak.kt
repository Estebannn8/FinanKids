package com.universidad.finankids.data.model

import java.util.Date

data class UserStreak(
    val uid: String = "",
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastActivityDate: Date? = null,
    val streakHistory: Map<String, Boolean> = emptyMap() // formato: "yyyy-MM-dd" -> true/false
)