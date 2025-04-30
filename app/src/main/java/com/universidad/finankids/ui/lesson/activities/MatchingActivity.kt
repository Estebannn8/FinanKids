package com.universidad.finankids.ui.lesson.activities

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.data.model.ActivityContent
import com.universidad.finankids.data.model.ActivityType
import com.universidad.finankids.data.model.MatchingPair

@Composable
fun MatchingActivity(
    content: ActivityContent,
    matchedPairs: Set<MatchingPair>,
    onPairMatched: (MatchingPair) -> Unit,
    selectedLeft: String?,
    selectedRight: String?,
    onSelectLeft: (String?) -> Unit,
    onSelectRight: (String?) -> Unit
) {
    val pairs = content.matchingPairs ?: emptyList()
    val leftItems = pairs.map { it.leftItem }
    val rightItems = content.shuffledRightItems ?: pairs.map { it.rightItem }.shuffled()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = content.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                leftItems.forEach { item ->
                    MatchingItem(
                        text = item,
                        isSelected = selectedLeft == item,
                        isMatched = matchedPairs.any { it.leftItem == item },
                        onClick = {
                            onSelectLeft(if (selectedLeft == item) null else item)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(32.dp))

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                rightItems.forEach { item ->
                    MatchingItem(
                        text = item,
                        isSelected = selectedRight == item,
                        isMatched = matchedPairs.any { it.rightItem == item },
                        onClick = {
                            onSelectRight(if (selectedRight == item) null else item)

                            val left = selectedLeft
                            val right = if (selectedRight == item) null else item

                            if (left != null && right != null) {
                                val isCorrect = pairs.any {
                                    it.leftItem == left && it.rightItem == right
                                }
                                if (isCorrect) {
                                    onPairMatched(MatchingPair(left, right))
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MatchingItem(
    text: String,
    isSelected: Boolean,
    isMatched: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isMatched -> Color(0xFF4CAF50)
        isSelected -> Color(0xFF2196F3)
        else -> Color.White
    }
    val textColor = if (isSelected || isMatched) Color.White else Color.Black
    val borderColor = if (isSelected) Color(0xFF1976D2) else Color(0xFFBBBBBB)

    Surface(
        modifier = Modifier
            .width(160.dp)
            .height(60.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        color = backgroundColor,
        shadowElevation = 2.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = textColor,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatchingActivityPreview() {
    val content = ActivityContent(
        type = ActivityType.Matching,
        title = "Empareja los conceptos",
        matchingPairs = listOf(
            MatchingPair("Banco", "Guarda dinero"),
            MatchingPair("Ahorro", "Guardar para el futuro")
        ),
        shuffledRightItems = listOf("Guarda dinero", "Guardar para el futuro"),
        feedback = "¡Correcto!"
    )

    MatchingActivity(
        content = content,
        matchedPairs = emptySet(),
        onPairMatched = {},
        selectedLeft = null,
        selectedRight = null,
        onSelectLeft = {},
        onSelectRight = {}
    )
}
