package com.universidad.finankids.ui

import android.content.Context
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.universidad.finankids.R
import com.universidad.finankids.data.sound.AppSound
import com.universidad.finankids.data.sound.SoundManager
import com.universidad.finankids.events.AchievementTrigger
import com.universidad.finankids.events.AchievementsEventBus
import com.universidad.finankids.events.AvatarEvent
import com.universidad.finankids.events.UserEvent
import com.universidad.finankids.navigation.AppScreens
import com.universidad.finankids.navigation.navigateToScreen
import com.universidad.finankids.ui.components.BottomMenu
import com.universidad.finankids.ui.components.ChangeAvatarOverlay
import com.universidad.finankids.ui.components.EditNameOverlay
import com.universidad.finankids.ui.components.LoadingOverlay
import com.universidad.finankids.ui.components.Settings
import com.universidad.finankids.ui.components.StreakCalendarOverlay
import com.universidad.finankids.ui.theme.AppTypography
import com.universidad.finankids.viewmodel.AchievementsViewModel
import com.universidad.finankids.viewmodel.AuthViewModel
import com.universidad.finankids.viewmodel.AvataresViewModel
import com.universidad.finankids.viewmodel.StreakViewModel
import com.universidad.finankids.viewmodel.UserSettingsViewModel
import com.universidad.finankids.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    authViewModel: AuthViewModel,
    avataresViewModel: AvataresViewModel,
    userSettingsViewModel: UserSettingsViewModel,
    streakViewModel: StreakViewModel,
    achievementsViewModel: AchievementsViewModel,
) {
    // Estados observables
    val userState by userViewModel.state.collectAsState()
    val avatarState by avataresViewModel.state.collectAsState()
    val userSettings by userSettingsViewModel.settings.collectAsState()
    val streakState by streakViewModel.streak.collectAsState()

    val context = LocalContext.current

    // Función para obtener el título según el nivel
    fun obtenerTituloNivel(nivel: Int): String {
        return when {
            nivel < 10 -> "Ahorrista"
            nivel < 20 -> "Explorador"
            nivel < 30 -> "Guardián del Oro"
            nivel < 40 -> "Maestro Cash"
            nivel < 50 -> "Leyenda $"
            else -> "Gran Sabio $"
        }
    }

    // Cargar settings y rachas cuando el usuario esté disponible
    LaunchedEffect(userState.userData.uid) {
        if (userState.userData.uid.isNotEmpty()) {
            userSettingsViewModel.loadUserSettings(userState.userData.uid)
            streakViewModel.loadUserStreak(userState.userData.uid)
            streakViewModel.checkAndResetStreakIfNeeded(userState.userData.uid)
        }
    }


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

    // Overlay para el calendario de rachas
    var showStreakCalendar by remember { mutableStateOf(false) }

    var selectedItem by remember { mutableStateOf("perfil") }
    val sectionMenuColor = Color(0xFFC9CED6)

    // Overlay
    var showOverlay by remember { mutableStateOf(false) }
    var startWithAvatars by remember { mutableStateOf(true) }

    var showEditName by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf(userState.userData.nickname) }

    var showSettings by remember { mutableStateOf(false) }
    var showConfirmLogout by remember { mutableStateOf(false) }

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

    // Cargar settings cuando el usuario esté disponible
    LaunchedEffect(userState.userData.uid) {
        if (userState.userData.uid.isNotEmpty()) {
            userSettingsViewModel.loadUserSettings(userState.userData.uid)
        }
    }

    // Convertir el color hexadecimal del settings a Color de Compose
    val backgroundColor = remember(userSettings.colorFondoPerfil) {
        try {
            Color(android.graphics.Color.parseColor(userSettings.colorFondoPerfil))
        } catch (e: Exception) {
            Color(0xFFDCDEE2) // Color por defecto si hay error
        }
    }

    LaunchedEffect(userState.userData.uid) {
        val uid = userState.userData.uid
        if (uid.isNotEmpty()) {
            val alreadyUnlocked = achievementsViewModel.isAchievementUnlocked(uid, "cientifico_dinero")

            if (!alreadyUnlocked) {
                AchievementsEventBus.emit(
                    AchievementTrigger.ProfileOpenedFirstTime(uid)
                )
            }
        }
    }

    // Función para obtener el valor de cada categoría
    fun obtenerValorCategoria(categoria: String): Int {
        return when (categoria) {
            "Ahorro" -> userState.userData.progresoCategorias["ahorro"] ?: 0
            "Centro Comercial" -> userState.userData.progresoCategorias["compra"] ?: 0
            "Banco" -> userState.userData.progresoCategorias["basica"] ?: 0
            "Inversiones" -> userState.userData.progresoCategorias["inversion"] ?: 0
            else -> 0
        }
    }

    fun getInsigniaDrawable(
        context: Context,
        categoria: String,
        progreso: Int
    ): Int {
        val baseName = when (categoria) {
            "Ahorro" -> "ic_insignia_ahorro"
            "Centro Comercial" -> "ic_insignia_compra"
            "Inversiones" -> "ic_insignia_inversion"
            "Banco" -> "ic_insignia_basica"
            else -> "ic_insignia_basica"
        }

        val nivel = ((progreso / 5) + 1).coerceAtMost(5)
        val fullName = "${baseName}_nivel$nivel"

        return context.resources.getIdentifier(fullName, "drawable", context.packageName)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(modifier = Modifier.height(56.dp))

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
                                .size(34.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_nivel_gris),
                                contentDescription = "Icono de nivel",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = "${userState.nivel}",  // <- Nivel
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }


                        // --- Titulo de nivel ---
                        Text(
                            text = obtenerTituloNivel(userState.nivel),
                            modifier = Modifier
                                .padding(start = 10.dp),
                            style = TextStyle(
                                fontSize = 20.sp,
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
                        var editClicked by remember { mutableStateOf(false) }
                        val editScale by animateFloatAsState(
                            targetValue = if (editClicked) 1.1f else 1f,
                            animationSpec = tween(durationMillis = 200),
                            finishedListener = { editClicked = false }
                        )

                        Image(
                            painter = painterResource(id = R.drawable.ic_editar),
                            contentDescription = "Editar",
                            modifier = Modifier
                                .size(35.dp)
                                .scale(editScale)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    SoundManager.play(AppSound.BUTTON)
                                    editClicked = true
                                    showEditName = true
                                }
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        // --- Ajustes ---
                        var settingsClicked by remember { mutableStateOf(false) }

                        val settingsScale by animateFloatAsState(
                            targetValue = if (settingsClicked) 1.1f else 1f,
                            animationSpec = tween(durationMillis = 200),
                            finishedListener = { settingsClicked = false }
                        )

                        Image(
                            painter = painterResource(id = R.drawable.ic_ajustes),
                            contentDescription = "settings",
                            modifier = Modifier
                                .size(35.dp)
                                .scale(settingsScale)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    SoundManager.play(AppSound.BUTTON)
                                    settingsClicked = true
                                    showSettings = true
                                }
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
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
                        ) {

                            listOf(
                                "Basica",
                                "Ahorro",
                                "Centro Comercial",
                                "Inversiones"
                            ).forEach { categoria ->

                                val progreso = obtenerValorCategoria(categoria)
                                val resId = getInsigniaDrawable(context, categoria, progreso)

                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = "Insignia $categoria",
                                    modifier = Modifier.size(70.dp)
                                )
                            }
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
                            Triple(
                                if (streakViewModel.isStreakActive()) R.drawable.ic_racha_normal else R.drawable.ic_racha_apagada,
                                "${streakState.currentStreak}",
                                "DIAS DE RACHA"
                            ),
                            Triple(R.drawable.ic_coin, "${userState.userData.dinero}", "PESITOS")
                        ).forEach { (icon, value, label) ->
                            var boxClicked by remember { mutableStateOf(false) }
                            val boxScale by animateFloatAsState(
                                targetValue = if (boxClicked) 1.05f else 1f,
                                animationSpec = tween(durationMillis = 200),
                                finishedListener = { boxClicked = false }
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp)
                                    .height(70.dp)
                                    .scale(boxScale)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        SoundManager.play(AppSound.BUTTON)
                                        if (label == "DIAS DE RACHA") {
                                            boxClicked = true
                                            showStreakCalendar = true
                                        }
                                    }
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
                    // Data class para representar cada categoría
                    data class CategoryItem(
                        val backgroundRes: Int,
                        val pesitoIcon: Int,
                        val icons: List<Int>,
                        val categoryName: String
                    )

                    val categoryItems = listOf(
                        CategoryItem(
                            R.drawable.rectangulo_estadistica_amarillo,
                            R.drawable.ic_pesito_original,
                            listOf(R.drawable.ic_estrella_amarillo, R.drawable.ic_precision_amarillo),
                            "Banco"
                        ),
                        CategoryItem(
                            R.drawable.rectangulo_estadistica_morado,
                            R.drawable.ic_pesito_ahorrador,
                            listOf(R.drawable.ic_estrella_morado, R.drawable.ic_precision_morado),
                            "Ahorro"
                        ),
                        CategoryItem(
                            R.drawable.rectangulo_estadistica_naranja,
                            R.drawable.ic_pesito_inversionista,
                            listOf(R.drawable.ic_estrella_naranja, R.drawable.ic_precision_naranja),
                            "Inversiones"
                        ),
                        CategoryItem(
                            R.drawable.rectangulo_estadistica_azul,
                            R.drawable.ic_pesito_comprador,
                            listOf(R.drawable.ic_estrella_azul, R.drawable.ic_precision_azul),
                            "Centro Comercial"
                        )
                    )

                    // Dividir en filas de 2 elementos
                    categoryItems.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            rowItems.forEach { categoryItem ->
                                val (estrellaIcon, _) = categoryItem.icons
                                val valorCategoria = obtenerValorCategoria(categoryItem.categoryName)

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(70.dp)
                                        .padding(start = 4.dp)
                                ) {
                                    // Fondo
                                    Image(
                                        painter = painterResource(id = categoryItem.backgroundRes),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(70.dp)
                                            .align(Alignment.Center)
                                            .zIndex(0f),
                                        contentScale = ContentScale.FillBounds
                                    )

                                    // Pesito sobresalido
                                    Image(
                                        painter = painterResource(id = categoryItem.pesitoIcon),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(150.dp)
                                            .align(Alignment.CenterStart)
                                            .offset(x = (-30).dp)
                                            .zIndex(1f)
                                    )

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(end = 12.dp)
                                            .offset(x = (-10).dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "$valorCategoria",
                                                color = Color.White,
                                                fontSize = 25.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily(Font(R.font.baloo_regular))
                                            )
                                            Spacer(modifier = Modifier.width(9.dp))
                                            Image(
                                                painter = painterResource(id = estrellaIcon),
                                                contentDescription = null,
                                                modifier = Modifier.size(23.dp).offset(y = (-4).dp)
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
            Dialog(onDismissRequest = {
                showOverlay = false }) {
                ChangeAvatarOverlay(
                    userViewModel = userViewModel,
                    avataresViewModel = avataresViewModel,
                    startWithAvatars = startWithAvatars,
                    onDismiss = { showOverlay = false }
                )
            }
        }

        var triedToSave by remember { mutableStateOf(false) }

        if (showEditName) {
            Dialog(onDismissRequest = { showEditName = false }) {
                EditNameOverlay(
                    text = newName,
                    onValueChange = {
                        SoundManager.play(AppSound.KEYBOARD)
                        newName = it
                        userViewModel.clearError()
                    },
                    onDismiss = { showEditName = false },
                    onSave = {
                        SoundManager.play(AppSound.BUTTON)
                        triedToSave = true
                        userViewModel.sendEvent(UserEvent.ChangeNickname(newName))
                    },
                    userViewModel = userViewModel
                )
            }

            val userState by userViewModel.state.collectAsState()
            LaunchedEffect(userState.userData.nickname, userState.errorMessage, triedToSave) {
                if (triedToSave && userState.errorMessage == null && userState.userData.nickname == newName) {
                    showEditName = false
                    triedToSave = false // reset para el siguiente uso
                }
            }
        }

        if (showSettings) {
            Dialog(onDismissRequest = { showSettings = false }) {
                Settings(
                    onToggleMusica = {
                        SoundManager.play(AppSound.BUTTON)
                        userSettingsViewModel.toggleMusica(userState.userData.uid)
                    },
                    onToggleSonido = {
                        SoundManager.play(AppSound.BUTTON)
                        userSettingsViewModel.toggleSonido(userState.userData.uid)
                    },
                    onChangeFondoPerfil = { colorHex ->
                        SoundManager.play(AppSound.BUTTON)
                        userSettingsViewModel.updateBackgroundColor(userState.userData.uid, colorHex)
                    },
                    onLogout = {
                        showConfirmLogout = true
                    },
                    userSettings = userSettings
                )
            }
        }

        if (showConfirmLogout) {
            AlertDialog(
                onDismissRequest = { showConfirmLogout = false },
                title = { Text("Cerrar sesión") },
                text = { Text("¿Seguro que deseas cerrar sesión?") },
                confirmButton = {
                    val context = LocalContext.current

                    TextButton(onClick = {
                        SoundManager.play(AppSound.BUTTON)
                        showConfirmLogout = false
                        showSettings = false

                        authViewModel.logout(context)
                        userViewModel.clearState()
                        avataresViewModel.clearState()
                        userSettingsViewModel.clearState()
                        streakViewModel.clearState()

                        navController.navigate(AppScreens.MainScreen.route) {
                            popUpTo(AppScreens.HomeScreen.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }) {
                        SoundManager.play(AppSound.BUTTON)
                        Text("Sí")
                    }

                },
                dismissButton = {
                    TextButton(onClick = {
                        SoundManager.play(AppSound.BUTTON)
                        showConfirmLogout = false
                    }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // Calendario de Rachas
        if (showStreakCalendar) {
            StreakCalendarOverlay(
                streak = streakState,
                onDismiss = { showStreakCalendar = false }
            )
        }


    }
}


