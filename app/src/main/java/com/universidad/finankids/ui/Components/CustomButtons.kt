package com.universidad.finankids.ui.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.R
import com.universidad.finankids.ui.theme.AppTypography

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    gradientLight: Color,
    gradientDark: Color,
    baseColor: Color,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth(0.8f) // responsive horizontal
            .aspectRatio(287f / 37f), // mantiene proporción original
        shape = RoundedCornerShape(40.88.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Fondo gradiente + borde
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(gradientLight, gradientDark),
                            start = Offset.Zero,
                            end = Offset(0f, Float.POSITIVE_INFINITY)
                        ),
                        shape = RoundedCornerShape(40.88.dp)
                    )
                    .border(
                        width = 3.dp,
                        color = baseColor,
                        shape = RoundedCornerShape(40.88.dp)
                    )
            )

            // Columna base color un poco más abajo del centro
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f) // altura del rectángulo base
                    .align(Alignment.TopCenter)
                    .offset(y = 6.dp) // desplazado para dejar más visible el gradiente arriba
                    .background(
                        color = baseColor,
                        shape = RoundedCornerShape(40.88.dp)
                    ),
                contentAlignment = Alignment.Center // centra el texto vertical y horizontalmente
            ) {
                AppTypography.BalooStroke(
                    text = buttonText,
                    strokeColor = Color.White,
                    fillColor = Color.White,
                    fontSize = 16.88.sp,
                    letterSpacing = 3.07.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.99.sp,
                )
            }

            // Brillo decorativo
            Image(
                painter = painterResource(id = R.drawable.ic_refresh),
                contentDescription = "Brillo del botón",
                modifier = Modifier
                    .size(28.dp, 15.dp)
                    .align(Alignment.TopStart)
                    .offset(x = 11.dp, y = 2.61.dp)
            )
        }
    }
}





@Preview(showBackground = true, widthDp = 360, heightDp = 100)
@Composable
fun CustomButtonPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDEDED)), // fondo suave para visualizar bordes
        contentAlignment = Alignment.Center
    ) {
        CustomButton(
            buttonText = "RESTABLECER",
            gradientLight = Color(0xFFF7E27C),
            gradientDark = Color(0xFFFF9D00),
            baseColor = Color(0xFFF8B528),
            onClick = {}
        )
    }
}


