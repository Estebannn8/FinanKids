package com.universidad.finankids.events

import com.universidad.finankids.data.model.MatchingPair

sealed class LessonEvent {
    data class LoadLessonAndInitialize(
        val categoryId: String,
        val completedLessons: Map<String, Any>
    ) : LessonEvent()
    object ContinueActivity : LessonEvent()
    object MoveToNextActivity : LessonEvent()
    object RestartLesson : LessonEvent()
    object ExitLesson : LessonEvent()
    data class SelectAnswer(val answer: String) : LessonEvent()
    data class MatchPair(val pair: MatchingPair) : LessonEvent()
    data class SelectLeftItem(val item: String?) : LessonEvent()
    data class SelectRightItem(val item: String?) : LessonEvent()
    data class UpdateRightItems(val items: List<String>) : LessonEvent()
    data class PlaceWord(val word: String, val position: Int) : LessonEvent()
    data class RemoveWord(val position: Int) : LessonEvent()
    object ShowExitConfirmation : LessonEvent()
    object HideExitConfirmation : LessonEvent()
    object CompleteLesson : LessonEvent()
    object ClearError : LessonEvent()
    object ResetCurrentActivity : LessonEvent()
    object HideFeedback : LessonEvent()
    object ShowCompleteScreen : LessonEvent()
}