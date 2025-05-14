package com.universidad.finankids.ui.components.lesson

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.universidad.finankids.R
import com.universidad.finankids.ui.components.CustomButton
import com.universidad.finankids.ui.lesson.TemaVisual
import kotlinx.coroutines.delay

@Composable
fun LessonCompleteScreen(
    exp: Int,
    dinero: Int,
    onContinue: () -> Unit,
    temaVisual: TemaVisual,
    perfectLesson: Boolean = false
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.celebration))

    var showTitle by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }
    var showBaseRewards by remember { mutableStateOf(false) }
    var showBonusText by remember { mutableStateOf(false) }
    var showBonusValues by remember { mutableStateOf(false) }
    var showFinalRewards by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }
    var showConfetti by remember { mutableStateOf(false) }

    var displayedExpTarget by remember { mutableStateOf(0) }
    var displayedDineroTarget by remember { mutableStateOf(0) }

    val animatedExp by animateIntAsState(
        targetValue = displayedExpTarget,
        animationSpec = tween(durationMillis = 900),
        label = "expAnimation"
    )

    val animatedDinero by animateIntAsState(
        targetValue = displayedDineroTarget,
        animationSpec = tween(durationMillis = 900),
        label = "dineroAnimation"
    )

    val finalExp = if (perfectLesson) (exp * 1.2f).toInt() else exp
    val finalDinero = if (perfectLesson) (dinero * 1.2f).toInt() else dinero

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val confettiCount = 3
    val confettiData = remember {
        List(confettiCount) { index ->
            val offsetX = when (index) {
                0 -> screenWidth * 0.2f
                1 -> screenWidth * 0.5f
                else -> screenWidth * 0.8f
            }
            val offsetY = if (index % 2 == 0) screenHeight * 0.3f else screenHeight * 0.7f
            ConfettiPlacement(
                offsetX = offsetX,
                offsetY = offsetY,
                baseSize = (100..180).random().dp // Reducido
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "confetti-scaling")
    val scaleFactors = List(confettiCount) { index ->
        transition.animateFloat(
            initialValue = 0.7f,
            targetValue = 1.4f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 3000 + index * 1000,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale$index"
        )
    }

    // 🎬 Secuencia optimizada
    LaunchedEffect(Unit) {
        showTitle = true
        delay(700)
        showText = true
        delay(400)

        showConfetti = true // Primero confeti
        delay(1200)
        showConfetti = false // Apagamos luego

        showBaseRewards = true
        displayedExpTarget = exp
        displayedDineroTarget = dinero
        delay(1000)

        showBonusText = true
        delay(500)

        if (perfectLesson) {
            showBonusValues = true
            delay(800)
            displayedExpTarget = finalExp
            displayedDineroTarget = finalDinero
        }

        delay(400)
        showFinalRewards = true
        delay(300)
        showButton = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background_complete),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        // 🎊 Confeti sólo si se activa
        if (showConfetti) {
            confettiData.forEachIndexed { index, confetti ->
                LottieAnimation(
                    composition = composition,
                    iterations = 1, // Solo una vez
                    modifier = Modifier
                        .size(confetti.baseSize * scaleFactors[index].value)
                        .offset(
                            x = confetti.offsetX - confetti.baseSize / 2,
                            y = confetti.offsetY - confetti.baseSize / 2
                        )
                        .zIndex(10f)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = showTitle,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_felicidades),
                    contentDescription = "\u00a1Felicidades!",
                    modifier = Modifier.size(400.dp).offset(y = 10.dp)
                )
            }

            AnimatedVisibility(
                visible = showText,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                ) + fadeIn(),
                modifier = Modifier.offset(y = (-65).dp)
            ) {
                Text(
                    text = "TUS RECOMPENSAS:",
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.luckiest_guy_regular)),
                    color = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .offset(y = (-60).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = showBonusText && perfectLesson,
                    enter = scaleIn() + fadeIn()
                ) {
                    Text(
                        text = "\u00a1Bonus por cero errores!",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.luckiest_guy_regular)),
                        color = Color(0xFFFFC107),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                AnimatedVisibility(
                    visible = showBaseRewards,
                    enter = fadeIn() + slideInHorizontally(initialOffsetX = { -100 })
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RewardItem(
                            iconRes = R.drawable.ic_experience,
                            value = animatedExp,
                            showBonus = showBonusValues && perfectLesson,
                            bonusText = "+20%"
                        )
                        RewardItem(
                            iconRes = R.drawable.ic_coin,
                            value = animatedDinero,
                            showBonus = showBonusValues && perfectLesson,
                            bonusText = "+20%"
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = showButton,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeIn(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .offset(y = (-40).dp)
            ) {
                CustomButton(
                    buttonText = "OK",
                    gradientLight = temaVisual.gradientLight,
                    gradientDark = temaVisual.gradientDark,
                    baseColor = temaVisual.baseColor,
                    onClick = onContinue
                )
            }
        }
    }
}

