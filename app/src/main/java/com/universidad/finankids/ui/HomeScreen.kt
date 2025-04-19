package com.universidad.finankids.ui

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.universidad.finankids.R
import com.universidad.finankids.navigation.navigateToScreen
import com.universidad.finankids.ui.Components.BottomMenu
import com.universidad.finankids.ui.theme.AppTypography
import com.universidad.finankids.viewmodel.UserViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {

    val userState by userViewModel.state.collectAsState()

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

    LaunchedEffect(userState) {
        Log.d("HomeScreen", "Estado actual del usuario: $userState")
        Log.d("HomeScreen", "UID: ${userState.uid}")
        Log.d("HomeScreen", "Nickname: ${userState.nickname}")
        Log.d("HomeScreen", "Nivel: ${userState.nivel}")
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(section.backgroundColor)
            .padding(WindowInsets.statusBars.asPaddingValues())
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

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            // --- Avatar y Nivel ---
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier.size(70.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Avatar
                    Image(
                        painter = painterResource(id = R.drawable.ic_avatar_placeholder),
                        contentDescription = "Avatar del usuario",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .offset(y = 2.dp),
                        contentScale = ContentScale.Inside
                    )

                    // Marco
                    val frameRes = when (section.color) {
                        "azul" -> R.drawable.ic_frame_azul
                        "morado" -> R.drawable.ic_frame_morado
                        "amarillo" -> R.drawable.ic_frame_amarillo
                        "naranja" -> R.drawable.ic_frame_naranja
                        else -> R.drawable.ic_frame_azul
                    }

                    Image(
                        painter = painterResource(id = frameRes),
                        contentDescription = "Marco del avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                    // Nivel
                    val levelIconRes = when (section.color) {
                        "azul" -> R.drawable.ic_nivel_azul
                        "morado" -> R.drawable.ic_nivel_morado
                        "amarillo" -> R.drawable.ic_nivel_amarillo
                        "naranja" -> R.drawable.ic_nivel_naranja
                        else -> R.drawable.ic_nivel_azul
                    }
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.BottomCenter)
                            .offset(y = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = levelIconRes),
                            contentDescription = "Icono de nivel",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = "${userState.nivel}",  // <- Nivel
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // --- Barra de experiencia ---
            val expBarRes = when (section.color) {
                "azul" -> R.drawable.ic_exp_bar_azul
                "morado" -> R.drawable.ic_exp_bar_morado
                "amarillo" -> R.drawable.ic_exp_bar_amarillo
                "naranja" -> R.drawable.ic_exp_bar_naranja
                else -> R.drawable.ic_exp_bar_azul
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(32.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = 80.dp, y = (-30).dp) // Ajusta según tu diseño
                    .zIndex(2f) // Encima del quest
            ) {
                Image(
                    painter = painterResource(id = expBarRes),
                    contentDescription = "Barra de experiencia",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }

            // --- Nickname y Dinero ---
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.BottomStart)
                    .offset(x = 88.dp, y = (-68).dp) // Justo encima de la barra
                    .zIndex(3f), // Encima de todo
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppTypography.ItimStroke(
                    text = userState.nickname,   // <- Nickname
                    strokeColor = Color.White,
                    fillColor = Color.White,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Start,
                    lineHeight = 1.sp,
                    letterSpacing = 1.sp
                )


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 20.dp)
                ) {
                    Text(
                        text = "${userState.dinero}", // Dinero actual
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_coin),
                        contentDescription = "Moneda",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // --- Daily Quest ---
            var questClicked by remember { mutableStateOf(false) }
            val questScale by animateFloatAsState(
                targetValue = if (questClicked) 1.2f else 1f,
                animationSpec = tween(durationMillis = 200),
                finishedListener = { questClicked = false }
            )

            val questFrameRes = when (section.color) {
                "azul" -> R.drawable.ic_quest_frame_azul
                "morado" -> R.drawable.ic_quest_frame_morado
                "amarillo" -> R.drawable.ic_quest_frame_amarillo
                "naranja" -> R.drawable.ic_quest_frame_naranja
                else -> R.drawable.ic_quest_frame_azul
            }

            Box(
                modifier = Modifier
                    .size(55.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = (-60).dp, y = 15.dp)
                    .zIndex(1f) // Debajo de la barra de experiencia
                    .clickable {
                        questClicked = true
                        // Evento de clic
                    }
            ) {
                Image(
                    painter = painterResource(id = questFrameRes),
                    contentDescription = "Marco de Daily Quest",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_quest),
                    contentDescription = "Icono de Daily Quest",
                    modifier = Modifier
                        .size(45.dp)
                        .align(Alignment.Center)
                        .scale(questScale),
                    contentScale = ContentScale.Fit
                )
            }
        }


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