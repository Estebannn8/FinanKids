package com.universidad.finankids.ui.lesson.activities

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.data.model.ActivityContent
import com.universidad.finankids.data.model.MatchingPair
import kotlin.random.Random

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
    val leftItems = remember { (content.matchingPairs?.map { it.leftItem } ?: emptyList()).shuffled() }
    val rightItems = remember {
        content.shuffledRightItems ?: (content.matchingPairs?.map { it.rightItem } ?: emptyList()).shuffled()
    }

    // Pastel colors for matched pairs
    val pastelColors = listOf(
        Color(0xDFB39DDB), // Purple pastel
        Color(0xDF81C784), // Green pastel
        Color(0xDF64B5F6), // Blue pastel
        Color(0xDFFFB74D), // Orange pastel
        Color(0xDFE57373), // Red pastel
        Color(0xDF9575CD), // Deep purple pastel
        Color(0xDF4DB6AC), // Teal pastel
        Color(0xDFFFD54F)  // Yellow pastel
    )

    // Map to track colors for each matched pair
    val pairColors = remember { mutableStateOf(mapOf<MatchingPair, Color>()) }

    // Función para verificar si un ítem ya está emparejado
    fun isItemMatched(item: String, isLeft: Boolean): Boolean {
        return if (isLeft) {
            matchedPairs.any { it.leftItem == item }
        } else {
            matchedPairs.any { it.rightItem == item }
        }
    }

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
            // Left column with header
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Selecciona aquí",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                leftItems.forEach { item ->
                    MatchingItem(
                        text = item,
                        isSelected = selectedLeft == item,
                        isMatched = isItemMatched(item, true),
                        pairColor = matchedPairs.firstOrNull { it.leftItem == item }?.let {
                            pairColors.value[it]
                        },
                        onClick = {
                            // Solo permitir selección si el ítem no está emparejado
                            if (!isItemMatched(item, true)) {
                                onSelectLeft(if (selectedLeft == item) null else item)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(32.dp))

            // Right column with header
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Empareja con",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                rightItems.forEach { item ->
                    MatchingItem(
                        text = item,
                        isSelected = selectedRight == item,
                        isMatched = isItemMatched(item, false),
                        pairColor = matchedPairs.firstOrNull { it.rightItem == item }?.let {
                            pairColors.value[it]
                        },
                        onClick = {
                            // Solo permitir selección si el ítem no está emparejado
                            if (!isItemMatched(item, false)) {
                                onSelectRight(if (selectedRight == item) null else item)

                                val left = selectedLeft
                                val right = if (selectedRight == item) null else item

                                if (left != null && right != null) {
                                    // Verificar que ninguno de los ítems ya esté emparejado
                                    if (!isItemMatched(left, true) && !isItemMatched(right, false)) {
                                        // Create a new pair regardless of correctness
                                        val newPair = MatchingPair(left, right)
                                        onPairMatched(newPair)

                                        // Assign a color if this is a new pair
                                        if (newPair !in pairColors.value) {
                                            val availableColors = pastelColors.filter {
                                                it !in pairColors.value.values
                                            }
                                            val randomColor = if (availableColors.isNotEmpty()) {
                                                availableColors.random()
                                            } else {
                                                pastelColors[Random.nextInt(pastelColors.size)]
                                            }
                                            pairColors.value = pairColors.value + (newPair to randomColor)
                                        }

                                        // Clear selections
                                        onSelectLeft(null)
                                        onSelectRight(null)
                                    }
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
    pairColor: Color?,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isMatched -> pairColor ?: Color(0xFF4CAF50) // Use assigned color if matched
        else -> Color.White
    }
    val textColor = if (isMatched) Color.White else Color.Black
    val borderColor = when {
        isSelected -> Color(0xFF1976D2) // Blue border for selected items
        isMatched -> Color.Black.copy(alpha = 0.5f) // Subtle border for matched items
        else -> Color(0xFFBBBBBB) // Default light gray border
    }
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Surface(
        modifier = Modifier
            .width(160.dp)
            .height(60.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(borderWidth, borderColor),
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



