package com.universidad.finankids.ui.lesson

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.universidad.finankids.data.model.ActivityContent
import com.universidad.finankids.data.model.ActivityType
import com.universidad.finankids.data.model.MatchingPair

class LessonManager(
    private val activities: List<ActivityContent>,
    private val onExitLesson: () -> Unit,
    private val onLessonComplete: (Int, Int) -> Unit // (exp, dinero)
) {
    init {
        initializeCurrentActivity()
    }

    // Estados de progreso
    private var currentActivityIndex by mutableIntStateOf(0)
    var lives by mutableIntStateOf(5)
    private var errorCount by mutableIntStateOf(0)
    var isLessonLocked by mutableStateOf(false)

    // Recompensas
    private val baseExp = 100
    private val baseDinero = 500

    // Estados de actividad actual
    val currentActivity: ActivityContent get() = activities[currentActivityIndex]
    val progress: Float get() = if (activities.isEmpty()) 0f else currentActivityIndex.toFloat() / activities.size

    // Feedback
    var showFeedback by mutableStateOf(false)
    var lastAnswerCorrect by mutableStateOf<Boolean?>(null)
    var feedbackText by mutableStateOf("")

    // Estados específicos por tipo de actividad
    var selectedAnswer by mutableStateOf<String?>(null)
    var matchedPairs by mutableStateOf<Set<MatchingPair>>(emptySet())
    var selectedLeft by mutableStateOf<String?>(null)
    var selectedRight by mutableStateOf<String?>(null)
    var rightItems by mutableStateOf<List<String>>(emptyList())
    var currentDragIndex by mutableStateOf<Int?>(null)
    var placedWords by mutableStateOf<List<String?>>(emptyList())
    var availableWords by mutableStateOf<List<String>>(emptyList())

    fun initializeCurrentActivity() {
        when (currentActivity.type) {
            ActivityType.FillBlank, ActivityType.MultipleChoice -> selectedAnswer = null
            ActivityType.Matching -> {
                matchedPairs = emptySet()
                selectedLeft = null
                selectedRight = null
            }
            ActivityType.DragPairs -> {
                val pairs = currentActivity.orderedPairs ?: emptyList()
                rightItems = pairs.shuffled().mapIndexed { index, pair ->
                    if (currentActivity.showNumbers) (index + 1).toString() else pair.item
                }
                currentDragIndex = null
            }
            ActivityType.SentenceBuilder -> {
                val words = currentActivity.sentenceParts ?: emptyList()
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
                // Respuesta correcta
                showFeedback = true
                lastAnswerCorrect = true
                feedbackText = currentActivity.feedback ?: "¡Correcto!"
                // NO llamamos moveToNextActivity aquí, se hará en onDismiss del Feedback
            }
            else -> {
                // Respuesta incorrecta
                lives--
                errorCount++
                showFeedback = true
                lastAnswerCorrect = false
                feedbackText = currentActivity.feedback ?: "Inténtalo de nuevo"

                if (lives <= 0) {
                    isLessonLocked = true
                } else {
                    initializeCurrentActivity() // Reiniciamos la actividad actual
                }
            }
        }
    }

    private fun validateCurrentActivity(): Boolean {
        return when (currentActivity.type) {
            ActivityType.MultipleChoice, ActivityType.FillBlank ->
                selectedAnswer == currentActivity.correctAnswer
            ActivityType.Matching ->
                matchedPairs.size == (currentActivity.matchingPairs?.size ?: 0)
            ActivityType.DragPairs -> checkDragOrderCorrectness()
            ActivityType.SentenceBuilder -> checkSentenceCorrectness()
            else -> true
        }
    }

    private fun checkDragOrderCorrectness(): Boolean {
        val pairs = currentActivity.orderedPairs ?: emptyList()
        return if (currentActivity.showNumbers) {
            rightItems.mapNotNull { it.toIntOrNull() } == (1..rightItems.size).toList()
        } else {
            rightItems == pairs.sortedBy { it.correctPosition }.map { it.item }
        }
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
            calculateRewards()
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
        initializeCurrentActivity()
    }
}

@Composable
fun rememberLessonManager(
    activities: List<ActivityContent>,
    onExitLesson: () -> Unit,
    onLessonComplete: (Int, Int) -> Unit
): LessonManager {
    return remember {
        LessonManager(activities, onExitLesson, onLessonComplete)
    }
}