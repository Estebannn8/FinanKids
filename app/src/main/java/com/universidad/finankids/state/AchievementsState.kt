package com.universidad.finankids.state

import com.universidad.finankids.data.model.Logro
import com.universidad.finankids.data.model.LogroUI
import com.universidad.finankids.data.model.UserAchievement

data class AchievementsState(
    val globalAchievements: List<Logro> = emptyList(),
    val userAchievements: List<UserAchievement> = emptyList(),
    val uiAchievements: List<LogroUI> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)
