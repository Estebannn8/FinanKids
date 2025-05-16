package com.universidad.finankids.ui.lesson

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.universidad.finankids.R
import com.universidad.finankids.data.model.ActivityType
import com.universidad.finankids.events.LessonEvent
import com.universidad.finankids.navigation.AppScreens
import com.universidad.finankids.state.LessonState
import com.universidad.finankids.ui.components.CustomButton
import com.universidad.finankids.ui.components.LoadingOverlay
import com.universidad.finankids.ui.components.lesson.AllLessonsCompletedScreen
import com.universidad.finankids.ui.components.lesson.ErrorScreen
import com.universidad.finankids.ui.components.lesson.FeedbackOverlay
import com.universidad.finankids.ui.components.lesson.LessonCompleteScreen
import com.universidad.finankids.ui.components.lesson.LessonLockedScreen
import com.universidad.finankids.ui.lesson.activities.DragPairsActivity
import com.universidad.finankids.ui.lesson.activities.FillBlankActivity
import com.universidad.finankids.ui.lesson.activities.MatchingActivity
import com.universidad.finankids.ui.lesson.activities.MultipleChoiceActivity
import com.universidad.finankids.ui.lesson.activities.SentenceBuilderActivity
import com.universidad.finankids.ui.lesson.activities.TeachingActivity
import com.universidad.finankids.viewmodel.LessonsViewModel
import com.universidad.finankids.viewmodel.LessonsViewModel.LoadingState
import com.universidad.finankids.viewmodel.UserViewModel

