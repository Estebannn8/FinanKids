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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.events.LessonEvent
import com.universidad.finankids.state.LessonState
import com.universidad.finankids.ui.lesson.TemaVisual

@Composable
fun SentenceBuilderActivity(
    state: LessonState,
    onEvent: (LessonEvent) -> Unit,
    temaVisual: TemaVisual
) {
    val activity = state.currentActivity ?: return
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Character and question section
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(temaVisual.categoryIcon),
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
                    text = activity.question ?: "Ordena las palabras para formar la oración correcta.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    lineHeight = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sentence construction section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFFD3D3D3), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Split placed words into rows
                val rows = remember(state.placedWords, density) {
                    mutableListOf<List<String>>().apply {
                        val currentRow = mutableListOf<String>()
                        var currentRowWidth = 0f

                        val maxRowWidth = with(density) {
                            configuration.screenWidthDp.dp.toPx() - 48.dp.toPx()
                        }

                        state.placedWords.filterNotNull().forEach { word ->
                            val wordWidth = with(density) {
                                (word.length * 10.sp.toPx()) + 16.dp.toPx()
                            }

                            if (currentRow.isNotEmpty() && currentRowWidth + wordWidth > maxRowWidth) {
                                add(currentRow.toList())
                                currentRow.clear()
                                currentRowWidth = 0f
                            }

                            currentRow.add(word)
                            currentRowWidth += with(density) {
                                wordWidth + 8.dp.toPx()
                            }
                        }

                        if (currentRow.isNotEmpty()) {
                            add(currentRow.toList())
                        }
                    }
                }

                // Display each row
                rows.forEachIndexed { rowIndex, words ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFF787878),
                            thickness = 1.dp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            words.forEach { word ->
                                val absoluteIndex = state.placedWords.indexOfFirst { it == word }
                                if (absoluteIndex >= 0) {
                                    Box(
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                            .clickable { onEvent(LessonEvent.RemoveWord(absoluteIndex)) }
                                            .border(color = Color.Gray, width = 1.dp)
                                    ) {
                                        Text(
                                            text = word,
                                            fontSize = 18.sp,
                                            color = Color.Black,
                                            modifier = Modifier
                                                .background(Color(0xFFFFFFFF), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Additional empty line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF787878),
                        thickness = 1.dp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Available words section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(0xFFFFFFFF).copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFFBBBBBB), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            if (state.availableWords.isEmpty()) {
                Text(
                    text = "¡Todas las palabras colocadas!",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxSize().border(1.dp, Color(0xFFBBBBBB), RoundedCornerShape(8.dp)),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(state.availableWords) { word ->
                        Box(
                            modifier = Modifier
                                .background(Color.White, RoundedCornerShape(16.dp))
                                .border(2.dp, Color(0xFFBBBBBB), RoundedCornerShape(16.dp))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable {
                                    val index = state.placedWords.indexOfFirst { it == null }
                                    if (index != -1) {
                                        onEvent(LessonEvent.PlaceWord(word, index))
                                    }
                                }
                        ) {
                            Text(
                                text = word,
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}