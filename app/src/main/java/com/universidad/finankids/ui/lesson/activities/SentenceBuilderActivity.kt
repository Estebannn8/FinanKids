package com.universidad.finankids.ui.lesson.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceBuilderActivity() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Pesito + texto indicativo
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_pesito_ahorrador),
                contentDescription = "Pesito hablando",
                modifier = Modifier
                    .size(120.dp)
                    .padding(end = 8.dp)
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
                    text = "Ordena las palabras para formar la oración correcta.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    lineHeight = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Contenedor tipo cuaderno (líneas con espacio suficiente)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp) // Aumenté altura
                .background(Color.White)
                .border(1.dp, Color(0xFFBDBDBD), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly, // líneas bien espaciadas
                modifier = Modifier.fillMaxSize()
            ) {
                repeat(3) { // Número de líneas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFBDBDBD))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Palabras desordenadas
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf("dinero", "en", "guarda", "banco", "El").forEach { word ->
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

@Preview(showBackground = true)
@Composable
fun PreviewSentenceBuilderActivity() {
    SentenceBuilderActivity()
}

