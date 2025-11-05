package com.universidad.finankids.ui.lesson

import androidx.compose.ui.graphics.Color
import com.universidad.finankids.R

object TemaVisualManager {

    // Definir los temas para cada categoría
    private val temasPorCategoria = mapOf(
        "ahorro" to TemaVisual(
            baseColor = Color(0xFF53164F),
            gradientLight = Color(0xFF9C749A),
            gradientDark = Color(0xFF431441),
            progressColor = Color(0xDC53164F),
            teachingBackground = R.drawable.teaching_background_ahorro,
            fillBlankBackground = R.drawable.teaching_background_ahorro,
            multipleChoiceBackground = R.drawable.teaching_background_ahorro,
            sentenceBuilderBackground = R.drawable.teaching_background_ahorro,
            matchingBackground = R.drawable.teaching_background_ahorro,
            dragPairsBackground = R.drawable.teaching_background_ahorro,
            categoryIcon = R.drawable.ic_pesito_ahorrador,
            CloseIcon = R.drawable.ic_close_ahorro,
            progressBar = R.drawable.ic_exp_bar_morado,
            pesitoFeliz = R.drawable.ic_pesito_ahorrador_feliz,
            pesitoTriste = R.drawable.ic_pesito_ahorrador_triste
        ),
        "basica" to TemaVisual(
            baseColor = Color(0xFFF8B528),
            gradientLight = Color(0xFFF7E27C),
            gradientDark = Color(0xFFFF9D00),
            progressColor = Color(0xDCF8B528),
            teachingBackground = R.drawable.teaching_background_banco,
            fillBlankBackground = R.drawable.teaching_background_banco,
            multipleChoiceBackground = R.drawable.teaching_background_banco,
            sentenceBuilderBackground = R.drawable.teaching_background_banco,
            matchingBackground = R.drawable.teaching_background_banco,
            dragPairsBackground = R.drawable.teaching_background_banco,
            categoryIcon = R.drawable.ic_pesito_original,
            CloseIcon = R.drawable.ic_close_banco,
            progressBar = R.drawable.ic_exp_bar_amarillo,
            pesitoFeliz = R.drawable.ic_pesito_original_feliz,
            pesitoTriste = R.drawable.ic_pesito_original_triste
        ),
        "inversion" to TemaVisual(
            baseColor = Color(0xFFED7621),
            gradientLight = Color(0xFFFFB987),
            gradientDark = Color(0xFFDD5500),
            progressColor = Color(0xDCED7621),
            teachingBackground = R.drawable.sentence_builder_background_inversiones,
            fillBlankBackground = R.drawable.sentence_builder_background_inversiones,
            multipleChoiceBackground = R.drawable.sentence_builder_background_inversiones,
            sentenceBuilderBackground = R.drawable.sentence_builder_background_inversiones,
            matchingBackground = R.drawable.sentence_builder_background_inversiones,
            dragPairsBackground = R.drawable.sentence_builder_background_inversiones,
            categoryIcon = R.drawable.ic_pesito_inversionista,
            CloseIcon = R.drawable.ic_close_inversiones,
            progressBar = R.drawable.ic_exp_bar_naranja,
            pesitoFeliz = R.drawable.ic_pesito_inversionista_feliz,
            pesitoTriste = R.drawable.ic_pesito_inversionista_triste
        ),
        "compra" to TemaVisual(
            baseColor = Color(0xFF0270BA),
            gradientLight = Color(0xFF479FDB),
            gradientDark = Color(0xFF003A61),
            progressColor = Color(0xDC0270BA),
            teachingBackground = R.drawable.matching_background_centro_comercial,
            fillBlankBackground = R.drawable.matching_background_centro_comercial,
            multipleChoiceBackground = R.drawable.matching_background_centro_comercial,
            sentenceBuilderBackground = R.drawable.matching_background_centro_comercial,
            matchingBackground = R.drawable.matching_background_centro_comercial,
            dragPairsBackground = R.drawable.matching_background_centro_comercial,
            categoryIcon = R.drawable.ic_pesito_comprador,
            CloseIcon = R.drawable.ic_close_centro_comercial,
            progressBar = R.drawable.ic_exp_bar_azul,
            pesitoFeliz = R.drawable.ic_pesito_comprador_feliz,
            pesitoTriste = R.drawable.ic_pesito_comprador_triste
        )
    )

    // Obtener el tema visual para una categoría dada
    fun obtenerTemaPorCategoria(categoria: String): TemaVisual? {
        return temasPorCategoria[categoria]
    }

}
