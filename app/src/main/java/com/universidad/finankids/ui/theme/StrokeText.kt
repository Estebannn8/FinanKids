package com.universidad.finankids.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun StrokeText(
    text: String,
    fontSize: TextUnit = 16.sp,
    fontFamily: FontFamily,
    strokeColor: Color = Color.White,
    fillColor: Color = Color.White,
    strokeWidth: Float = 0.51f,
    letterSpacing: TextUnit = 0.sp,
    textAlign: TextAlign = TextAlign.Center,
    lineHeight: TextUnit = 18.99.sp,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        // Capa de contorno
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = fontFamily,
            style = TextStyle(
                drawStyle = Stroke(width = strokeWidth),
                color = strokeColor,
                letterSpacing = letterSpacing,
                textAlign = textAlign,
                lineHeight = lineHeight
            )
        )
        // Capa de relleno
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = fontFamily,
            color = fillColor,
            letterSpacing = letterSpacing,
            textAlign = textAlign,
            lineHeight = lineHeight
        )
    }
}
