package com.universidad.finankids.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.R
import kotlinx.coroutines.delay

@Composable
fun BuyConfirmation(
    precio: Int,
    errorMessage: String? = null,
    onBuy: () -> Unit,
    onCancel: () -> Unit
) {
    // Altura dinámica con animación
    val targetHeight = if (errorMessage.isNullOrEmpty()) 180.dp else 240.dp
    val animatedHeight by animateDpAsState(targetValue = targetHeight, label = "boxHeight")

    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(animatedHeight), // 👈 altura cambia según error
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_menu_name),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Desea comprar este avatar por:",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.baloo_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF666666),
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .offset(x = (-50).dp, y = (-48).dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_coin),
                    contentDescription = "Moneda",
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 24.sp,
                                fontFamily = FontFamily(Font(R.font.baloo_regular)),
                                fontWeight = FontWeight(500),
                                color = Color(0xDC53164F)
                            )
                        ) {
                            append("$precio")
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                fontSize = 24.sp,
                                fontFamily = FontFamily(Font(R.font.baloo_regular)),
                                fontWeight = FontWeight(500),
                                color = Color(0xFF666666)
                            )
                        ) {
                            append("?")
                        }
                    }
                )
            }

            if (!errorMessage.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.offset(y = (-45).dp),
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.baloo_regular))
                )
            }
        }

        // Botón Comprar
        var buyClicked by remember { mutableStateOf(false) }
        val buyInteractionSource = remember { MutableInteractionSource() }
        val buyScale by animateFloatAsState(
            targetValue = if (buyClicked) 1.1f else 1f,
            animationSpec = tween(durationMillis = 200),
            finishedListener = { buyClicked = false }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_boton_comprar),
            contentDescription = "Comprar",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(y = 8.dp, x = (-20).dp)
                .size(110.dp)
                .scale(buyScale)
                .clickable(
                    interactionSource = buyInteractionSource,
                    indication = null
                ) {
                    buyClicked = true
                    onBuy()
                }
        )

        // Botón Cancelar
        var cancelClicked by remember { mutableStateOf(false) }
        val cancelInteractionSource = remember { MutableInteractionSource() }
        val cancelScale by animateFloatAsState(
            targetValue = if (cancelClicked) 1.1f else 1f,
            animationSpec = tween(durationMillis = 200),
            finishedListener = { cancelClicked = false }
        )

        // Efecto para retrasar el cierre
        LaunchedEffect(cancelClicked) {
            if (cancelClicked) {
                delay(200) // mismo tiempo que la animación
                onCancel()
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ic_boton_cancelar),
            contentDescription = "Cancelar",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(y = 8.dp, x = (20).dp)
                .size(110.dp)
                .scale(cancelScale)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    cancelClicked = true
                }
        )
    }
}

