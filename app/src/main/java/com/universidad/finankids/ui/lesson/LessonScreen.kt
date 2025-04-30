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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.universidad.finankids.R
import com.universidad.finankids.data.model.ActivityContent
import com.universidad.finankids.data.model.ActivityType
import com.universidad.finankids.data.model.OrderedPair
import com.universidad.finankids.ui.Components.CustomButton
import com.universidad.finankids.ui.lesson.activities.DragPairsActivity
import com.universidad.finankids.ui.lesson.activities.FillBlankActivity
import com.universidad.finankids.ui.lesson.activities.MatchingActivity
import com.universidad.finankids.ui.lesson.activities.MultipleChoiceActivity
import com.universidad.finankids.ui.lesson.activities.SentenceBuilderActivity
import com.universidad.finankids.ui.lesson.activities.TeachingActivity

@Composable
fun LessonScreen(
    activities: List<ActivityContent>,
    onExitLesson: () -> Unit,
    onLessonComplete: () -> Unit
) {
    var showCompleteScreen by remember { mutableStateOf(false) }
    var earnedExp by remember { mutableStateOf(0) }
    var earnedDinero by remember { mutableStateOf(0) }

    val lessonManager = rememberLessonManager(
        activities = activities,
        onExitLesson = onExitLesson,
        onLessonComplete = { exp, dinero ->
            earnedExp = exp
            earnedDinero = dinero
            showCompleteScreen = true
        }
    )

    when {
        showCompleteScreen -> {
            LessonCompleteScreen(
                exp = earnedExp,
                dinero = earnedDinero,
                onContinue = onLessonComplete
            )
        }
        lessonManager.isLessonLocked -> {
            LessonLockedScreen(
                onRestart = { lessonManager.restartLesson() },
                onExit = onExitLesson
            )
        }
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                LessonHeader(
                    progress = lessonManager.progress,
                    lives = lessonManager.lives,
                    onBackPressed = onExitLesson
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    when (lessonManager.currentActivity.type) {
                        ActivityType.Teaching -> {
                            TeachingActivity(content = lessonManager.currentActivity)
                        }
                        ActivityType.MultipleChoice -> {
                            MultipleChoiceActivity(
                                content = lessonManager.currentActivity,
                                selectedAnswer = lessonManager.selectedAnswer,
                                onOptionSelected = { lessonManager.selectedAnswer = it }
                            )
                        }
                        ActivityType.FillBlank -> {
                            FillBlankActivity(
                                content = lessonManager.currentActivity,
                                selectedAnswer = lessonManager.selectedAnswer,
                                onOptionSelected = { lessonManager.selectedAnswer = it }
                            )
                        }
                        ActivityType.Matching -> {
                            MatchingActivity(
                                content = lessonManager.currentActivity,
                                matchedPairs = lessonManager.matchedPairs,
                                onPairMatched = { lessonManager.matchedPairs = lessonManager.matchedPairs + it },
                                selectedLeft = lessonManager.selectedLeft,
                                selectedRight = lessonManager.selectedRight,
                                onSelectLeft = { lessonManager.selectedLeft = it },
                                onSelectRight = { lessonManager.selectedRight = it }
                            )
                        }
                        ActivityType.DragPairs -> {
                            DragPairsActivity(
                                content = lessonManager.currentActivity,
                                rightItems = lessonManager.rightItems,
                                currentDragIndex = lessonManager.currentDragIndex,
                                onDragIndexChange = { lessonManager.currentDragIndex = it },
                                onSwap = { lessonManager.rightItems = it }
                            )
                        }
                        ActivityType.SentenceBuilder -> {
                            SentenceBuilderActivity(
                                content = lessonManager.currentActivity,
                                placedWords = lessonManager.placedWords,
                                availableWords = lessonManager.availableWords,
                                onWordPlaced = {
                                    val index = lessonManager.placedWords.indexOfFirst { it == null }
                                    if (index != -1) {
                                        lessonManager.placedWords = lessonManager.placedWords.toMutableList().apply { set(index, it) }
                                        lessonManager.availableWords = lessonManager.availableWords - it
                                    }
                                },
                                onWordRemoved = { index, word ->
                                    lessonManager.placedWords = lessonManager.placedWords.toMutableList().apply { set(index, null) }
                                    lessonManager.availableWords = lessonManager.availableWords + word
                                }
                            )
                        }
                    }
                }

                if (lessonManager.showFeedback) {
                    FeedbackOverlay(
                        isCorrect = lessonManager.lastAnswerCorrect ?: false,
                        feedbackText = lessonManager.feedbackText,
                        onDismiss = {
                            if (lessonManager.lastAnswerCorrect == true) {
                                lessonManager.moveToNextActivity()
                            }
                        }
                    )
                }

                BottomSection(
                    onContinue = { lessonManager.handleContinue() }
                )
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
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .background(
                    color = if (isCorrect) Color(0xFFDCEDC8) else Color(0xFFFFCDD2),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp),
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
        // Flecha de retroceso
        Box(
            modifier = Modifier
                .size(40.dp)
                .padding(bottom = 10.dp, start = 10.dp)
                .align(Alignment.CenterStart)
                .clickable(onClick = onBackPressed)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_atras_recovery),
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
                    .background(Color.Magenta)
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

@Preview(showBackground = true)
@Composable
fun PreviewLessonScreen() {
    val sampleActivities = listOf(
        ActivityContent(
            type = ActivityType.Teaching,
            title = "Introducción al ahorro",
            explanation = "El ahorro es guardar parte de tu dinero para usarlo en el futuro..."
        ),
        ActivityContent(
            type = ActivityType.MultipleChoice,
            title = "Selecciona la opción correcta:",
            question = "¿Qué es el ahorro?",
            options = listOf("Gastar todo el dinero", "Guardar dinero para el futuro", "Pedir prestado"),
            correctAnswer = "Guardar dinero para el futuro",
            feedback = "¡Correcto! El ahorro es guardar dinero para usarlo después."
        ),
        ActivityContent(
            type = ActivityType.SentenceBuilder,
            title = "Construye la oración",
            question = "Ordena las palabras para formar la oración correcta.",
            sentenceParts = listOf("dinero", "en", "guarda", "banco", "El"),
            correctOrder = listOf("El", "banco", "guarda", "dinero", "en"),
            feedback = "¡Correcto! La oración es: 'El banco guarda dinero en'."
        )
    )

    LessonScreen(
        activities = sampleActivities,
        onExitLesson = {},
        onLessonComplete = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LessonLockedScreenPreview(){
    LessonLockedScreen(onRestart = {}, onExit = {})
}

@Preview(showBackground = true)
@Composable
fun LessonCompleteScreenPreview(){
    LessonCompleteScreen(exp = 100, dinero = 500, onContinue = {})
}