package com.universidad.finankids.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
        modifier = modifier
            .width(287.dp)
            .height(37.dp),
        enabled = enabled,
        shape = RoundedCornerShape(40.88.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(gradientLight, gradientDark),
                            start = Offset.Zero,
                            end = Offset(0F, 36.72F),
                        ),
                        shape = RoundedCornerShape(40.88.dp)
                    )
                    .border(
                        width = 3.dp,
                        color = baseColor,
                        shape = RoundedCornerShape(40.88.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .offset(y = 5.05.dp)
                        .padding(2.06.dp)
                        .fillMaxWidth()
                        .height(23.06.dp)
                        .background(
                            color = baseColor,
                            shape = RoundedCornerShape(40.88.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(19.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
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
                }
            }
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

@Preview
@Composable
fun CustomButtonPreview() {
    CustomButton(
        buttonText = "RESTABLECER",
        gradientLight = Color(0xFF9C749A),
        gradientDark = Color(0xFF431441),
        baseColor = Color(0xFF53164F),
        onClick = {}
    )
}