@Composable
fun LessonScreen(
    category: String,
    userViewModel: UserViewModel,
    lessonsViewModel: LessonsViewModel,
    navController: NavController
) {
    val userState by userViewModel.state.collectAsState()
    val lessonState by lessonsViewModel.state.collectAsState()
    val loadingState by lessonsViewModel.loadingState.collectAsState()

    // Efecto para cargar lecciones
    LaunchedEffect(category, userState.userData.leccionesCompletadas) {
        val completedLessons = userState.userData.leccionesCompletadas ?: emptyMap()
        lessonsViewModel.sendEvent(
            LessonEvent.LoadLessonAndInitialize(
                categoryId = category,
                completedLessons = completedLessons
            )
        )
    }

    LaunchedEffect(lessonsViewModel.state) {
        lessonsViewModel.state.collect { state ->
            if (state.currentLesson == null &&
                !state.isLoading &&
                lessonsViewModel.loadingState.value == LoadingState.Idle) {
                navController.popBackStack()
            }
        }
    }

    when {
        // 1. Estado de carga
        loadingState is LoadingState.LoadingLessons || lessonState.isLoading -> {
            LoadingOverlay()
        }

        // 2. Estado de error (usando when con asignación)
        loadingState.let { it is LoadingState.LessonsLoaded && !it.success } -> {
            ErrorScreen(
                message = "Error al cargar las lecciones",
                onRetry = {
                    userState.userData.leccionesCompletadas?.let { completed ->
                        lessonsViewModel.sendEvent(
                            LessonEvent.LoadLessonAndInitialize(category, completed)
                        )
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // 3. Lección cargada y disponible
        lessonState.currentLesson != null && lessonState.currentActivity != null -> {
            LessonContentScreen(
                lessonState = lessonState,
                onEvent = lessonsViewModel::sendEvent,
                navController = navController,
                userViewModel = userViewModel,
                category = category
            )
        }

        // 4. Todas las lecciones completadas (usando let para el smart cast)
        loadingState.let { it is LoadingState.LessonsLoaded } &&
                lessonState.currentLesson == null -> {
            // Verificar si todas las lecciones de esta categoría están completadas
            val categoryCompletedLessons = userState.userData.leccionesCompletadas?.get(category) as? Map<*, *>
            val allLessonsInCategory = lessonsViewModel.allLessons
            val allCompleted = allLessonsInCategory.isNotEmpty() &&
                    allLessonsInCategory.all { lesson ->
                        categoryCompletedLessons?.containsKey(lesson.id) == true
                    }

            if (allCompleted) {
                AllLessonsCompletedScreen(
                    onBack = { navController.popBackStack() }
                )
            } else {
                // Estado inesperado, recargar
                LaunchedEffect(Unit) {
                    lessonsViewModel.sendEvent(
                        LessonEvent.LoadLessonAndInitialize(
                            category,
                            userState.userData.leccionesCompletadas ?: emptyMap()
                        )
                    )
                }
            }
        }

        // 5. Estado inesperado (recargar)
        else -> {
            LoadingOverlay()
            LaunchedEffect(Unit) {
                userState.userData.leccionesCompletadas?.let { completed ->
                    lessonsViewModel.sendEvent(
                        LessonEvent.LoadLessonAndInitialize(category, completed)
                    )
                }
            }
        }
    }
}

@Composable
fun LessonContentScreen(
    lessonState: LessonState,
    onEvent: (LessonEvent) -> Unit,
    navController: NavController,
    userViewModel: UserViewModel,
    category: String
) {

    // Obtener el tema visual para la categoría actual
    val temaVisual = remember(category) {
        val tema = TemaVisualManager.obtenerTemaPorCategoria(category) ?: TemaVisualManager.obtenerTemaPorCategoria("Ahorro")!!
        Log.d("TemaVisual", "Categoría seleccionada: $category")
        Log.d("TemaVisual", "Tema aplicado: ${tema.baseColor}, Icono: ${tema.categoryIcon}")
        tema
    }

    LaunchedEffect(lessonState.currentLesson) {
        if (lessonState.currentLesson == null) {
            navController.popBackStack()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fondo de pantalla según el tipo de actividad
        val backgroundResource = remember(lessonState.currentActivity?.type) {
            val resource = when (lessonState.currentActivity?.type) {
                ActivityType.Teaching -> temaVisual.teachingBackground
                ActivityType.MultipleChoice -> temaVisual.multipleChoiceBackground
                ActivityType.FillBlank -> temaVisual.fillBlankBackground
                ActivityType.Matching -> temaVisual.matchingBackground
                ActivityType.DragPairs -> temaVisual.dragPairsBackground
                ActivityType.SentenceBuilder -> temaVisual.sentenceBuilderBackground
                else -> temaVisual.teachingBackground
            }
            Log.d("TemaVisual", "Tipo de actividad: ${lessonState.currentActivity?.type}, Fondo: $resource")
            resource
        }


        // Fondo de pantalla
        Image(
            painter = painterResource(id = backgroundResource),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize().alpha(0.7f)
        )

        when {
            lessonState.showCompleteScreen -> {
                LessonCompleteScreen(
                    exp = lessonState.earnedExp,
                    dinero = lessonState.earnedDinero,
                    onContinue = {
                        onEvent(LessonEvent.CompleteLesson)
                        // Usar los valores finales (con bonus si aplica) para actualizar el usuario
                        val finalExp = if (lessonState.perfectLesson) (lessonState.earnedExp * 1.2f).toInt() else lessonState.earnedExp
                        val finalDinero = if (lessonState.perfectLesson) (lessonState.earnedDinero * 1.2f).toInt() else lessonState.earnedDinero

                        userViewModel.markLessonAsCompleted(
                            lessonState.currentLesson?.id ?: "",
                            finalExp,
                            finalDinero
                        )
                        navController.popBackStack()
                        navController.navigate(AppScreens.HomeScreen.route)
                    },
                    temaVisual = temaVisual,
                    perfectLesson = lessonState.perfectLesson
                )
            }
            lessonState.isLessonLocked -> {
                LessonLockedScreen(
                    onRestart = { onEvent(LessonEvent.RestartLesson) },
                    onExit = { onEvent(LessonEvent.ExitLesson) },
                    temaVisual = temaVisual
                )
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent) // Cambiado a transparente
                ) {
                    LessonHeader(
                        progress = lessonState.progress,
                        lives = lessonState.lives,
                        onBackPressed = { onEvent(LessonEvent.ShowExitConfirmation) },
                        temaVisual = temaVisual
                    )

                    Box(modifier = Modifier.weight(1f)) {
                        lessonState.currentActivity?.let { activity ->
                            when (activity.type) {
                                ActivityType.Teaching -> {
                                    TeachingActivity(
                                        state = lessonState,
                                        onEvent = onEvent,
                                        temaVisual = temaVisual
                                    )
                                }
                                ActivityType.MultipleChoice -> {
                                    MultipleChoiceActivity(
                                        state = lessonState,
                                        onEvent = onEvent,
                                        temaVisual = temaVisual
                                    )
                                }
                                ActivityType.FillBlank -> {
                                    FillBlankActivity(
                                        state = lessonState,
                                        onEvent = onEvent,
                                        temaVisual = temaVisual
                                    )
                                }
                                ActivityType.Matching -> {
                                    MatchingActivity(
                                        state = lessonState,
                                        onEvent = onEvent,
                                        temaVisual = temaVisual
                                    )
                                }
                                ActivityType.DragPairs -> {
                                    DragPairsActivity(
                                        state = lessonState,
                                        onEvent = onEvent,
                                        temaVisual = temaVisual
                                    )
                                }
                                ActivityType.SentenceBuilder -> {
                                    SentenceBuilderActivity(
                                        state = lessonState,
                                        onEvent = onEvent,
                                        temaVisual = temaVisual
                                    )
                                }
                            }
                        }
                    }

                    if (lessonState.showFeedback) {
                        FeedbackOverlay(
                            isCorrect = lessonState.lastAnswerCorrect ?: false,
                            onDismiss = {
                                onEvent(LessonEvent.HideFeedback)

                                if (lessonState.lastAnswerCorrect == true) {
                                    if (lessonState.isLastActivityInLesson) {
                                        onEvent(LessonEvent.ShowCompleteScreen)
                                    } else {
                                        onEvent(LessonEvent.MoveToNextActivity)
                                    }
                                } else {
                                    onEvent(LessonEvent.ResetCurrentActivity)
                                }
                            },
                            temaVisual = temaVisual
                        )
                    }

                    if (lessonState.showExitConfirmation) {
                        AlertDialog(
                            onDismissRequest = { onEvent(LessonEvent.HideExitConfirmation) },
                            title = { Text("¿Seguro que quieres salir?") },
                            text = { Text("Si sales ahora perderás el progreso de esta lección.") },
                            confirmButton = {
                                TextButton(onClick = {
                                    onEvent(LessonEvent.HideExitConfirmation)
                                    onEvent(LessonEvent.ExitLesson)
                                }) {
                                    Text("Salir")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    onEvent(LessonEvent.HideExitConfirmation)
                                }) {
                                    Text("Continuar")
                                }
                            }
                        )
                    }

                    BottomSection(
                        onContinue = { onEvent(LessonEvent.ContinueActivity) },
                        temaVisual = temaVisual,
                        enabled = when (lessonState.currentActivity?.type) {
                            ActivityType.MultipleChoice, ActivityType.FillBlank -> lessonState.selectedAnswer != null
                            ActivityType.Matching -> {
                                val totalPairs = lessonState.currentActivity?.matchingPairs?.size ?: 0
                                lessonState.matchedPairs.size == totalPairs
                            }
                            else -> true
                        }
                    )

                }
            }
        }
    }
}

@Composable
fun LessonHeader(
    progress: Float,
    lives: Int,
    onBackPressed: () -> Unit,
    temaVisual: TemaVisual
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(80.dp)
    ) {

        val playButtonInteractionSource = remember { MutableInteractionSource() }

        // Boton Salir
        Box(
            modifier = Modifier
                .size(40.dp)
                .padding(bottom = 10.dp, start = 10.dp)
                .align(Alignment.CenterStart)
                .clickable(
                    onClick = { onBackPressed() },
                    interactionSource = playButtonInteractionSource,
                    indication = null
                )
        ) {
            Image(
                painter = painterResource(temaVisual.CloseIcon),
                contentDescription = "Flecha atras",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        // Barra de progreso
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(32.dp)
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(temaVisual.progressBar),
                contentDescription = "Barra de progreso",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            // Progreso actual
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(17.dp)
                    .padding(end = 12.2.dp)
                    .zIndex(3f)
                    .offset(y = 3.73.dp, x = 5.8.dp)
                    .clip(RoundedCornerShape(
                        topEnd = 8.dp,
                        bottomEnd = 8.dp
                    ))
                    .background(temaVisual.progressColor)
            )
        }

        // Vidas
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(y = (-3).dp)
        ) {
            val heartIconRes = when (lives) {
                5 -> R.drawable.ic_full_heart
                4 -> R.drawable.ic_heart_4l
                3 -> R.drawable.ic_heart_3l
                2 -> R.drawable.ic_heart_2l
                else -> R.drawable.ic_heart_1l
            }

            Image(
                painter = painterResource(id = heartIconRes),
                contentDescription = "Vidas",
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = "x$lives",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}


@Composable
fun BottomSection(
    onContinue: () -> Unit,
    temaVisual: TemaVisual,
    enabled: Boolean = true
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(80.dp)
    ){
        CustomButton(
            modifier = Modifier.align(Alignment.Center),
            buttonText = "CONTINUAR",
            gradientLight = temaVisual.gradientLight,
            gradientDark = temaVisual.gradientDark,
            baseColor = temaVisual.baseColor,
            onClick = onContinue,
            enabled = enabled
        )
    }
}
