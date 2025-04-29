package com.universidad.finankids.state

import com.universidad.finankids.data.model.UserData

data class UserState(
    val userData: UserData = UserData(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentSectionIndex: Int = 0,
) {
    val nivel: Int
        get() = calculateCurrentLevel(userData.exp)

    val levelProgress: Float
        get() = calculateLevelProgress(userData.exp)

    val expForCurrentLevel: Int
        get() = calculateExpForLevel(nivel - 1)

    val expForNextLevel: Int
        get() = calculateExpForLevel(nivel)

    val expInCurrentLevel: Int
        get() = (userData.exp - expForCurrentLevel).coerceAtLeast(0)

    val expNeededToNextLevel: Int
        get() = (expForNextLevel - expForCurrentLevel).coerceAtLeast(1)

    companion object {
        private const val BASE_EXP_REQUIRED = 100
        private const val EXP_GROWTH_RATE = 1.2
        private const val MAX_LEVEL = 100

        private fun calculateCurrentLevel(totalExp: Int): Int {
            if (totalExp < BASE_EXP_REQUIRED) return 1

            var expForNextLevel = BASE_EXP_REQUIRED
            var accumulatedExp = 0
            var level = 0

            while (accumulatedExp <= totalExp) {
                level++
                accumulatedExp += expForNextLevel
                expForNextLevel = (expForNextLevel * EXP_GROWTH_RATE).toInt()
            }

            return level
        }

        private fun calculateExpForLevel(level: Int): Int {
            if (level <= 0) return 0

            var exp = BASE_EXP_REQUIRED
            var accumulatedExp = 0

            for (i in 1..level) {
                accumulatedExp += exp
                exp = (exp * EXP_GROWTH_RATE).toInt()
            }

            return accumulatedExp
        }


        private fun calculateLevelProgress(totalExp: Int): Float {
            val currentLevel = calculateCurrentLevel(totalExp)

            if (currentLevel >= MAX_LEVEL) return 1f

            val expForCurrentLevel = calculateExpForLevel(currentLevel - 1)
            val expForNextLevel = calculateExpForLevel(currentLevel)
            val expInCurrentLevel = totalExp - expForCurrentLevel
            val expNeeded = expForNextLevel - expForCurrentLevel

            return (expInCurrentLevel.toFloat() / expNeeded.toFloat()).coerceIn(0f, 1f)
        }
    }
}
