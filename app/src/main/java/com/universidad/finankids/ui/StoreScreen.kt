package com.universidad.finankids.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.universidad.finankids.R
import com.universidad.finankids.data.model.Avatar
import com.universidad.finankids.events.AvatarEvent
import com.universidad.finankids.events.UserEvent
import com.universidad.finankids.navigation.navigateToScreen
import com.universidad.finankids.ui.components.BottomMenu
import com.universidad.finankids.ui.components.BuyConfirmation
import com.universidad.finankids.viewmodel.AvataresViewModel
import com.universidad.finankids.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun StoreScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    avataresViewModel: AvataresViewModel
) {
    var selectedItem by remember { mutableStateOf("tienda") }
    val sectionBackgroundColor = Color(0xFFC9CED6)

    val userState by userViewModel.state.collectAsState()
    val avatarState by avataresViewModel.state.collectAsState()

    var selectedAvatar by remember { mutableStateOf<Avatar?>(null) }

    // cargar avatares
    LaunchedEffect(Unit) {
        avataresViewModel.sendEvent(AvatarEvent.LoadAllAvatars)
    }

    val dineroUsuario = userState.userData.dinero
    val avataresDesbloqueados = userState.userData.avataresDesbloqueados
    val avataresDisponibles = avatarState.avatarList.filter { it.id !in avataresDesbloqueados }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7AAFD4)) // fondo azul claro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(56.dp))

            // Title
            Text(
                text = "PESIMARKET",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.baloo_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFFFFFFF),
                    letterSpacing = 3.07.sp,
                )
            )

            // Dinero actual
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Row(
                    modifier = Modifier
                        .background(Color(0xFFB3D9F2), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_pesito_moneda),
                        contentDescription = "Pesito Moneda",
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dineroUsuario.toString(),
                            color = Color(0xFF111344),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "PESITOS",
                            color = Color(0xFF111344),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.pesito_azul_tienda),
                    contentDescription = "Pesito Azul Tienda",
                    modifier = Modifier.size(250.dp, 150.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Recuadro Grid con avatares disponibles
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(450.dp)
                    .background(Color(0xFF023B62), RoundedCornerShape(20.dp))
            ) {
                val scroll = rememberScrollState()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scroll)
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 32.dp,   // 🔹 Más espacio arriba
                            bottom = 40.dp // 🔹 Más espacio abajo
                        ),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    val rows = (avataresDisponibles.size + 2) / 3
                    repeat(rows) { rowIndex ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            repeat(3) { colIndex ->
                                val index = rowIndex * 3 + colIndex
                                if (index < avataresDisponibles.size) {
                                    val avatar = avataresDisponibles[index]
                                    AvatarItem(
                                        avatar = avatar,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(4.dp),
                                        onClick = { selectedAvatar = avatar }
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_boton_avatares_tienda),
                    contentDescription = "Botón Avatares",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = 20.dp, y = (-20).dp)
                        .size(width = 160.dp, height = 50.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        // Menú inferior
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BottomMenu(
                isHomeSection = false,
                sectionColor = "",
                menuBackgroundColor = sectionBackgroundColor,
                selectedItem = selectedItem,
                onItemSelected = { item ->
                    selectedItem = item
                    navigateToScreen(navController, item)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // 🔹 Diálogo de compra
        if (selectedAvatar != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x8B000000))
                    .clickable { selectedAvatar = null },
                contentAlignment = Alignment.Center
            ) {
                BuyConfirmation(
                    precio = selectedAvatar?.price ?: 0,
                    errorMessage = userState.errorMessage,
                    onBuy = {
                        // enviamos el evento, pero no cerramos todavía
                        userViewModel.sendEvent(UserEvent.BuyAvatar(selectedAvatar!!))
                    },
                    onCancel = {
                        // limpiar error y cerrar
                        userViewModel.clearError()
                        selectedAvatar = null
                    }
                )
            }
        }

        // 👇 cerrar automáticamente si la compra fue exitosa
        LaunchedEffect(userState.userData.avataresDesbloqueados) {
            if (selectedAvatar != null && selectedAvatar!!.id in userState.userData.avataresDesbloqueados) {
                selectedAvatar = null
                userViewModel.clearError()
            }
        }
    }
}

@Composable
fun AvatarItem(
    avatar: Avatar,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    // Estado de animación
    var clicked by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (clicked) 1.1f else 1f,
        animationSpec = tween(durationMillis = 200),
        finishedListener = { clicked = false }
    )

    // Lanzar acción después de la animación
    LaunchedEffect(clicked) {
        if (clicked) {
            delay(200)
            onClick()
        }
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                clicked = true
            },
        contentAlignment = Alignment.Center
    ) {
        // Imagen del avatar
        AsyncImage(
            model = avatar.imageUrl,
            contentDescription = avatar.name,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_avatar_placeholder),
            error = painterResource(id = R.drawable.ic_avatar_placeholder)
        )

        // Badge inferior con precio
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 8.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pesito_moneda),
                    contentDescription = "Pesito Moneda Pequeño",
                    modifier = Modifier.size(16.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = avatar.price?.toString() ?: "0",
                    color = Color(0xFFFF9505),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}



