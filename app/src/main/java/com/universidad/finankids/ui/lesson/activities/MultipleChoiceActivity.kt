package com.universidad.finankids.ui.lesson.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun MultipleChoiceActivity(
    content: ActivityContent,
    selectedAnswer: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Título
        Text(
            text = content.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Imagen de Pesito y pregunta
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_pesito_ahorrador),
                contentDescription = "Pesito hablando",
                modifier = Modifier
                    .size(120.dp)
                    .padding(end = 16.dp)
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
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            content.options?.forEach { option ->
                MultipleChoiceOption(
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
fun MultipleChoiceOption(
    optionText: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFF4CAF50) else Color.White
    val borderColor = if (isSelected) Color(0xFF388E3C) else Color(0xFFBBBBBB)
    val textColor = if (isSelected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = optionText,
            fontSize = 16.sp,
            color = textColor,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MultipleChoiceActivityPreview() {
    val sampleContent = ActivityContent(
        type = ActivityType.MultipleChoice,
        title = "Seleccione la opción correcta:",
        question = "¿Qué opción representa un ahorro?",
        options = listOf("Banco", "Ahorro", "Inversión", "Crédito"),
        correctAnswer = "Ahorro",
        feedback = "¡Correcto! El ahorro es guardar dinero para el futuro."
    )

    MultipleChoiceActivity(
        content = sampleContent,
        selectedAnswer = null,
        onOptionSelected = {}
    )
}
