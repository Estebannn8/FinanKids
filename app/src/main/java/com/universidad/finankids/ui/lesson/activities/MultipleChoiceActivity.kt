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
fun MultipleChoiceActivity(
    state: LessonState,
    onEvent: (LessonEvent) -> Unit,
    temaVisual: TemaVisual
) {
    val activity = state.currentActivity ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = activity.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(temaVisual.categoryIcon),
                contentDescription = "Pesito hablando",
                modifier = Modifier
                    .size(130.dp)
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
                    text = activity.question ?: "",
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
            activity.options?.forEach { option ->
                MultipleChoiceOption(
                    optionText = option,
                    isSelected = state.selectedAnswer == option,
                    onClick = {
                        onEvent(LessonEvent.SelectAnswer(option))
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
    val backgroundColor = if (isSelected) Color(0xDF64B5F6).copy(alpha = 0.7f) else Color.White.copy(alpha = 0.7f)
    val borderColor = if (isSelected) Color(0xFF64B5F6) else Color(0xFFBBBBBB)
    val textColor = if (isSelected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
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
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}