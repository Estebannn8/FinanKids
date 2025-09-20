package com.universidad.finankids.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.universidad.finankids.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChangeAvatarOverlay(
    startWithAvatars: Boolean = true,
    onDismiss: () -> Unit
) {
    // 🔹 Estado: pestaña activa (true = avatares, false = marcos)
    var isAvatarsTab by remember { mutableStateOf(startWithAvatars) }

    // 🔹 Estado simulado de selección
    var selectedAvatar by remember { mutableStateOf(0) }
    var selectedFrame by remember { mutableStateOf(0) }

    // 🔹 Lista de avatares (mock, 12 placeholders por ahora)
    val avatars = List(12) { R.drawable.ic_avatar_placeholder }

    // 🔹 Lista de marcos (8 fijos, predeterminados)
    val frames = listOf(
        R.drawable.ic_frame_amarillo,
        R.drawable.ic_frame_azul,
        R.drawable.ic_frame_morado,
        R.drawable.ic_frame_naranja,
        R.drawable.ic_frame_azulrey,
        R.drawable.ic_frame_placeholder, // gris
        R.drawable.ic_frame_rosado,
        R.drawable.ic_frame_cafe
    )

    // 🔹 Ancho de pantalla para tamaños proporcionales
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
            // 🔹 Avatar + Marco actual (dinámico)
            Box(
                modifier = Modifier.size(90.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = avatars[selectedAvatar]),
                    contentDescription = "Avatar del usuario",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = frames[selectedFrame]),
                    contentDescription = "Marco del avatar",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Fit
                )
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
                    avatars = avatars,
                    selectedAvatar = selectedAvatar,
                    onAvatarSelected = { selectedAvatar = it }
                )
            } else {
                MarcosGrid(
                    frames = frames,
                    selectedFrame = selectedFrame,
                    onFrameSelected = { selectedFrame = it }
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
    avatars: List<Int>,
    selectedAvatar: Int,
    onAvatarSelected: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 30.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(avatars.size) { index ->
            val avatarRes = avatars[index]

            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(4.dp)
                    .border(
                        width = if (index == selectedAvatar) 4.dp else 0.dp,
                        color = if (index == selectedAvatar) Color(0xFF53164F) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onAvatarSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = avatarRes),
                    contentDescription = "Avatar $index",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MarcosGrid(
    frames: List<Int>,
    selectedFrame: Int,
    onFrameSelected: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 30.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(frames.size) { index ->
            val frameRes = frames[index]

            // 🔹 Contenedor externo: borde de selección
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(4.dp)
                    .clickable { onFrameSelected(index) }
                    .then(
                        if (index == selectedFrame) {
                            Modifier
                                .border(
                                    width = 3.dp,
                                    color = Color(0xFF53164F)
                                )
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                // 🔹 Contenido interno: imagen del marco
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp), // espacio para que el borde quede afuera
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = frameRes),
                        contentDescription = "Marco $index",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
private fun ChangeAvatarOverlayDialogPreviewAvatares() {
    Dialog(onDismissRequest = {}) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            ChangeAvatarOverlay(
                startWithAvatars = true,
                onDismiss = {}
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
private fun ChangeAvatarOverlayDialogPreviewMarcos() {
    Dialog(onDismissRequest = {}) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            ChangeAvatarOverlay(
                startWithAvatars = false,
                onDismiss = {}
            )
        }
    }
}
