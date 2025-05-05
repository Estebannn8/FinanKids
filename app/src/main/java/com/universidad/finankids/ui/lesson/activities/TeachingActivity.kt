package com.universidad.finankids.ui.lesson.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.events.LessonEvent
import com.universidad.finankids.state.LessonState
import com.universidad.finankids.ui.lesson.TemaVisual

@Composable
fun TeachingActivity(
    state: LessonState,
    onEvent: (LessonEvent) -> Unit,
    temaVisual: TemaVisual
) {
    // Obtenemos la actividad actual del estado
    val activity = state.currentActivity ?: return

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
            text = activity.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        // Imagen de Pesito
        Image(
            painter = painterResource(temaVisual.categoryIcon),
            contentDescription = "Pesito enseñando",
            modifier = Modifier.size(130.dp)
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
                text = activity.explanation ?: "",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

    }
}