@Composable
private fun RewardItem(
    iconRes: Int,
    value: Int,
    showBonus: Boolean,
    bonusText: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "$value",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.luckiest_guy_regular)),
                color = Color.White,
                modifier = Modifier.padding(start = 4.dp)
            )
            AnimatedVisibility(
                visible = showBonus,
                enter = scaleIn() + fadeIn()
            ) {
                Text(
                    text = bonusText,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.luckiest_guy_regular)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFC107),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

data class ConfettiPlacement(
    val offsetX: Dp,
    val offsetY: Dp,
    val baseSize: Dp
)


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun LessonCompleteScreenPreview_Normal() {
    val temaDemo = TemaVisual(
        baseColor = Color(0xFF53164F),
        gradientLight = Color(0xFF9C749A),
        gradientDark = Color(0xFF431441),
        progressColor = Color(0xDC53164F),
        teachingBackground = R.drawable.teaching_background_ahorro,
        fillBlankBackground = R.drawable.fill_blank_background_ahorro,
        multipleChoiceBackground = R.drawable.multiple_choice_background_ahorro,
        sentenceBuilderBackground = R.drawable.sentence_builder_background_ahorro,
        matchingBackground = R.drawable.matching_background_ahorro,
        dragPairsBackground = R.drawable.drag_pairs_background_ahorro,
        categoryIcon = R.drawable.ic_pesito_ahorrador,
        CloseIcon = R.drawable.ic_close_ahorro,
        progressBar = R.drawable.ic_exp_bar_morado,
        pesitoFeliz = R.drawable.ic_pesito_ahorrador_feliz,
        pesitoTriste = R.drawable.ic_pesito_ahorrador_triste
    )

    LessonCompleteScreen(
        exp = 100,
        dinero = 50,
        onContinue = {},
        temaVisual = temaDemo,
        perfectLesson = false
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun LessonCompleteScreenPreview_Perfect() {
    val temaDemo = TemaVisual(
        baseColor = Color(0xFF53164F),
        gradientLight = Color(0xFF9C749A),
        gradientDark = Color(0xFF431441),
        progressColor = Color(0xDC53164F),
        teachingBackground = R.drawable.teaching_background_ahorro,
        fillBlankBackground = R.drawable.fill_blank_background_ahorro,
        multipleChoiceBackground = R.drawable.multiple_choice_background_ahorro,
        sentenceBuilderBackground = R.drawable.sentence_builder_background_ahorro,
        matchingBackground = R.drawable.matching_background_ahorro,
        dragPairsBackground = R.drawable.drag_pairs_background_ahorro,
        categoryIcon = R.drawable.ic_pesito_ahorrador,
        CloseIcon = R.drawable.ic_close_ahorro,
        progressBar = R.drawable.ic_exp_bar_morado,
        pesitoFeliz = R.drawable.ic_pesito_ahorrador_feliz,
        pesitoTriste = R.drawable.ic_pesito_ahorrador_triste
    )

    LessonCompleteScreen(
        exp = 120, // +20% de bonus
        dinero = 60, // +20% de bonus
        onContinue = {},
        temaVisual = temaDemo,
        perfectLesson = true
    )
}