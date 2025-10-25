package com.universidad.finankids.ui.components

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.R
import com.universidad.finankids.events.BancoEvent
import com.universidad.finankids.viewmodel.BancoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankKeypad(
    bancoViewModel: BancoViewModel,
    modifier: Modifier = Modifier,
    onPinConfirmado: () -> Unit = {}
) {
    val state by bancoViewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var pin by remember { mutableStateOf("") }
    var confirmMode by remember { mutableStateOf(false) }
    var pinConfirm by remember { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val buttonWidth = screenWidth * 0.16f
    val buttonHeight = screenHeight * 0.08f
    val maxLength = 4
    val esRegistro = state.banco?.pin == null

    // --- Lógica Firebase igual ---
    LaunchedEffect(Unit) { bancoViewModel.onEvent(BancoEvent.CargarBanco) }
    LaunchedEffect(state.banco) {
        if (state.banco == null && !state.loading) {
            bancoViewModel.onEvent(BancoEvent.CrearBancoInicial)
        }
    }
    LaunchedEffect(state.errorMessage, state.mensaje) {
        val msg = state.errorMessage ?: state.mensaje
        msg?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it,
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
    LaunchedEffect(state.pinValidado) {
        if (state.pinValidado) {
            onPinConfirmado()
            bancoViewModel.onEvent(BancoEvent.ResetValidacion)
        }
    }

    // --- UI ---
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        // Fondo del keypad
        Image(
            painter = painterResource(id = R.drawable.ic_fondo_bank),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(screenWidth * 0.85f)
                .height(screenHeight * 0.75f)
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .width(screenWidth * 0.65f)
                .height(screenHeight * 0.7f)
                .offset(y = (screenHeight * 0.12f)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Texto superior ---
            Text(
                text = when {
                    confirmMode -> "      \nCONFIRMA TU CLAVE"
                    esRegistro -> "CREA TU CLAVE SECRETA\nDE 4 NÚMEROS"
                    else -> "INGRESA TU CLAVE\nPARA ACCEDER AL BANCO"
                },
                fontSize = (screenWidth.value * 0.045).sp,
                fontFamily = FontFamily(Font(R.font.baloo_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xFF7A2500),
                textAlign = TextAlign.Center,
                lineHeight = (screenWidth.value * 0.055).sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // --- Indicadores del PIN ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(maxLength) { index ->
                    val visible = index < pin.length
                    Text(
                        text = if (visible) "*" else "",
                        fontSize = 48.sp,
                        color = Color(0xFF6F2A00),
                        fontFamily = FontFamily(Font(R.font.baloo_regular)),
                        fontWeight = FontWeight(400),
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- Teclado numérico ---
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val filas = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("borrar", "0", "check")
                )

                filas.forEach { fila ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(screenWidth * 0.045f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        fila.forEach { icon ->
                            var clicked by remember { mutableStateOf(false) }

                            val scale by animateFloatAsState(
                                targetValue = if (clicked) 1.1f else 1f,
                                animationSpec = tween(durationMillis = 150),
                                finishedListener = { clicked = false }
                            )

                            Box(
                                modifier = Modifier
                                    .width(buttonWidth)
                                    .height(buttonHeight)
                                    .scale(scale)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null // 🔕 sin ripple
                                    ) {
                                        clicked = true
                                        when (icon) {
                                            "borrar" -> if (pin.isNotEmpty()) pin = pin.dropLast(1)
                                            "check" -> {
                                                if (pin.length == maxLength) {
                                                    if (esRegistro && !confirmMode) {
                                                        confirmMode = true
                                                        pinConfirm = pin
                                                        pin = ""
                                                    } else if (esRegistro && confirmMode) {
                                                        if (pin == pinConfirm)
                                                            bancoViewModel.onEvent(BancoEvent.RegistrarPin(pin))
                                                        else
                                                            bancoViewModel.onEvent(BancoEvent.Error("Los PIN no coinciden"))
                                                        confirmMode = false
                                                        pin = ""
                                                    } else {
                                                        bancoViewModel.onEvent(BancoEvent.ValidarPin(pin))
                                                        pin = ""
                                                    }
                                                }
                                            }
                                            else -> if (pin.length < maxLength) pin += icon
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(
                                        id = when (icon) {
                                            "borrar" -> R.drawable.ic_borrarb
                                            "check" -> R.drawable.ic_check
                                            "0" -> R.drawable.ic_number0
                                            "1" -> R.drawable.ic_number1
                                            "2" -> R.drawable.ic_number2
                                            "3" -> R.drawable.ic_number3
                                            "4" -> R.drawable.ic_number4
                                            "5" -> R.drawable.ic_number5
                                            "6" -> R.drawable.ic_number6
                                            "7" -> R.drawable.ic_number7
                                            "8" -> R.drawable.ic_number8
                                            "9" -> R.drawable.ic_number9
                                            else -> R.drawable.ic_number0
                                        }
                                    ),
                                    contentDescription = icon,
                                    modifier = Modifier.fillMaxSize(0.9f),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    }
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}


/*
@Preview()
@Composable
fun BankKeypadSmallPreview() {
    BankKeypad()
}
 */
