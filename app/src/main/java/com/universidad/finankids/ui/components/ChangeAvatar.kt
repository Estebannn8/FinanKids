package com.universidad.finankids.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.universidad.finankids.R
import com.universidad.finankids.data.model.Avatar
import com.universidad.finankids.events.UserEvent
import com.universidad.finankids.viewmodel.AvataresViewModel
import com.universidad.finankids.viewmodel.UserViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChangeAvatarOverlay(
    userViewModel: UserViewModel,
    avataresViewModel: AvataresViewModel,
    startWithAvatars: Boolean = true,
    onDismiss: () -> Unit
) {
    val userState by userViewModel.state.collectAsState()
    val avatarState by avataresViewModel.state.collectAsState()

    var isAvatarsTab by remember { mutableStateOf(startWithAvatars) }

    // 🔹 Estado actual desde Firestore
    var selectedAvatar by remember { mutableStateOf(userState.userData.avatarActual) }
    var selectedFrame by remember { mutableStateOf(userState.userData.marcoActual) }

    // 🔹 Avatares desbloqueados del usuario
    val unlockedAvatars = avatarState.avatarList.filter {
        userState.userData.avataresDesbloqueados.contains(it.id)
    }

    // 🔹 Marcos locales (ID = clave, drawable = recurso)
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

    // Ancho de pantalla para tamaños proporcionales
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val buttonSizeActive = screenWidth * 0.42f   // 32% del ancho
    val buttonSizeInactive = screenWidth * 0.32f // 25% del ancho

    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .size(500.dp),
        contentAlignment = Alignment.Center
    ) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.background_menu_avatars),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Avatar + Marco actual
            Box(
                modifier = Modifier.size(90.dp),
                contentAlignment = Alignment.Center
            ) {
                // Avatar actual
                val currentAvatar = unlockedAvatars.find { it.id == selectedAvatar }
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
                        contentScale = ContentScale.Crop
                    )
                }

                // Marco actual
                val currentFrame = frames.find { it.first == selectedFrame }
                currentFrame?.let { (_, drawable) ->
                    Image(
                        painter = painterResource(id = drawable),
                        contentDescription = "Marco actual",
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 Título dinámico
            Text(
                text = if (isAvatarsTab) "MIS AVATARES" else "MIS MARCOS",
                fontFamily = FontFamily(Font(R.font.baloo_regular)),
                fontSize = 22.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(5.dp))

            // 🔹 Contenido dinámico
            if (isAvatarsTab) {
                AvataresGrid(
                    avatars = unlockedAvatars,
                    selectedAvatar = selectedAvatar,
                    onAvatarSelected = { avatarId ->
                        selectedAvatar = avatarId
                        userViewModel.sendEvent(UserEvent.ChangeAvatar(avatarId))
                    }
                )
            } else {
                MarcosGrid(
                    frames = frames,
                    selectedFrame = selectedFrame,
                    onFrameSelected = { marcoId ->
                        selectedFrame = marcoId
                        userViewModel.sendEvent(UserEvent.ChangeMarco(marcoId))
                    }
                )
            }
        }

        // Botón avatares
        var avatarsClicked by remember { mutableStateOf(false) }
        val avatarsInteractionSource = remember { MutableInteractionSource() }
        val avatarsScale by animateFloatAsState(
            targetValue = if (avatarsClicked) 1.1f else 1f,
            animationSpec = tween(durationMillis = 200),
            finishedListener = { avatarsClicked = false }
        )

        Image(
            painter = painterResource(
                id = if (isAvatarsTab) R.drawable.avatares_active else R.drawable.avatares_inactive
            ),
            contentDescription = "Mostrar avatares",
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = if (isAvatarsTab) (-70).dp else (-50).dp)
                .size(if (isAvatarsTab) buttonSizeActive else buttonSizeInactive)
                .scale(avatarsScale)
                .clickable(
                    interactionSource = avatarsInteractionSource,
                    indication = null
                ) {
                    avatarsClicked = true
                    isAvatarsTab = true
                }
        )

        // Botón marcos
        var framesClicked by remember { mutableStateOf(false) }
        val framesInteractionSource = remember { MutableInteractionSource() }
        val framesScale by animateFloatAsState(
            targetValue = if (framesClicked) 1.1f else 1f,
            animationSpec = tween(durationMillis = 200),
            finishedListener = { framesClicked = false }
        )

        Image(
            painter = painterResource(
                id = if (!isAvatarsTab) R.drawable.marcos_active else R.drawable.marcos_inactive
            ),
            contentDescription = "Mostrar marcos",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(y = if (!isAvatarsTab) (-70).dp else (-50).dp)
                .size(if (!isAvatarsTab) buttonSizeActive else buttonSizeInactive)
                .scale(framesScale)
                .clickable(
                    interactionSource = framesInteractionSource,
                    indication = null
                ) {
                    framesClicked = true
                    isAvatarsTab = false
                }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AvataresGrid(
    avatars: List<Avatar>,
    selectedAvatar: String,
    onAvatarSelected: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 30.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(avatars.size) { index ->
            val avatar = avatars[index]
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(4.dp)
                    .border(
                        width = if (avatar.id == selectedAvatar) 4.dp else 0.dp,
                        color = if (avatar.id == selectedAvatar) Color(0xFF53164F) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onAvatarSelected(avatar.id) },
                contentAlignment = Alignment.Center
            ) {
                if (avatar.imageUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(avatar.imageUrl),
                        contentDescription = "Avatar ${avatar.id}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_avatar_placeholder),
                        contentDescription = "Avatar placeholder",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MarcosGrid(
    frames: List<Pair<String, Int>>,
    selectedFrame: String,
    onFrameSelected: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 30.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(frames.size) { index ->
            val (marcoId, frameRes) = frames[index]
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(4.dp)
                    .clickable { onFrameSelected(marcoId) }
                    .then(
                        if (marcoId == selectedFrame) {
                            Modifier.border(width = 3.dp, color = Color(0xFF53164F))
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = frameRes),
                    contentDescription = "Marco $marcoId",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

