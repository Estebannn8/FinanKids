package com.universidad.finankids.ui.components.lesson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.ui.components.CustomButton

@Composable
fun AllLessonsCompletedScreen(onBack: () -> Unit) {
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