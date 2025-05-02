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
import com.universidad.finankids.R
import com.universidad.finankids.data.model.ActivityContent

@Composable
fun SentenceBuilderActivity(
    content: ActivityContent,
    placedWords: List<String?>,
    availableWords: List<String>,
    onWordPlaced: (String) -> Unit,
    onWordRemoved: (Int, String) -> Unit
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Sección del personaje y la pregunta (sin cambios)
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

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de construcción de la oración
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            // Dividir las palabras colocadas en renglones
            val rows = remember(placedWords, density) {
                mutableListOf<List<String>>().apply {
                    val currentRow = mutableListOf<String>()
                    var currentRowWidth = 0f

                    val maxRowWidth = with(density) {
                        configuration.screenWidthDp.dp.toPx() - 48.dp.toPx()
                    }

                    placedWords.filterNotNull().forEach { word ->
                        val wordWidth = with(density) {
                            // Estimación mejorada del ancho
                            (word.length * 10.sp.toPx()) + 16.dp.toPx() // Texto + padding
                        }

                        if (currentRow.isNotEmpty() && currentRowWidth + wordWidth > maxRowWidth) {
                            add(currentRow.toList())
                            currentRow.clear()
                            currentRowWidth = 0f
                        }

                        currentRow.add(word)
                        currentRowWidth += with(density) {
                            wordWidth + 8.dp.toPx() // Espacio entre palabras
                        }
                    }

                    if (currentRow.isNotEmpty()) {
                        add(currentRow.toList())
                    }
                }
            }

            // Mostrar cada renglón
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
                        color = Color(0xFFD3D3D3),
                        thickness = 1.dp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        words.forEach { word ->
                            val absoluteIndex = placedWords.indexOfFirst { it == word }
                            if (absoluteIndex >= 0) {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .clickable { onWordRemoved(absoluteIndex, word) }
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

            // Línea vacía adicional
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFD3D3D3),
                    thickness = 1.dp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de palabras disponibles (sin cambios)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(0xFFFFFFFF).copy(alpha = 0.7f))
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
                                .clickable {
                                    onWordPlaced(word)
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