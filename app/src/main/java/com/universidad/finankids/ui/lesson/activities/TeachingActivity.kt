package com.universidad.finankids.ui.lesson.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun TeachingActivity(
    content: ActivityContent
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Título de la lección
        Text(
            text = content.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        // Imagen de Pesito
        Image(
            painter = painterResource(id = R.drawable.ic_pesito_ahorrador),
            contentDescription = "Pesito enseñando",
            modifier = Modifier.size(120.dp)
        )

        // Contenedor tipo globo de diálogo con el contenido
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xC6FFFFFF),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 2.dp,
                    color = Color(0xFFBDBDBD),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = content.explanation ?: "",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeachingActivityPreview() {
    val sampleContent = ActivityContent(
        type = ActivityType.Teaching,
        title = "Introducción al ahorro",
        explanation = "El ahorro es guardar parte de tu dinero para usarlo en el futuro. " +
                "Es importante porque te ayuda a:\n\n" +
                "• Alcanzar tus metas financieras\n" +
                "• Estar preparado para imprevistos\n" +
                "• Generar seguridad económica\n\n" +
                "Recuerda: ¡Ahorrar un poco cada día puede hacer una gran diferencia!"
    )

    TeachingActivity(content = sampleContent)
}