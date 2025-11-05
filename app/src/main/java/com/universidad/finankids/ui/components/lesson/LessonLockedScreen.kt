package com.universidad.finankids.ui.components.lesson

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.R
import com.universidad.finankids.ui.components.CustomButton
import com.universidad.finankids.ui.lesson.TemaVisual

@Composable
fun LessonLockedScreen(
    onRestart: () -> Unit,
    onExit: () -> Unit,
    temaVisual: TemaVisual
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(R.drawable.background_locked),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(34.dp)
                .offset(y = (-40).dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_perdiste),
                contentDescription = null,
                modifier = Modifier.size(400.dp).offset(y = 10.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                modifier = Modifier
                    .offset(y = (-65).dp)
                    .fillMaxWidth(),
                text = "¡Has perdido todas tus vidas!",
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.luckiest_guy_regular)),
                color = Color.White,
                textAlign = TextAlign.Center
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
                    gradientLight = temaVisual.gradientLight,
                    gradientDark = temaVisual.gradientDark,
                    baseColor = temaVisual.baseColor,
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
}

@Preview(showBackground = true)
@Composable
fun LessonLockedScreenPreview() {
    val temaVisual = TemaVisual(
        baseColor = Color(0xFF53164F),
        gradientLight = Color(0xFF9C749A),
        gradientDark = Color(0xFF431441),
        progressColor = Color(0xDC53164F),
        teachingBackground = R.drawable.teaching_background_ahorro,
        fillBlankBackground = R.drawable.teaching_background_ahorro,
        multipleChoiceBackground = R.drawable.teaching_background_ahorro,
        sentenceBuilderBackground = R.drawable.teaching_background_ahorro,
        matchingBackground = R.drawable.teaching_background_ahorro,
        dragPairsBackground = R.drawable.teaching_background_ahorro,
        categoryIcon = R.drawable.ic_pesito_ahorrador,
        CloseIcon = R.drawable.ic_close_ahorro,
        progressBar = R.drawable.ic_exp_bar_morado,
        pesitoFeliz = R.drawable.ic_pesito_ahorrador_feliz,
        pesitoTriste = R.drawable.ic_pesito_ahorrador_triste
    )

    LessonLockedScreen(
        onRestart = { /* No hacer nada en preview */ },
        onExit = { /* No hacer nada en preview */ },
        temaVisual = temaVisual
    )
}