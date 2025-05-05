package com.universidad.finankids.ui.Components.lesson

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.ui.Components.CustomButton
import com.universidad.finankids.ui.lesson.TemaVisual

@Composable
fun LessonLockedScreen(
    onRestart: () -> Unit,
    onExit: () -> Unit,
    temaVisual: TemaVisual
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(temaVisual.pesitoTriste),
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