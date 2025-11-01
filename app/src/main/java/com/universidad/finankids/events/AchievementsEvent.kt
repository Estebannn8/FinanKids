package com.universidad.finankids.events

sealed class AchievementsEvent {
    data class OnLoadAchievements(val uid: String) : AchievementsEvent()
    data class OnProgressUpdate(val uid: String, val logroId: String, val amount: Int) : AchievementsEvent()
    data class OnUnlockAchievement(val uid: String, val logroId: String) : AchievementsEvent()
    data class OnClaimReward(val uid: String, val logroId: String) : AchievementsEvent()
}
