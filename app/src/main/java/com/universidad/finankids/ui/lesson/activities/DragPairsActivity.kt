package com.universidad.finankids.ui.lesson.activities

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.R
import com.universidad.finankids.data.model.ActivityContent

@Composable
fun DragPairsActivity(
    content: ActivityContent,
    rightItems: List<String>,
    onSwap: (List<String>) -> Unit,
    currentDragIndex: Int?,
    onDragIndexChange: (Int?) -> Unit
) {
    val pairs = content.orderedPairs ?: emptyList()
    val leftItems = pairs.sortedBy { it.correctPosition }.map { it.item }

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
                painter = painterResource(id = R.drawable.ic_pesito_ahorrador),
                contentDescription = "Pesito hablando",
                modifier = Modifier
                    .size(120.dp)
                    .padding(end = 16.dp)
            )

            Box(
                modifier = Modifier
                    .background(Color(0xC6FFFFFF), RoundedCornerShape(16.dp))
                    .border(2.dp, Color(0xFFBDBDBD), RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .widthIn(max = 250.dp)
            ) {
                Text(
                    text = content.question ?: "Arrastra los números a la posición correcta:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

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
                leftItems.forEachIndexed { index, item ->
                    DragPairItem(
                        text = "${index + 1}. $item",
                        isDraggable = false,
                        isHighlighted = false
                    )
                }
            }

            Spacer(modifier = Modifier.width(32.dp))

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                rightItems.forEachIndexed { index, item ->
                    val isBeingDragged = currentDragIndex == index

                    DragPairItem(
                        text = item,
                        isDraggable = true,
                        isHighlighted = isBeingDragged,
                        onDragStart = { onDragIndexChange(index) },
                        onDragEnd = { onDragIndexChange(null) },
                        onSwapWith = { targetIndex ->
                            if (index != targetIndex) {
                                val newList = rightItems.toMutableList()
                                newList[index] = rightItems[targetIndex]
                                newList[targetIndex] = rightItems[index]
                                onSwap(newList)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DragPairItem(
    text: String,
    isDraggable: Boolean,
    isHighlighted: Boolean,
    onDragStart: (() -> Unit)? = null,
    onDragEnd: (() -> Unit)? = null,
    onSwapWith: ((Int) -> Unit)? = null
) {
    val backgroundColor = if (isHighlighted) Color(0xFFE3F2FD) else Color.White
    val borderColor = if (isHighlighted) Color(0xFF2196F3) else Color(0xFFBBBBBB)

    Surface(
        modifier = Modifier
            .width(160.dp)
            .height(60.dp)
            .then(
                if (isDraggable && onDragStart != null && onDragEnd != null && onSwapWith != null) {
                    Modifier.pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { onDragStart() },
                            onDragEnd = { onDragEnd() },
                            onDrag = { change, _ -> change.consume() }
                        )
                    }
                } else Modifier
            ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        color = backgroundColor,
        shadowElevation = 2.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}
