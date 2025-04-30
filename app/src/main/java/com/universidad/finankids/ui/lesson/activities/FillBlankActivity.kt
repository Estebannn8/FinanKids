package com.universidad.finankids.ui.lesson.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.R
import com.universidad.finankids.data.model.ActivityContent
import com.universidad.finankids.data.model.ActivityType

@Composable
fun FillBlankActivity(
    content: ActivityContent,
    selectedAnswer: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la actividad
        Text(
            text = content.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Start,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        // Sección de Pesito + globo de diálogo
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_pesito_ahorrador),
                contentDescription = "Pesito hablando",
                modifier = Modifier
                    .size(120.dp)
                    .padding(end = 8.dp)
            )

            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xC6FFFFFF),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color(0xFFBDBDBD),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .widthIn(max = 250.dp)
            ) {
                Text(
                    text = content.question ?: "",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    lineHeight = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            content.options?.forEach { option ->
                FillBlankOptionButton(
                    optionText = option,
                    isSelected = selectedAnswer == option,
                    onClick = {
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}

@Composable
fun FillBlankOptionButton(
    optionText: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFF4CAF50) else Color(0xFFE0E0E0)
    val textColor = if (isSelected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = optionText,
            fontSize = 18.sp,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFillBlankActivity() {
    val sampleContent = ActivityContent(
        type = ActivityType.FillBlank,
        title = "Completa el espacio en blanco",
        question = "El banco guarda tu _____ de manera segura.",
        options = listOf("dinero", "ropa", "comida", "mascota"),
        correctAnswer = "dinero",
        feedback = "¡Correcto! Los bancos guardan tu dinero de manera segura."
    )

    FillBlankActivity(
        content = sampleContent,
        selectedAnswer = null,
        onOptionSelected = {}
    )
}
