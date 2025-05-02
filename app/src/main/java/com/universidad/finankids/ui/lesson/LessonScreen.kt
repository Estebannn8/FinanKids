package com.universidad.finankids.ui.lesson

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.universidad.finankids.R
import com.universidad.finankids.data.model.ActivityType
import com.universidad.finankids.events.LessonEvent
import com.universidad.finankids.navigation.AppScreens
import com.universidad.finankids.state.LessonState
import com.universidad.finankids.ui.Components.CustomButton
import com.universidad.finankids.ui.Components.LoadingOverlay
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
        if (userState.userData.leccionesCompletadas != null) {
            lessonsViewModel.sendEvent(
                LessonEvent.LoadLessonAndInitialize(
                    categoryId = category,
                    completedLessons = userState.userData.leccionesCompletadas
                )
            )
        }
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
                userViewModel = userViewModel
            )
        }

        // 4. Todas las lecciones completadas (usando let para el smart cast)
        loadingState.let { it is LoadingState.LessonsLoaded } &&
                lessonState.currentLesson == null &&
                userState.userData.leccionesCompletadas?.isNotEmpty() == true -> {
            AllLessonsCompletedScreen(
                onBack = { navController.popBackStack() }
            )
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
private fun ErrorScreen(message: String, onRetry: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = Color.Red,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column (
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomButton(
                buttonText = "Reintentar",
                gradientLight = Color(0xFF9C749A),
                gradientDark = Color(0xFF431441),
                baseColor = Color(0xFF53164F),
                onClick = onRetry
            )
            CustomButton(
                buttonText = "Volver",
                gradientLight = Color(0xFF9C749A),
                gradientDark = Color(0xFF431441),
                baseColor = Color(0xFF53164F),
                onClick = onBack
            )
        }
    }
}

@Composable
private fun AllLessonsCompletedScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡Felicidades!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50)
        )

        Text(
            text = "Has completado todas las lecciones de esta categoría",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(16.dp)
        )

        CustomButton(
            buttonText = "VOLVER",
            gradientLight = Color(0xFF9C749A),
            gradientDark = Color(0xFF431441),
            baseColor = Color(0xFF53164F),
            onClick = onBack
        )
    }
}

@Composable
fun LessonContentScreen(
    lessonState: LessonState,
    onEvent: (LessonEvent) -> Unit,
    navController: NavController,
    userViewModel: UserViewModel
) {
    LaunchedEffect(lessonState.currentLesson) {
        if (lessonState.currentLesson == null) {
            navController.popBackStack()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fondo de pantalla
        Image(
            painter = painterResource(id = R.drawable.matching_background_ahorro),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        when {
            lessonState.showCompleteScreen -> {
                LessonCompleteScreen(
                    exp = lessonState.earnedExp,
                    dinero = lessonState.earnedDinero,
                    onContinue = {
                        onEvent(LessonEvent.CompleteLesson)
                        userViewModel.markLessonAsCompleted(
                            lessonState.currentLesson?.id ?: "",
                            lessonState.earnedExp,
                            lessonState.earnedDinero
                        )
                        navController.popBackStack()
                        navController.navigate(AppScreens.HomeScreen.route)
                    }
                )
            }
            lessonState.isLessonLocked -> {
                LessonLockedScreen(
                    onRestart = { onEvent(LessonEvent.RestartLesson) },
                    onExit = { onEvent(LessonEvent.ExitLesson) }
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
                        onBackPressed = { onEvent(LessonEvent.ShowExitConfirmation) }
                    )

                    Box(modifier = Modifier.weight(1f)) {
                        lessonState.currentActivity?.let { activity ->
                            when (activity.type) {
                                ActivityType.Teaching -> {
                                    TeachingActivity(
                                        state = lessonState,
                                        onEvent = onEvent
                                    )
                                }
                                ActivityType.MultipleChoice -> {
                                    MultipleChoiceActivity(
                                        state = lessonState,
                                        onEvent = onEvent
                                    )
                                }
                                ActivityType.FillBlank -> {
                                    FillBlankActivity(
                                        state = lessonState,
                                        onEvent = onEvent
                                    )
                                }
                                ActivityType.Matching -> {
                                    MatchingActivity(
                                        state = lessonState,
                                        onEvent = onEvent
                                    )
                                }
                                ActivityType.DragPairs -> {
                                    DragPairsActivity(
                                        state = lessonState,
                                        onEvent = onEvent
                                    )
                                }
                                ActivityType.SentenceBuilder -> {
                                    SentenceBuilderActivity(
                                        state = lessonState,
                                        onEvent = onEvent
                                    )
                                }
                            }
                        }
                    }

                    if (lessonState.showFeedback) {
                        FeedbackOverlay(
                            isCorrect = lessonState.lastAnswerCorrect ?: false,
                            feedbackText = lessonState.feedbackText,
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
                            }
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
                        onContinue = { onEvent(LessonEvent.ContinueActivity) }
                    )
                }
            }
        }
    }
}

@Composable
fun FeedbackOverlay(
    isCorrect: Boolean,
    feedbackText: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .background(
                    color = if (isCorrect) Color(0xFFDCEDC8) else Color(0xFFFFCDD2),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp)
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = feedbackText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun LessonLockedScreen(
    onRestart: () -> Unit,
    onExit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_pesito_ahorrador_triste),
            contentDescription = "Pesito triste",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "¡Has perdido todas tus vidas!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE53935),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "No te rindas, ¡inténtalo de nuevo!",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Botones apilados verticalmente
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                buttonText = "REINTENTAR LECCIÓN",
                gradientLight = Color(0xFF9C749A),
                gradientDark = Color(0xFF431441),
                baseColor = Color(0xFF53164F),
                onClick = onRestart
            )

            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                buttonText = "SALIR",
                gradientLight = Color(0xFFCCCCCC),
                gradientDark = Color(0xFF999999),
                baseColor = Color(0xFFAAAAAA),
                onClick = onExit
            )
        }
    }
}

@Composable
fun LessonCompleteScreen(
    exp: Int,
    dinero: Int,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_pesito__ahorrador_feliz),
            contentDescription = "Pesito feliz",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "¡Lección Completada!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Recompensas
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Recompensas obtenidas:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_experience),
                    contentDescription = "Experiencia",
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "$exp EXP",
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.width(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.ic_coin),
                    contentDescription = "Dinero",
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "$$dinero",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            buttonText = "CONTINUAR",
            gradientLight = Color(0xFF9C749A),
            gradientDark = Color(0xFF431441),
            baseColor = Color(0xFF53164F),
            onClick = onContinue
        )
    }
}

@Composable
fun LessonHeader(
    progress: Float,
    lives: Int,
    onBackPressed: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(80.dp)
    ) {
        // Boton Salir
        Box(
            modifier = Modifier
                .size(40.dp)
                .padding(bottom = 10.dp, start = 10.dp)
                .align(Alignment.CenterStart)
                .clickable(onClick = { onBackPressed() })
        ) {
            Image(
                painter = painterResource(R.drawable.ic_close_ahorro),
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
                painter = painterResource(R.drawable.ic_exp_bar_morado),
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
                    .background(Color(0xDC53164F))
            )
        }

        // Vidas (simplificado a icono + texto)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(y = (-3).dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_full_heart),
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
    onContinue: () -> Unit
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
            gradientLight = Color(0xFF9C749A),
            gradientDark = Color(0xFF431441),
            baseColor = Color(0xFF53164F),
            onClick = onContinue
        )
    }
}
