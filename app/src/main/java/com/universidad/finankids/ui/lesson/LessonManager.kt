package com.universidad.finankids.ui.lesson

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.universidad.finankids.data.model.ActivityContent
import com.universidad.finankids.data.model.ActivityType
import com.universidad.finankids.data.model.MatchingPair

class LessonManager(
    val activities: List<ActivityContent>,
    val onExitLesson: () -> Unit,
    val onLessonComplete: (Int, Int) -> Unit
) {
    private val _currentActivityIndex = mutableIntStateOf(0)
    var currentActivityIndex: Int
        get() = _currentActivityIndex.intValue
        set(value) { _currentActivityIndex.intValue = value }

    private val _lives = mutableIntStateOf(5)
    var lives: Int
        get() = _lives.intValue
        set(value) { _lives.intValue = value }

    private val _errorCount = mutableIntStateOf(0)
    var errorCount: Int
        get() = _errorCount.intValue
        set(value) { _errorCount.intValue = value }

    private val _isLessonLocked = mutableStateOf(false)
    var isLessonLocked: Boolean
        get() = _isLessonLocked.value
        set(value) { _isLessonLocked.value = value }

    val baseExp = 100
    val baseDinero = 500

    val currentActivity: ActivityContent
        get() = activities.getOrNull(currentActivityIndex) ?: run {
            onLessonComplete(0, 0)
            activities.first()
        }

    val progress: Float
        get() = if (activities.isEmpty()) 0f else currentActivityIndex.toFloat() / activities.size

    private val _showFeedback = mutableStateOf(false)
    var showFeedback by _showFeedback

    private val _lastAnswerCorrect = mutableStateOf<Boolean?>(null)
    var lastAnswerCorrect by _lastAnswerCorrect

    private val _feedbackText = mutableStateOf("")
    var feedbackText by _feedbackText

    private val _selectedAnswer = mutableStateOf<String?>(null)
    var selectedAnswer by _selectedAnswer

    private val _matchedPairs = mutableStateOf<Set<MatchingPair>>(emptySet())
    var matchedPairs by _matchedPairs

    private val _selectedLeft = mutableStateOf<String?>(null)
    var selectedLeft by _selectedLeft

    private val _selectedRight = mutableStateOf<String?>(null)
    var selectedRight by _selectedRight

    private val _rightItems = mutableStateOf<List<String>>(emptyList())
    var rightItems by _rightItems

    private val _currentDragIndex = mutableStateOf<Int?>(null)
    var currentDragIndex by _currentDragIndex

    private val _placedWords = mutableStateOf<List<String?>>(emptyList())
    var placedWords by _placedWords

    private val _availableWords = mutableStateOf<List<String>>(emptyList())
    var availableWords by _availableWords

    private val _leftItems = mutableStateOf<List<String>>(emptyList())
    var leftItems by _leftItems

    init {
        require(activities.isNotEmpty()) { "Activities list cannot be empty" }
        _currentActivityIndex.intValue = 0
        initializeCurrentActivity()
    }

    fun initializeCurrentActivity() {
        val activity = currentActivity

        when (activity.type) {
            ActivityType.FillBlank, ActivityType.MultipleChoice -> selectedAnswer = null
            ActivityType.Matching -> {
                matchedPairs = emptySet()
                selectedLeft = null
                selectedRight = null
            }
            ActivityType.DragPairs -> {
                val pairs = activity.orderedPairs ?: emptyList()
                leftItems = pairs.map { it.item }
                rightItems = pairs.map { it.correctPosition.toString() }.shuffled()
                currentDragIndex = null
            }
            ActivityType.SentenceBuilder -> {
                val words = activity.sentenceParts ?: emptyList()
                placedWords = List(words.size) { null }
                availableWords = words
            }
            else -> {}
        }
    }

    fun handleContinue() {
        if (isLessonLocked) return

        when {
            currentActivity.type == ActivityType.Teaching -> {
                moveToNextActivity()
            }
            validateCurrentActivity() -> {
                showFeedback = true
                lastAnswerCorrect = true
                feedbackText = currentActivity.feedback ?: "¡Correcto!"
            }
            else -> {
                lives--
                errorCount++

                if (lives <= 0) {
                    isLessonLocked = true
                    showFeedback = false
                } else {
                    showFeedback = true
                    lastAnswerCorrect = false
                    feedbackText = "¡Incorrecto! Inténtalo de nuevo"
                    initializeCurrentActivity()
                }
            }
        }
    }


    private fun validateCurrentActivity(): Boolean {
        return when (currentActivity.type) {
            ActivityType.MultipleChoice, ActivityType.FillBlank ->
                selectedAnswer == currentActivity.correctAnswer
            ActivityType.Matching -> {
                val correctPairs = currentActivity.matchingPairs?.toSet() ?: emptySet()
                matchedPairs == correctPairs
            }
            ActivityType.DragPairs -> checkDragOrderCorrectness()
            ActivityType.SentenceBuilder -> checkSentenceCorrectness()
            else -> true
        }
    }

    private fun checkDragOrderCorrectness(): Boolean {
        val pairs = currentActivity.orderedPairs ?: return false

        val pairMap = pairs.associate { it.item to it.correctPosition.toString() }

        return leftItems.mapIndexed { index, leftItem ->
            val expectedRightValue = pairMap[leftItem]
            rightItems.getOrNull(index) == expectedRightValue
        }.all { it }
    }

    private fun checkSentenceCorrectness(): Boolean {
        val correctOrder = currentActivity.correctOrder ?: emptyList()
        return placedWords.none { it == null } && placedWords.filterNotNull() == correctOrder
    }

    fun moveToNextActivity() {
        showFeedback = false

        if (currentActivityIndex < activities.size - 1) {
            currentActivityIndex++
            initializeCurrentActivity()
        } else {
        }
    }

    private fun calculateRewards() {
        val errorPercentage = errorCount.toFloat() / (activities.size * 0.5f)
        val exp = (baseExp * (1 - errorPercentage.coerceAtMost(0.7f))).toInt()
        val dinero = (baseDinero * (1 - errorPercentage.coerceAtMost(0.7f))).toInt()
        onLessonComplete(exp, dinero)
    }

    fun restartLesson() {
        currentActivityIndex = 0
        lives = 5
        errorCount = 0
        isLessonLocked = false
        showFeedback = false
        initializeCurrentActivity()
    }
}
