package com.universidad.finankids.events

sealed class AchievementTrigger {
    data class LessonCompleted(val uid: String, val lessonId: String, val category: String, val perfectLesson: Boolean) : AchievementTrigger()
    data class StreakChanged(val uid: String, val currentStreak: Int) : AchievementTrigger()
    data class LevelChanged(val uid: String, val newLevel: Int) : AchievementTrigger()
    data class BankBalanceChanged(val uid: String, val montoDepositado: Int) : AchievementTrigger()
    data class AvatarPurchased(val uid: String, val totalAvatarsOwned: Int) : AchievementTrigger()
    data class ProfileOpenedFirstTime(val uid: String) : AchievementTrigger()
    data class ManualProgress(val uid: String, val logroId: String, val amount: Int) : AchievementTrigger()
}