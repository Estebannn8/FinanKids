package com.universidad.finankids.ui.components.lesson

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.universidad.finankids.R
import com.universidad.finankids.ui.lesson.TemaVisual

@Composable
fun FeedbackOverlay(
    isCorrect: Boolean,
    onDismiss: () -> Unit,
    temaVisual: TemaVisual
) {
    val interactionSource = remember { MutableInteractionSource() }

    // Control de animación
    val scale = remember { Animatable(0f) }

    // Lanzamos la animación cuando entra en composición
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 700,
                easing = {
                    OvershootInterpolator(2.5f).getInterpolation(it)
                }
            )
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onDismiss
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val image = if (isCorrect) {
                R.drawable.ic_correcto
            } else {
                R.drawable.ic_incorrecto
            }

            // Imagen principal con animación
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .size(if (isCorrect) 280.dp else 300.dp)
                    .offset(y = if (isCorrect) 10.dp else 21.dp)
                    .graphicsLayer(
                        scaleX = scale.value,
                        scaleY = scale.value
                    )
            )

            val imageRes = if (isCorrect) temaVisual.pesitoFeliz else temaVisual.pesitoTriste

            // Imagen secundaria con la misma animación
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(300.dp)
                    .padding(26.dp)
                    .offset(y = (-50).dp)
                    .graphicsLayer(
                        scaleX = scale.value,
                        scaleY = scale.value
                    )
            )
        }
    }
}



@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun FeedbackOverlayPreview() {
    val temaAhorro = TemaVisual(
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
        pesitoFeliz = R.drawable.ic_pesito_original_feliz,
        pesitoTriste = R.drawable.ic_pesito_original_triste
    )

    // Previsualización para respuesta correcta
    FeedbackOverlay(
        isCorrect = true,
        onDismiss = { },
        temaVisual = temaAhorro
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun FeedbackOverlayIncorrectPreview() {
    val temaAhorro = TemaVisual(
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
        pesitoFeliz = R.drawable.ic_pesito_original_feliz,
        pesitoTriste = R.drawable.ic_pesito_original_triste
    )

    // Previsualización para respuesta incorrecta
    FeedbackOverlay(
        isCorrect = false,
        onDismiss = { },
        temaVisual = temaAhorro
    )
}
