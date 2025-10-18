package com.universidad.finankids.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.R

val PoppinsBold = FontFamily(Font(R.font.poppins_bold, FontWeight.Bold))
val PoppinsSemiBold = FontFamily(Font(R.font.poppins_semibold, FontWeight.SemiBold))
val ItimRegular = FontFamily(Font(R.font.itim_regular, FontWeight.Normal))

@Composable
fun AchievementProgressBar(
    progress: Float,
    current: Int,
    total: Int
) {
    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "$current/$total",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B572A),
                modifier = Modifier.padding(end = 6.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.LightGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .background(Color(0xFFFFC107))
            )
        }
    }
}

@Composable
fun AchievementDetailScreen(
    progress: Float = 1f,
    current: Int = 3,
    total: Int = 3
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(24.dp)
                .shadow(12.dp, RoundedCornerShape(20.dp))
                .border(4.dp, Color(0xFFB0B0B0), RoundedCornerShape(20.dp))
                .background(Color(0xFFD9D9D9), RoundedCornerShape(20.dp))
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_explorador_logros),
                        contentDescription = "Icono Explorador",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "EXPLORADOR\nDE METAS",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        lineHeight = 22.sp,
                        fontFamily = PoppinsBold
                    )
                }

                // Descripción
                Text(
                    text = "Has logrado tus objetivos de ahorro y llegaste a la cima como un verdadero campeón",
                    fontSize = 15.sp,
                    color = Color.Black
                )


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        AchievementProgressBar(progress = progress, current = current, total = total)

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "¡RECLAMA TU PREMIO!",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                            fontFamily = ItimRegular
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))


                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "RECLAMAR",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                fontFamily = PoppinsSemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_coin),
                                    contentDescription = "Moneda",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "300",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }

            Image(
                painter = painterResource(id = R.drawable.ic_x),
                contentDescription = "Cerrar",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AchievementDetailScreenPreview() {
    AchievementDetailScreen(progress = 0.66f, current = 2, total = 3)
}
