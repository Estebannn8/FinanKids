package com.universidad.finankids.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

import com.universidad.finankids.ui.theme.Baloo as BalooFont
import com.universidad.finankids.ui.theme.Itim as ItimFont

object AppTypography {
    val Baloo = BalooFont
    val Itim = ItimFont
    val PoppinsFont = Poppins

    @Composable
    fun BalooStroke(
        text: String,
        fontSize: TextUnit = 24.sp,
        strokeColor: Color = Color.White,
        fillColor: Color = Color.White,
        letterSpacing: TextUnit = 3.07.sp,
        textAlign: TextAlign = TextAlign.Center,
        lineHeight: TextUnit = 18.99.sp
    ) {
        StrokeText(text = text, fontSize = fontSize, fontFamily = Baloo, strokeColor = strokeColor, fillColor = fillColor, letterSpacing = letterSpacing, textAlign = textAlign, lineHeight = lineHeight)
    }

    @Composable
    fun ItimStroke(
        text: String,
        fontSize: TextUnit = 24.sp,
        strokeColor: Color = Color.White,
        fillColor: Color = Color.White,
        letterSpacing: TextUnit = 3.07.sp,
        textAlign: TextAlign,
        lineHeight: TextUnit
    ) {
        StrokeText(text = text, fontSize = fontSize, fontFamily = Itim, strokeColor = strokeColor, fillColor = fillColor, letterSpacing = letterSpacing, textAlign = textAlign, lineHeight = lineHeight)
    }
}
