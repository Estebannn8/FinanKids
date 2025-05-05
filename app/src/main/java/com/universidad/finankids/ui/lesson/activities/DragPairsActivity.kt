package com.universidad.finankids.ui.lesson.activities

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DragPairsActivity(
    state: LessonState,
    onEvent: (LessonEvent) -> Unit,
    temaVisual: TemaVisual
) {
    val activity = state.currentActivity ?: return

    val leftItems = state.leftItems
    val rightItems = state.rightItems

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(temaVisual.categoryIcon),
                contentDescription = "Pesito hablando",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xC6FFFFFF), RoundedCornerShape(16.dp))
                    .border(2.dp, Color(0xFFBDBDBD), RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .widthIn(max = 250.dp)
            ) {
                Text(
                    text = activity.question ?: "Ordena los elementos de la derecha para que coincidan con los de la izquierda",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Left column (fixed items)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                leftItems.forEach { item ->
                    DragPairItem(
                        text = item,
                        isHighlighted = false
                    )
                }
            }

            // Right column (sortable items)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                itemsIndexed(rightItems) { index, item ->
                    Box(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .animateItemPlacement()
                            .clickable {
                                if (state.selectedRight == null) {
                                    onEvent(LessonEvent.SelectRightItem(item))
                                } else {
                                    if (state.selectedRight != item) {
                                        // Swap items
                                        val selectedIndex = rightItems.indexOf(state.selectedRight)
                                        val currentIndex = rightItems.indexOf(item)
                                        if (selectedIndex != -1 && currentIndex != -1) {
                                            val newList = rightItems.toMutableList().apply {
                                                this[selectedIndex] = this[currentIndex].also {
                                                    this[currentIndex] = this[selectedIndex]
                                                }
                                            }
                                            onEvent(LessonEvent.UpdateRightItems(newList))
                                        }
                                    }
                                    onEvent(LessonEvent.SelectRightItem(null))
                                }
                            }
                    ) {
                        DragPairItem(
                            text = item,
                            isHighlighted = state.selectedRight == item
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DragPairItem(
    text: String,
    isHighlighted: Boolean
) {
    val backgroundColor = if (isHighlighted) Color(0xFFE3F2FD) else Color.White.copy(alpha = 0.7f)
    val borderColor = if (isHighlighted) Color(0xFF2196F3) else Color(0xFFBBBBBB)

    Surface(
        modifier = Modifier
            .width(160.dp)
            .height(60.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        color = backgroundColor,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
