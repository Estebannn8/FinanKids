package com.universidad.finankids.ui.lesson

import androidx.compose.ui.graphics.Color

data class TemaVisual(
    val baseColor: Color,
    val gradientLight: Color,
    val gradientDark: Color,
    val progressColor: Color,
    val teachingBackground: Int,   // (Enseñanza) - Teaching screens with explanations
    val fillBlankBackground: Int,  // (Completar espacio en blanco)
    val multipleChoiceBackground: Int,   // (Selección múltiple)
    val sentenceBuilderBackground: Int,  // (Armar/ordenar oración)
    val matchingBackground: Int,      // (Seleccionar parejas)
    val dragPairsBackground: Int,    // (Pares arrastrando)
    val categoryIcon: Int,
    val CloseIcon: Int,
    val progressBar: Int
)
