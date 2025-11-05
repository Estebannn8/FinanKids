package com.universidad.finankids.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.universidad.finankids.R
import com.universidad.finankids.data.model.LogroUI

val PoppinsBold = FontFamily(Font(R.font.poppins_bold, FontWeight.Bold))
val PoppinsSemiBold = FontFamily(Font(R.font.poppins_semibold, FontWeight.SemiBold))
val ItimRegular = FontFamily(Font(R.font.itim_regular, FontWeight.Normal))

@Composable
fun AchievementDetailDialog(
    logroUI: LogroUI,
    onClaim: () -> Unit,
    onDismiss: () -> Unit
) {
    val unlocked = logroUI.desbloqueado
    var claimedLocal by remember { mutableStateOf(logroUI.reclamado) }

    val progress = logroUI.progresoActual.toFloat() / logroUI.progresoTotal.toFloat()

    // Animación de entrada suave
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(220, easing = FastOutSlowInEasing),
        label = ""
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = false) {} // evita click-through
    ) {
        Box(
            modifier = Modifier
                .scale(scale)
                .fillMaxWidth(0.88f)
                .wrapContentHeight()
                .shadow(14.dp, RoundedCornerShape(24.dp))
                .border(4.dp, Color(0xFFB0B0B0), RoundedCornerShape(24.dp))
                .background(Color(0xFFD9D9D9), RoundedCornerShape(24.dp))
                .padding(horizontal = 24.dp, vertical = 28.dp)
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                // Encabezado
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberAsyncImagePainter(logroUI.logro.iconoUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(68.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(Modifier.width(16.dp))

                    Text(
                        text = logroUI.logro.titulo.uppercase(),
                        fontSize = 20.sp,
                        fontFamily = PoppinsBold,
                        color = Color.Black,
                        lineHeight = 20.sp
                    )
                }

                // Descripción
                Text(
                    logroUI.logro.descripcion,
                    fontSize = 15.sp,
                    fontFamily = ItimRegular,
                    color = Color(0xFF2E2E2E),
                    lineHeight = 18.sp
                )

                // Barra de progreso
                AchievementProgressBar(
                    progress = progress,
                    current = logroUI.progresoActual,
                    total = logroUI.progresoTotal
                )

                Text(
                    text = when {
                        !unlocked -> "Sigue mejorando…"
                        unlocked && !claimedLocal -> "¡Reclama tu recompensa!"
                        else -> "Recompensa reclamada"
                    },
                    fontSize = 13.sp,
                    fontFamily = ItimRegular,
                    color = Color(0xFF6D563A)
                )
            }

            // Botón cerrar
            Image(
                painter = painterResource(id = R.drawable.ic_x),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = (-28).dp, x = (20).dp)
                    .padding(10.dp)
                    .size(32.dp)
                    .clickable { onDismiss() }
            )

            // Botón reclamar (flotante)
            if (unlocked && !claimedLocal) {
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .clickable {
                            claimedLocal = true  // cambia UI inmediatamente
                            onClaim()
                        }
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.multiple_coins),
                            contentDescription = null,
                            modifier = Modifier.size(60.dp)
                        )
                        Text(
                            text = "RECLAMAR",
                            fontSize = 22.sp,
                            fontFamily = PoppinsBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_coin),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${logroUI.logro.dineroRecompensa}",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}


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


/*
@Preview(showBackground = true)
@Composable
fun AchievementDetailScreenPreview() {
    AchievementDetailDialog(progress = 0.66f, current = 2, total = 3)
}
 */
