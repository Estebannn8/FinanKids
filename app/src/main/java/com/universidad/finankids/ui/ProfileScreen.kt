package com.universidad.finankids.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.universidad.finankids.R
import com.universidad.finankids.events.AvatarEvent
import com.universidad.finankids.events.UserEvent
import com.universidad.finankids.navigation.navigateToScreen
import com.universidad.finankids.ui.components.BottomMenu
import com.universidad.finankids.ui.components.ChangeAvatarOverlay
import com.universidad.finankids.ui.components.LoadingOverlay
import com.universidad.finankids.ui.theme.AppTypography
import com.universidad.finankids.viewmodel.AvataresViewModel
import com.universidad.finankids.viewmodel.UserViewModel


@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    avataresViewModel: AvataresViewModel
) {
    // Estados observables
    val userState by userViewModel.state.collectAsState()
    val avatarState by avataresViewModel.state.collectAsState()

    // Lanzar carga de datos al entrar a la pantalla
    LaunchedEffect(Unit) {
        val uid = userViewModel.state.value.userData.uid
        if (uid.isEmpty()) {
            // si aún no tenemos uid en el estado, lo pedimos desde AuthViewModel
            val currentUid = FirebaseAuth.getInstance().currentUser?.uid
            if (currentUid != null) {
                userViewModel.sendEvent(UserEvent.LoadUser(currentUid))
            }
        } else {
            userViewModel.sendEvent(UserEvent.LoadUser(uid))
        }

        avataresViewModel.sendEvent(AvatarEvent.LoadAllAvatars)
    }

    // Verificar si los datos están cargados
    val isDataLoaded = remember(userState, avatarState) {
        userState.userData.uid.isNotEmpty() && !userState.isLoading &&
                avatarState.currentAvatar != null && !avatarState.isLoading
    }

    // Mostrar loading si los datos no están listos
    if (!isDataLoaded) {
        LoadingOverlay()
    }

    var selectedItem by remember { mutableStateOf("perfil") }
    val sectionMenuColor = Color(0xFFC9CED6)

    // Overlay
    var showOverlay by remember { mutableStateOf(false) }
    var startWithAvatars by remember { mutableStateOf(true) }

    // Marcos locales (para mostrar el actual)
    val frames = listOf(
        "amarillo" to R.drawable.ic_frame_amarillo,
        "azul" to R.drawable.ic_frame_azul,
        "morado" to R.drawable.ic_frame_morado,
        "naranja" to R.drawable.ic_frame_naranja,
        "azulrey" to R.drawable.ic_frame_azulrey,
        "gris" to R.drawable.ic_frame_placeholder,
        "rosado" to R.drawable.ic_frame_rosado,
        "cafe" to R.drawable.ic_frame_cafe
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDCDEE2)),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //Title
                Text(
                    text = "MI PERFIL",
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 18.99.sp,
                        fontFamily = FontFamily(Font(R.font.baloo_regular)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF666666),
                        textAlign = TextAlign.Right,
                        letterSpacing = 3.07.sp,
                    )
                )

                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {

                        // Avatar + marco clickable para abrir overlay
                        var avatarBoxClicked by remember { mutableStateOf(false) }
                        val interactionSource = remember { MutableInteractionSource() }
                        val avatarBoxScale by animateFloatAsState(
                            targetValue = if (avatarBoxClicked) 1.1f else 1f, // 🔹 Zoom sutil
                            animationSpec = tween(durationMillis = 200),
                            finishedListener = { avatarBoxClicked = false }
                        )

                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .scale(avatarBoxScale)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    avatarBoxClicked = true
                                    startWithAvatars = true
                                    showOverlay = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            // --- Avatar ---
                            // --- Avatar actual dinámico ---
                            val currentAvatar = avatarState.avatarList.find { it.id == userState.userData.avatarActual }

                            if (currentAvatar?.imageUrl?.isNotEmpty() == true) {
                                Image(
                                    painter = rememberAsyncImagePainter(currentAvatar.imageUrl),
                                    contentDescription = "Avatar del usuario",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_avatar_placeholder),
                                    contentDescription = "Avatar predeterminado",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    contentScale = ContentScale.Inside
                                )
                            }


                            // --- Marco actual dinámico ---
                            val currentFrame = frames.find { it.first == userState.userData.marcoActual }
                            currentFrame?.let { (_, drawable) ->
                                Image(
                                    painter = painterResource(id = drawable),
                                    contentDescription = "Marco del avatar",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            }

                        }
                    }

                    // Nivel
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(32.dp)
                            .align(Alignment.BottomStart)
                            .offset(x = 114.dp, y = (-50).dp)
                            .zIndex(3f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_nivel_naranja),
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


                        // --- Titulo de nivel ---
                        Text(
                            text = "NOVATO AHORRADOR",
                            modifier = Modifier
                                .padding(start = 10.dp),
                            style = TextStyle(
                                fontSize = 15.sp,
                                lineHeight = 10.99.sp,
                                fontFamily = FontFamily(Font(R.font.baloo_regular)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF666666),
                                textAlign = TextAlign.Right,
                                letterSpacing = 2.07.sp,
                            )
                        )

                    }

                    // --- Nickname ---
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .offset(x = 114.dp, y = (-105).dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppTypography.ItimStroke(
                            text = userState.userData.nickname,   // <- Nickname
                            strokeColor = Color(0xFF666666),
                            fillColor = Color(0xFF666666),
                            fontSize = 21.sp,
                            textAlign = TextAlign.Start,
                            lineHeight = 1.sp,
                            letterSpacing = 1.sp
                        )

                    }


                    // --- Ajustes y Editar ---
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 0.dp, y = (-107).dp),
                    ) {

                        // --- Editar ---
                        Image(
                            painter = painterResource(id = R.drawable.ic_editar),
                            contentDescription = "Moneda",
                            modifier = Modifier.size(30.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        // --- Ajustes ---
                        Image(
                            painter = painterResource(id = R.drawable.ic_ajustes),
                            contentDescription = "Moneda",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                // --- INSIGNIAS ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "INSIGNIAS",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.baloo_regular)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFF666666),
                            letterSpacing = 4.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Imagen de fondo del recuadro
                        Image(
                            painter = painterResource(id = R.drawable.rectangulo_insignias),
                            contentDescription = "Fondo recuadro insignias",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                        )

                        // Fila de insignias encima de la imagen
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = 5.dp, vertical = 5.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_insignia_nivel9),
                                contentDescription = "Insignia Nivel 9",
                                modifier = Modifier.size(70.dp)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.ic_insignia_nivel8),
                                contentDescription = "Insignia Nivel 8",
                                modifier = Modifier.size(70.dp)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.ic_insignia_nivel4),
                                contentDescription = "Insignia Nivel 4",
                                modifier = Modifier.size(70.dp)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.ic_insignia_nivel3),
                                contentDescription = "Insignia Nivel 3",
                                modifier = Modifier.size(70.dp)
                            )
                        }
                    }
                }


                Spacer(modifier = Modifier.height(24.dp))

                // --- Estadisticas ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // Título Estadísticas
                    Text(
                        text = "ESTADÍSTICAS",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.baloo_regular)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFF666666),
                            letterSpacing = 4.sp
                        ),
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Fila de estadísticas
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf(
                            Triple(R.drawable.ic_xp, "${userState.userData.exp}", "TOTAL XP"),
                            Triple(R.drawable.ic_racha_normal, "1", "DIAS DE RACHA"),
                            Triple(R.drawable.ic_coin, "${userState.userData.dinero}", "PESITOS")
                        ).forEach { (icon, value, label) ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp)
                                    .height(70.dp)
                            ) {
                                // Fondo del recuadro
                                Image(
                                    painter = painterResource(id = R.drawable.rectangulo_estadisticas),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.matchParentSize()
                                )

                                // Contenido sobre el fondo
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(start = 5.dp, end = 5.dp)
                                ) {
                                    // Icono
                                    Image(
                                        painter = painterResource(id = icon),
                                        contentDescription = label,
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))

                                    // Contenido del valor y la etiqueta
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            Text(
                                                text = value,
                                                fontFamily = FontFamily(Font(R.font.baloo_regular)),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp,
                                                color = Color(0xFF444444),
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = label,
                                                fontFamily = FontFamily(Font(R.font.baloo_regular)),
                                                fontSize = 13.sp,
                                                color = Color(0xFF444444),
                                                lineHeight = 13.sp
                                            )
                                        }
                                    }

                                }
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.height(30.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val items = listOf(
                        Triple(
                            R.drawable.rectangulo_estadistica_amarillo,
                            R.drawable.ic_pesito_original,
                            listOf(R.drawable.ic_estrella_amarillo, R.drawable.ic_precision_amarillo)
                        ),
                        Triple(
                            R.drawable.rectangulo_estadistica_morado,
                            R.drawable.ic_pesito_ahorrador,
                            listOf(R.drawable.ic_estrella_morado, R.drawable.ic_precision_morado)
                        ),
                        Triple(
                            R.drawable.rectangulo_estadistica_naranja,
                            R.drawable.ic_pesito_inversionista,
                            listOf(R.drawable.ic_estrella_naranja, R.drawable.ic_precision_naranja)
                        ),
                        Triple(
                            R.drawable.rectangulo_estadistica_azul,
                            R.drawable.ic_pesito_comprador,
                            listOf(R.drawable.ic_estrella_azul, R.drawable.ic_precision_azul)
                        )
                    )

                    // Dividir en filas de 2 elementos
                    items.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            rowItems.forEach { (bgRes, pesitoIcon, icons) ->
                                val (estrellaIcon, precisionIcon) = icons

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(70.dp)
                                        .padding(start = 4.dp)
                                ) {
                                    // Fondo
                                    Image(
                                        painter = painterResource(id = bgRes),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(70.dp)
                                            .align(Alignment.Center)
                                            .zIndex(0f), // Fondo debajo
                                        contentScale = ContentScale.FillBounds
                                    )

                                    // Pesito sobresalido
                                    Image(
                                        painter = painterResource(id = pesitoIcon),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(150.dp)
                                            .align(Alignment.CenterStart)
                                            .offset(x = (-30).dp)
                                            .zIndex(1f)
                                    )

                                    Column(
                                    horizontalAlignment = Alignment.Start,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(end = 12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "1",
                                            color = Color.White,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily(Font(R.font.baloo_regular))
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Image(
                                            painter = painterResource(id = estrellaIcon),
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "80%",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontFamily = FontFamily(Font(R.font.baloo_regular))
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Image(
                                            painter = painterResource(id = precisionIcon),
                                            contentDescription = null,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }

                                }
                            }
                        }
                    }
                }

            }
        }

        BottomMenu(
            isHomeSection = false,
            sectionColor = "", // No se usa en esta pantalla
            menuBackgroundColor = sectionMenuColor,
            selectedItem = selectedItem,
            onItemSelected = { item ->
                selectedItem = item
                navigateToScreen(navController, item)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (showOverlay) {
            Dialog(onDismissRequest = { showOverlay = false }) {
                ChangeAvatarOverlay(
                    userViewModel = userViewModel,
                    avataresViewModel = avataresViewModel,
                    startWithAvatars = startWithAvatars,
                    onDismiss = { showOverlay = false }
                )
            }
        }

    }
}


