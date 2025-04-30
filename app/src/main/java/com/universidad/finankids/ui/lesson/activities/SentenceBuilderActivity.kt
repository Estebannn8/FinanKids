package com.universidad.finankids.ui.lesson.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.R
import com.universidad.finankids.data.model.ActivityContent
import kotlin.math.roundToInt

@Composable
fun SentenceBuilderActivity(
    content: ActivityContent,
    placedWords: List<String?>,
    availableWords: List<String>,
    onWordPlaced: (String) -> Unit,
    onWordRemoved: (Int, String) -> Unit
) {

    var draggedWord by remember { mutableStateOf<String?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    val scrollState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_pesito_ahorrador),
                contentDescription = "Pesito hablando",
                modifier = Modifier.size(120.dp).padding(end = 8.dp)
            )
            Box(
                modifier = Modifier
                    .background(Color(0xC6FFFFFF), RoundedCornerShape(16.dp))
                    .border(2.dp, Color(0xFFBDBDBD), RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .widthIn(max = 250.dp)
            ) {
                Text(
                    text = content.question ?: "Ordena las palabras para formar la oración correcta.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    lineHeight = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.White)
                .border(1.dp, Color(0xFFBDBDBD), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxSize()
            ) {
                placedWords.forEachIndexed { index, word ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(if (word != null) Color(0xFFE3F2FD) else Color.Transparent)
                            .border(
                                1.dp,
                                if (word != null) Color(0xFF90CAF9) else Color(0xFFBDBDBD),
                                RoundedCornerShape(4.dp)
                            )
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = {
                                        if (word != null) {
                                            draggedWord = word
                                            onWordRemoved(index, word)
                                        }
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        dragOffset += dragAmount
                                    },
                                    onDragEnd = {
                                        draggedWord = null
                                        dragOffset = Offset.Zero
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (word != null) {
                            Text(text = word, fontSize = 16.sp, color = Color.Black)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(0xFFE3F2FD).copy(alpha = 0.7f))
                .border(1.dp, Color(0xFFBBBBBB), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            if (availableWords.isEmpty()) {
                Text(
                    text = "¡Todas las palabras colocadas!",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            } else {
                LazyRow(
                    state = scrollState,
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(availableWords) { word ->
                        Box(
                            modifier = Modifier
                                .background(Color.White, RoundedCornerShape(16.dp))
                                .border(2.dp, Color(0xFFBBBBBB), RoundedCornerShape(16.dp))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragStart = {
                                            draggedWord = word
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            dragOffset += dragAmount
                                        },
                                        onDragEnd = {
                                            val emptyIndex = placedWords.indexOfFirst { it == null }
                                            if (emptyIndex != -1) {
                                                onWordPlaced(word)
                                            }
                                            draggedWord = null
                                            dragOffset = Offset.Zero
                                        }
                                    )
                                }
                        ) {
                            Text(text = word, fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }

    draggedWord?.let { word ->
        Box(
            modifier = Modifier
                .offset(dragOffset.x.roundToInt().dp, dragOffset.y.roundToInt().dp)
                .background(Color(0xC6FFFFFF), RoundedCornerShape(16.dp))
                .border(2.dp, Color(0xFFBDBDBD), RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(text = word, fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Medium)
        }
    }
}
