package com.universidad.finankids.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.universidad.finankids.data.model.ActivityType
import com.universidad.finankids.data.model.Lesson
import com.universidad.finankids.data.model.MatchingPair
import com.universidad.finankids.events.LessonEvent
import com.universidad.finankids.state.LessonState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LessonsViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val _state = MutableStateFlow(LessonState())
    val state: StateFlow<LessonState> = _state.asStateFlow()

    private val _events = Channel<LessonEvent>(Channel.UNLIMITED)
    val events = _events.receiveAsFlow()

    var allLessons: List<Lesson> = emptyList()

    // Estado para controlar la carga
    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    sealed class LoadingState {
        object Idle : LoadingState()
        object LoadingLessons : LoadingState()
        data class LessonsLoaded(val success: Boolean) : LoadingState()
    }

    init {
        viewModelScope.launch {
            events.collect { event ->
                handleEvent(event)
            }
        }
    }

    fun sendEvent(event: LessonEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }

    private fun handleEvent(event: LessonEvent) {
        when (event) {
            is LessonEvent.LoadLessonAndInitialize -> {
                viewModelScope.launch {
                    loadLessonsAndInitialize(event.categoryId, event.completedLessons)
                }
            }
            LessonEvent.ContinueActivity -> handleContinue()
            LessonEvent.MoveToNextActivity -> moveToNextActivity()
            LessonEvent.RestartLesson -> restartLesson()
            LessonEvent.ExitLesson -> exitLesson()
            is LessonEvent.SelectAnswer -> selectAnswer(event.answer)
            is LessonEvent.MatchPair -> addMatchedPair(event.pair)
            is LessonEvent.SelectLeftItem -> {
                _state.update { it.copy(selectedLeft = event.item) }
            }
            is LessonEvent.SelectRightItem -> {
                _state.update { it.copy(selectedRight = event.item) }
            }
            is LessonEvent.UpdateRightItems -> updateRightItems(event.items)
            is LessonEvent.PlaceWord -> placeWord(event.word, event.position)
            is LessonEvent.RemoveWord -> removeWord(event.position)
            LessonEvent.ShowExitConfirmation -> showExitConfirmation()
            LessonEvent.HideExitConfirmation -> hideExitConfirmation()
            LessonEvent.CompleteLesson -> completeLesson()
            LessonEvent.ClearError -> clearError()
            LessonEvent.ResetCurrentActivity -> resetCurrentActivity()
            LessonEvent.HideFeedback -> {
                _state.update { it.copy(showFeedback = false) }
            }
            LessonEvent.ShowCompleteScreen -> {
                calculateRewards()
                _state.update { it.copy(showCompleteScreen = true) }
            }
        }
    }

    private suspend fun loadLessonsAndInitialize(categoryId: String, completedLessons: Map<String, Any>) {
        val TAG = "LessonViewModel"

        Log.d(TAG, "Iniciando carga de lecciones para la categoría: $categoryId")
        _loadingState.value = LoadingState.LoadingLessons
        _state.update { it.copy(isLoading = true, error = null) }

        try {
            Log.d(TAG, "Consultando lecciones en Firestore...")
            val snapshot = firestore.collection("categorias/$categoryId/lecciones")
                .orderBy("order")
                .get()
                .await()

            allLessons = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Lesson::class.java)?.copy(id = doc.id)
            }

            Log.d(TAG, "Lecciones obtenidas: ${allLessons.size}")

            if (allLessons.isEmpty()) {
                Log.w(TAG, "No hay lecciones disponibles para la categoría: $categoryId")
                _loadingState.value = LoadingState.LessonsLoaded(false)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "No hay lecciones disponibles en esta categoría"
                    )
                }
                return
            }

            val nextLesson = allLessons
                .sortedBy { it.order }
                .firstOrNull { lesson ->
                    val categoryLessons = completedLessons[categoryId] as? Map<String, Any> ?: emptyMap()
                    !categoryLessons.containsKey(lesson.id)
                }

            if (nextLesson == null) {
                Log.d(TAG, "Todas las lecciones están completadas para la categoría: $categoryId")
            } else {
                Log.d(TAG, "Siguiente lección a mostrar: ${nextLesson.title} (ID: ${nextLesson.id})")
            }

            _state.update {
                it.copy(
                    currentLesson = nextLesson,
                    isLoading = false,
                    currentActivity = nextLesson?.activities?.firstOrNull(),
                    currentActivityIndex = 0,
                    progress = 0f,
                    lives = nextLesson?.lives ?: 0,
                    errorCount = 0
                )
            }

            _loadingState.value = LoadingState.LessonsLoaded(true)
            nextLesson?.let {
                Log.d(TAG, "Inicializando actividad actual de la lección: ${it.title}")
                initializeCurrentActivity()
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error cargando las lecciones: ${e.message}", e)
            _loadingState.value = LoadingState.LessonsLoaded(false)
            _state.update {
                it.copy(
                    isLoading = false,
                    error = "Error loading lessons: ${e.message}"
                )
            }
        }
    }

    private fun initializeCurrentActivity() {
        val currentActivity = _state.value.currentActivity ?: return

        _state.update { state ->
            when (currentActivity.type) {
                ActivityType.FillBlank, ActivityType.MultipleChoice ->
                    state.copy(selectedAnswer = null)
                ActivityType.Matching ->
                    state.copy(
                        matchedPairs = emptySet(),
                        selectedLeft = null,
                        selectedRight = null
                    )
                ActivityType.DragPairs -> {
                    val pairs = currentActivity.orderedPairs ?: emptyList()
                    state.copy(
                        leftItems = pairs.map { it.item },
                        rightItems = pairs.map { it.correctPosition.toString() }.shuffled()
                    )
                }
                ActivityType.SentenceBuilder -> {
                    val words = currentActivity.sentenceParts ?: emptyList()
                    state.copy(
                        placedWords = List(words.size) { null },
                        availableWords = words
                    )
                }
                else -> state
            }
        }
    }

    private fun handleContinue() {
        if (_state.value.isLessonLocked) return

        val currentActivity = _state.value.currentActivity ?: return
        val isLastActivity = _state.value.currentActivityIndex == (_state.value.currentLesson?.activities?.size?.minus(1) ?: 0)

        when (currentActivity.type) {
            ActivityType.Teaching -> {
                if (isLastActivity) {
                    calculateRewards()
                    _state.update {
                        it.copy(
                            showCompleteScreen = true,
                            isLastActivityInLesson = true
                        )
                    }
                } else {
                    moveToNextActivity()
                }
            }
            else -> {
                if (validateCurrentActivity()) {
                    _state.update {
                        it.copy(
                            showFeedback = true,
                            lastAnswerCorrect = true,
                            feedbackText = currentActivity.feedback ?: "¡Correcto!",
                            isLastActivityInLesson = isLastActivity
                        )
                    }
                } else {
                    val newLives = _state.value.lives - 1
                    val newErrorCount = _state.value.errorCount + 1

                    if (newLives <= 0) {
                        _state.update {
                            it.copy(
                                isLessonLocked = true,
                                showFeedback = false,
                                lives = newLives,
                                errorCount = newErrorCount
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                showFeedback = true,
                                lastAnswerCorrect = false,
                                feedbackText = "¡Incorrecto! Inténtalo de nuevo",
                                lives = newLives,
                                errorCount = newErrorCount
                            )
                        }
                    }
                }
            }
        }
    }

    private fun validateCurrentActivity(): Boolean {
        val state = _state.value
        val currentActivity = state.currentActivity ?: return false

        return when (currentActivity.type) {
            ActivityType.MultipleChoice, ActivityType.FillBlank ->
                state.selectedAnswer == currentActivity.correctAnswer
            ActivityType.Matching -> {
                val correctPairs = currentActivity.matchingPairs?.toSet() ?: emptySet()
                state.matchedPairs == correctPairs
            }
            ActivityType.DragPairs -> checkDragOrderCorrectness()
            ActivityType.SentenceBuilder -> checkSentenceCorrectness()
            else -> true
        }
    }

    private fun checkDragOrderCorrectness(): Boolean {
        val state = _state.value
        val pairs = state.currentActivity?.orderedPairs ?: return false

        val pairMap = pairs.associate { it.item to it.correctPosition.toString() }

        return state.leftItems.mapIndexed { index, leftItem ->
            val expectedRightValue = pairMap[leftItem]
            state.rightItems.getOrNull(index) == expectedRightValue
        }.all { it }
    }

    private fun checkSentenceCorrectness(): Boolean {
        val state = _state.value
        val correctOrder = state.currentActivity?.correctOrder ?: emptyList()
        return state.placedWords.none { it == null } &&
                state.placedWords.filterNotNull() == correctOrder
    }

    private fun moveToNextActivity() {
        val currentIndex = _state.value.currentActivityIndex
        val activities = _state.value.currentLesson?.activities ?: emptyList()

        if (currentIndex < activities.size - 1) {
            _state.update {
                it.copy(
                    currentActivityIndex = currentIndex + 1,
                    currentActivity = activities[currentIndex + 1],
                    progress = (currentIndex + 1).toFloat() / activities.size,
                    showFeedback = false,
                    selectedAnswer = null,
                    matchedPairs = emptySet(),
                    selectedLeft = null,
                    selectedRight = null,
                    placedWords = emptyList(),
                    availableWords = emptyList()
                )
            }
            initializeCurrentActivity()
        } else {
            calculateRewards()
            _state.update {
                it.copy(
                    showCompleteScreen = true
                )
            }
        }
    }

    private fun resetCurrentActivity() {
        _state.update {
            it.copy(
                showFeedback = false,
                selectedAnswer = null,
                matchedPairs = emptySet(),
                selectedLeft = null,
                selectedRight = null,
                placedWords = emptyList(),
                availableWords = emptyList()
            )
        }
        initializeCurrentActivity()
    }

    private fun calculateRewards() {
        val state = _state.value
        val baseExp = state.currentLesson?.baseExp ?: 100
        val baseDinero = state.currentLesson?.baseDinero ?: 500
        val activitiesSize = state.currentLesson?.activities?.size ?: 1

        // Máximo de errores permitidos para calcular proporción (50% del total de actividades)
        val maxAllowedErrors = (activitiesSize * 0.5f).toInt().coerceAtLeast(1)

        // Factor de penalización (0 a 0.7) basado en errores
        val penaltyFactor = (state.errorCount.toFloat() / maxAllowedErrors).coerceIn(0f, 0.7f)

        // Factor de recompensa (0.3 a 1.0)
        val rewardFactor = 1 - penaltyFactor

        // Calcular recompensas asegurando valores enteros
        val exp = (baseExp * rewardFactor).toInt().coerceAtLeast((baseExp * 0.3f).toInt())
        val dinero = (baseDinero * rewardFactor).toInt().coerceAtLeast((baseDinero * 0.3f).toInt())

        _state.update {
            it.copy(
                earnedExp = exp,
                earnedDinero = dinero,
                showCompleteScreen = true
            )
        }
    }

    private fun restartLesson() {
        _state.value.currentLesson?.let { lesson ->
            _state.update {
                it.copy(
                    currentActivityIndex = 0,
                    currentActivity = lesson.activities.firstOrNull(),
                    lives = lesson.lives,
                    errorCount = 0,
                    isLessonLocked = false,
                    showFeedback = false,
                    progress = 0f
                )
            }
            initializeCurrentActivity()
        }
    }

    private fun exitLesson() {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Idle
            _state.emit(LessonState())
        }
    }

    private fun selectAnswer(answer: String) {
        _state.update { it.copy(selectedAnswer = answer) }
    }

    private fun addMatchedPair(pair: MatchingPair) {
        _state.update { it.copy(matchedPairs = it.matchedPairs + pair) }
    }

    private fun updateRightItems(items: List<String>) {
        _state.update { it.copy(rightItems = items) }
    }

    private fun placeWord(word: String, position: Int) {
        _state.update { state ->
            val newPlacedWords = state.placedWords.toMutableList().apply {
                set(position, word)
            }
            val newAvailableWords = state.availableWords - word
            state.copy(
                placedWords = newPlacedWords,
                availableWords = newAvailableWords
            )
        }
    }

    private fun removeWord(position: Int) {
        _state.update { state ->
            val word = state.placedWords[position]
            val newPlacedWords = state.placedWords.toMutableList().apply {
                set(position, null)
            }
            val newAvailableWords = if (word != null) state.availableWords + word else state.availableWords
            state.copy(
                placedWords = newPlacedWords,
                availableWords = newAvailableWords
            )
        }
    }

    private fun showExitConfirmation() {
        _state.update { it.copy(showExitConfirmation = true) }
    }

    private fun hideExitConfirmation() {
        _state.update { it.copy(showExitConfirmation = false) }
    }

    private fun completeLesson() {
        _state.update { state ->
            state.copy(
                showCompleteScreen = false,
                currentLesson = null,
                currentActivity = null
            )
        }
        _loadingState.value = LoadingState.Idle
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}