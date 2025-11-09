package com.universidad.finankids.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.universidad.finankids.R
import com.universidad.finankids.data.sound.AppSound
import com.universidad.finankids.data.sound.SoundManager
import com.universidad.finankids.events.BancoEvent
import com.universidad.finankids.viewmodel.BancoViewModel
import com.universidad.finankids.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BankKeyboard(
    bancoViewModel: BancoViewModel,
    userViewModel: UserViewModel,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by bancoViewModel.state.collectAsState()
    val userState by userViewModel.state.collectAsState()

    var montoInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val scaleFactor = when {
        screenHeight < 550.dp -> 0.8f
        screenHeight < 700.dp -> 0.9f
        else -> 1f
    }

    val buttonWidth = screenWidth * 0.12f * scaleFactor
    val buttonHeight = screenHeight * 0.07f * scaleFactor
    val balooFont = FontFamily(Font(R.font.baloo_regular))

    val scope = rememberCoroutineScope()

    // --- Escucha de errores y mensajes ---
    LaunchedEffect(state.errorOperacion) {
        state.errorOperacion?.let { error ->
            if (error != errorMessage) errorMessage = error
        }
    }

    LaunchedEffect(state.mensajeOperacion) {
        state.mensajeOperacion?.let {
            bancoViewModel.onEvent(BancoEvent.LimpiarMensajeOperacion)
            FirebaseAuth.getInstance().currentUser?.uid?.let { uid ->
                userViewModel.loadUserData(uid)
            }
            onClose()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = screenHeight * 0.1f),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(screenWidth * 0.98f)
                .height(screenHeight * 0.65f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_bank_beigh),
                contentDescription = "Fondo cajero",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = screenHeight * 0.1f,
                        bottom = screenHeight * 0.08f,
                        start = screenWidth * 0.1f,
                        end = screenWidth * 0.1f
                    ),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ===== TEXTOS SUPERIORES =====
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ESCRIBE LA CANTIDAD DE\nPESITOS QUE QUIERES USAR",
                        color = Color(0xFF5B2C00),
                        fontFamily = balooFont,
                        textAlign = TextAlign.Center,
                        fontSize = (screenWidth.value * 0.05 * scaleFactor).sp,
                        lineHeight = (screenWidth.value * 0.05 * scaleFactor).sp
                    )
                    Spacer(modifier = Modifier.height(screenHeight * 0.003f))

                    Text(
                        text = errorMessage ?: "",
                        color = Color(0xFFD33333),
                        fontFamily = balooFont,
                        textAlign = TextAlign.Center,
                        fontSize = (screenWidth.value * 0.035 * scaleFactor).sp,
                        modifier = Modifier.height(screenHeight * 0.03f)
                    )

                    Spacer(modifier = Modifier.height(screenHeight * 0.01f))

                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_barra),
                            contentDescription = "Barra superior",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(screenWidth * 0.70f)
                                .height(screenHeight * 0.08f * scaleFactor)
                        )
                        Text(
                            text = if (montoInput.isEmpty()) "0" else montoInput,
                            color = Color(0xFF5B2C00),
                            fontFamily = balooFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = (screenWidth.value * 0.06 * scaleFactor).sp
                        )
                    }
                }

                // ===== TECLADO Y BOTONES =====
                Row(
                    horizontalArrangement = Arrangement.spacedBy(screenWidth * 0.04f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // --- Teclado numérico ---
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(screenHeight * 0.005f * scaleFactor)
                    ) {
                        val filas = listOf(
                            listOf(
                                Pair(R.drawable.ic_number1, "1"),
                                Pair(R.drawable.ic_number2, "2"),
                                Pair(R.drawable.ic_number3, "3")
                            ),
                            listOf(
                                Pair(R.drawable.ic_number4, "4"),
                                Pair(R.drawable.ic_number5, "5"),
                                Pair(R.drawable.ic_number6, "6")
                            ),
                            listOf(
                                Pair(R.drawable.ic_number7, "7"),
                                Pair(R.drawable.ic_number8, "8"),
                                Pair(R.drawable.ic_number9, "9")
                            ),
                            listOf(
                                Pair(R.drawable.ic_tecla_vacia, ""),
                                Pair(R.drawable.ic_number0, "0"),
                                Pair(R.drawable.ic_tecla_vacia, "")
                            )
                        )

                        filas.forEach { fila ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(screenWidth * 0.01f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                fila.forEach { (icono, value) ->
                                    var pressed by remember { mutableStateOf(false) }
                                    val scale by animateFloatAsState(
                                        targetValue = if (pressed) 0.9f else 1f,
                                        animationSpec = tween(80),
                                        label = "keyScale"
                                    )

                                    Box(
                                        modifier = Modifier
                                            .width(buttonWidth)
                                            .height(buttonHeight)
                                            .graphicsLayer(
                                                scaleX = scale,
                                                scaleY = scale
                                            )
                                            .clickable(
                                                enabled = value.isNotEmpty(),
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                SoundManager.play(AppSound.CAJERO)

                                                if (value.isNotEmpty()) {
                                                    scope.launch {
                                                        pressed = true
                                                        delay(80)
                                                        pressed = false
                                                    }

                                                    if (errorMessage != null) {
                                                        errorMessage = null
                                                        bancoViewModel.onEvent(BancoEvent.LimpiarMensajeOperacion)
                                                    }

                                                    montoInput += value
                                                    if (montoInput.length > 9) {
                                                        SoundManager.play(AppSound.ERROR)
                                                        montoInput = montoInput.dropLast(1)
                                                        errorMessage = "Monto máximo excedido"
                                                    }
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = icono),
                                            contentDescription = value,
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier.fillMaxSize(0.95f)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // --- Botones laterales ---
                    Column(
                        verticalArrangement = Arrangement.spacedBy(screenHeight * 0.011f * scaleFactor),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // BORRAR
                        Image(
                            painter = painterResource(id = R.drawable.ic_boton_borrar),
                            contentDescription = "BORRAR",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(screenWidth * 0.25f)
                                .height(screenHeight * 0.08f * scaleFactor)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    SoundManager.play(AppSound.CAJERO)
                                    montoInput = ""
                                    errorMessage = null
                                    bancoViewModel.onEvent(BancoEvent.LimpiarMensajeOperacion)
                                }
                        )

                        // RETIRAR
                        Image(
                            painter = painterResource(id = R.drawable.ic_boton_retirar),
                            contentDescription = "RETIRAR",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(screenWidth * 0.25f)
                                .height(screenHeight * 0.08f * scaleFactor)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    SoundManager.play(AppSound.CAJERO)

                                    errorMessage = null
                                    bancoViewModel.onEvent(BancoEvent.LimpiarMensajeOperacion)

                                    if (montoInput.isEmpty() || montoInput == "0") {
                                        SoundManager.play(AppSound.ERROR)
                                        errorMessage = "Ingresa un monto válido"
                                        return@clickable
                                    }

                                    val monto = montoInput.toIntOrNull()
                                    if (monto == null || monto <= 0) {
                                        SoundManager.play(AppSound.ERROR)
                                        errorMessage = "Monto inválido"
                                        return@clickable
                                    }

                                    val saldoBanco = state.banco?.saldo ?: 0
                                    if (monto > saldoBanco) {
                                        SoundManager.play(AppSound.ERROR)
                                        errorMessage = "Saldo insuficiente en el banco"
                                        return@clickable
                                    }
                                    SoundManager.play(AppSound.TRANSACCION)
                                    bancoViewModel.onEvent(BancoEvent.Retirar(monto))
                                }
                        )

                        // DEPOSITAR
                        Image(
                            painter = painterResource(id = R.drawable.ic_boton_depositar),
                            contentDescription = "DEPOSITAR",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(screenWidth * 0.25f)
                                .height(screenHeight * 0.08f * scaleFactor)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {

                                    SoundManager.play(AppSound.CAJERO)

                                    errorMessage = null
                                    bancoViewModel.onEvent(BancoEvent.LimpiarMensajeOperacion)

                                    if (montoInput.isEmpty() || montoInput == "0") {
                                        SoundManager.play(AppSound.ERROR)
                                        errorMessage = "Ingresa un monto válido"
                                        return@clickable
                                    }

                                    val monto = montoInput.toIntOrNull()
                                    if (monto == null || monto <= 0) {
                                        SoundManager.play(AppSound.ERROR)
                                        errorMessage = "Monto inválido"
                                        return@clickable
                                    }

                                    val dineroUsuario = userState.userData.dinero
                                    if (monto > dineroUsuario) {
                                        SoundManager.play(AppSound.ERROR)
                                        errorMessage = "No tienes suficiente dinero"
                                        return@clickable
                                    }
                                    SoundManager.play(AppSound.TRANSACCION)
                                    bancoViewModel.onEvent(BancoEvent.Depositar(monto))
                                }
                        )
                    }
                }
            }
        }
    }
}

/*
/* ---------------------------------------
   PREVIEWS RESPONSIVE
   --------------------------------------- */

@Preview(name = "Phone Small - 320x480", widthDp = 320, heightDp = 550, showBackground = true)
@Composable
fun BankKeyboardPreview_Small() {
    BankKeyboard()
}

@Preview(name = "Phone Normal - 360x640", widthDp = 360, heightDp = 640, showBackground = true)
@Composable
fun BankKeyboardPreview_Normal() {
    BankKeyboard()
}

@Preview(name = "Phone Tall - 412x915", widthDp = 412, heightDp = 800, showBackground = true)
@Composable
fun BankKeyboardPreview_Tall() {
    BankKeyboard()
}

 */
