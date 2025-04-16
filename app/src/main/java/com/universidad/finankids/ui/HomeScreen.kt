package com.universidad.finankids.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.universidad.finankids.R
import com.universidad.finankids.navigation.navigateToScreen
import com.universidad.finankids.ui.Components.BottomMenu

@Composable
fun HomeScreen(navController: NavController) {
    val sections = listOf(
        Section(
            "Ahorro",
            Color(0xFF746474),
            Color(0xFF9D759B),
            "morado",
            R.drawable.edificio_ahorro,
            R.drawable.texto_ahorro,
            20.dp, 55.dp,
            R.drawable.ic_morado_arrow_left,
            R.drawable.ic_morado_arrow_right,
            R.drawable.ic_jugar_morado,
            R.drawable.ic_estrella_morado
        ),
        Section(
            "Centro Comercial",
            Color(0xFF79AFD4),
            Color(0xFFAECDE3),
            "azul",
            R.drawable.edificio_centro_comercial,
            R.drawable.texto_centro_comercial,
            20.dp, 55.dp,
            R.drawable.ic_azul_arrow_left,
            R.drawable.ic_azul_arrow_right,
            R.drawable.ic_jugar_azul,
            R.drawable.ic_estrella_azul
        ),
        Section(
            "Banco",
            Color(0xFFE4C78A),
            Color(0xFFF1DCB3),
            "amarillo",
            R.drawable.edificio_banco,
            R.drawable.texto_banco,
            20.dp, 40.dp,
            R.drawable.ic_amarillo_arrow_left,
            R.drawable.ic_amarillo_arrow_right,
            R.drawable.ic_jugar_amarillo,
            R.drawable.ic_estrella_amarillo
        ),
        Section(
            "Inversiones",
            Color(0xFFB28F76),
            Color(0xFFFFBA88),
            "naranja",
            R.drawable.edificio_inversiones,
            R.drawable.texto_inversiones,
            25.dp, 55.dp,
            R.drawable.ic_naranja_arrow_left,
            R.drawable.ic_naranja_arrow_right,
            R.drawable.ic_jugar_naranja,
            R.drawable.ic_estrella_naranja
        )
    )

    var currentSectionIndex by remember { mutableStateOf(0) }
    var selectedItem by remember { mutableStateOf("inicio") }
    val section = sections[currentSectionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(section.backgroundColor)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    when {
                        dragAmount < -50 -> currentSectionIndex = (currentSectionIndex + 1) % sections.size
                        dragAmount > 50 -> currentSectionIndex = (currentSectionIndex - 1 + sections.size) % sections.size
                    }
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Espacio reservado para el header
        Spacer(modifier = Modifier.height(200.dp))

        // Contenido principal (sección media + puntuación)
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Sección Media
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    var buildingClicked by remember { mutableStateOf(false) }
                    val buildingScale by animateFloatAsState(
                        targetValue = if (buildingClicked) 1.05f else 1f,
                        animationSpec = tween(durationMillis = 200),
                        finishedListener = { buildingClicked = false }
                    )

                    Box(
                        modifier = Modifier
                            .size(500.dp)
                            .padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = section.buildingImage),
                            contentDescription = "Edificio de ${section.name}",
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(buildingScale)
                                .clickable {
                                    buildingClicked = true
                                },
                            contentScale = ContentScale.Fit
                        )

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(bottom = 10.dp, start = 10.dp)
                                .align(Alignment.BottomStart)
                                .clickable {
                                    currentSectionIndex = (currentSectionIndex - 1 + sections.size) % sections.size
                                }
                        ) {
                            var leftArrowClicked by remember { mutableStateOf(false) }
                            val leftArrowScale by animateFloatAsState(
                                targetValue = if (leftArrowClicked) 1.2f else 1f,
                                animationSpec = tween(durationMillis = 200),
                                finishedListener = { leftArrowClicked = false }
                            )

                            Image(
                                painter = painterResource(id = section.leftArrowIcon),
                                contentDescription = "Flecha izquierda",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .scale(leftArrowScale)
                                    .clickable {
                                        leftArrowClicked = true
                                        currentSectionIndex = (currentSectionIndex - 1 + sections.size) % sections.size
                                    },
                                contentScale = ContentScale.Fit
                            )
                        }

                        Image(
                            painter = painterResource(id = section.textImage),
                            contentDescription = "Texto de ${section.name}",
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = section.textBottomPadding)
                                .size(width = 300.dp, height = section.textHeight),
                            contentScale = ContentScale.Fit
                        )

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(bottom = 10.dp, end = 10.dp)
                                .align(Alignment.BottomEnd)
                                .clickable {
                                    currentSectionIndex = (currentSectionIndex + 1) % sections.size
                                }
                        ) {
                            var rightArrowClicked by remember { mutableStateOf(false) }
                            val rightArrowScale by animateFloatAsState(
                                targetValue = if (rightArrowClicked) 1.2f else 1f,
                                animationSpec = tween(durationMillis = 200),
                                finishedListener = { rightArrowClicked = false }
                            )

                            Image(
                                painter = painterResource(id = section.rightArrowIcon),
                                contentDescription = "Flecha derecha",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .scale(rightArrowScale)
                                    .clickable {
                                        rightArrowClicked = true
                                        currentSectionIndex = (currentSectionIndex + 1) % sections.size
                                    },
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }

                // Boton jugar y Puntuacion
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    // Puntuación y estrella
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        val score = remember { mutableStateOf(100) } //Cambiar

                        Text(
                            text = score.value.toString(),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 8.dp).offset(y = 2.dp)
                        )

                        Image(
                            painter = painterResource(id = section.starIcon),
                            contentDescription = "Estrella",
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    // Botón de jugar
                    var playButtonClicked by remember { mutableStateOf(false) }
                    val playButtonScale by animateFloatAsState(
                        targetValue = if (playButtonClicked) 1.1f else 1f,
                        animationSpec = tween(durationMillis = 200),
                        finishedListener = { playButtonClicked = false }
                    )

                    Image(
                        painter = painterResource(id = section.playButtonIcon),
                        contentDescription = "Jugar",
                        modifier = Modifier
                            .scale(playButtonScale)
                            .clickable {
                                playButtonClicked = true
                            },
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        // Bottom Menu
        BottomMenu(
            isHomeSection = true,
            sectionColor = section.color,
            menuBackgroundColor = section.menuBackgroundColor,
            selectedItem = selectedItem,
            onItemSelected = { item ->
                selectedItem = item
                navigateToScreen(navController, item)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

data class Section(
    val name: String,
    val backgroundColor: Color,
    val menuBackgroundColor: Color,
    val color: String,
    val buildingImage: Int,
    val textImage: Int,
    val textBottomPadding: Dp,
    val textHeight: Dp,
    val leftArrowIcon: Int,
    val rightArrowIcon: Int,
    val playButtonIcon: Int,
    val starIcon: Int
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalContext provides LocalContext.current) {
        HomeScreen(navController = navController)
    }
}