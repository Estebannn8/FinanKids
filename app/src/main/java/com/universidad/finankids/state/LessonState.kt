package com.universidad.finankids.state

import com.universidad.finankids.data.model.ActivityContent
import com.universidad.finankids.data.model.Lesson
import com.universidad.finankids.data.model.MatchingPair

data class LessonState(
    val currentLesson: Lesson? = null,
    val currentActivity: ActivityContent? = null,
    val currentActivityIndex: Int = 0,
    val progress: Float = 0f,
    val lives: Int = 5,
    val errorCount: Int = 0,
    val isLessonLocked: Boolean = false,
    val showFeedback: Boolean = false,
    val lastAnswerCorrect: Boolean? = null,
    val feedbackText: String = "",
    val selectedAnswer: String? = null,
    val matchedPairs: Set<MatchingPair> = emptySet(),
    val selectedLeft: String? = null,
    val selectedRight: String? = null,
    val rightItems: List<String> = emptyList(),
    val leftItems: List<String> = emptyList(),
    val placedWords: List<String?> = emptyList(),
    val availableWords: List<String> = emptyList(),
    val earnedExp: Int = 0,
    val earnedDinero: Int = 0,
    val showCompleteScreen: Boolean = false,
    val showExitConfirmation: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val noMoreLessons: Boolean = false,
    val isLastActivityInLesson: Boolean = false
)