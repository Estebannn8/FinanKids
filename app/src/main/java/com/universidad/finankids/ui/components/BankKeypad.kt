package com.universidad.finankids.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
    val buttonHeight = screenHeight * 0.085f
    val maxLength = 4

    val esRegistro = state.banco?.pin == null

    // Cargar o crear banco al iniciar
    LaunchedEffect(Unit) {
        bancoViewModel.onEvent(BancoEvent.CargarBanco)
    }

    LaunchedEffect(state.banco) {
        if (state.banco == null && !state.loading) {
            bancoViewModel.onEvent(BancoEvent.CrearBancoInicial)
        }
    }

    // Mostrar mensajes en Snackbar
    LaunchedEffect(state.errorMessage, state.mensaje) {
        state.errorMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it,
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
            }
        }
        state.mensaje?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it,
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    // Cuando el PIN se valida correctamente
    LaunchedEffect(state.pinValidado) {
        if (state.pinValidado) {
            onPinConfirmado()
            bancoViewModel.onEvent(BancoEvent.ResetValidacion)
        }
    }

    fun agregarDigito(digito: String) {
        if (pin.length < maxLength) {
            pin += digito
        }
    }

    fun borrarDigito() {
        if (pin.isNotEmpty()) {
            pin = pin.dropLast(1)
        }
    }

    fun confirmarPin() {
        if (pin.length < maxLength) return

        if (esRegistro && !confirmMode) {
            confirmMode = true
            pinConfirm = pin
            pin = ""
        } else if (esRegistro && confirmMode) {
            if (pin == pinConfirm) {
                bancoViewModel.onEvent(BancoEvent.RegistrarPin(pin))
            } else {
                bancoViewModel.onEvent(BancoEvent.Error("Los PIN no coinciden"))
            }
            confirmMode = false
            pin = ""
        } else {
            bancoViewModel.onEvent(BancoEvent.ValidarPin(pin))
            pin = ""
        }
    }

    // --- UI ---
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Fondo del teclado
        Image(
            painter = painterResource(id = R.drawable.ic_fondo_bank),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(screenWidth * 1.02f)
                .height(screenHeight * 1.05f)
        )

        AnimatedVisibility(
            visible = true,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .width(screenWidth * 0.6f)
                    .height(screenHeight * 0.72f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(screenHeight * 0.045f))

                Text(
                    text = when {
                        confirmMode -> "CONFIRMA TU CLAVE"
                        esRegistro -> "CREA TU CLAVE SECRETA\nDE 4 NÚMEROS"
                        else -> "INGRESA TU CLAVE\nPARA ACCEDER AL BANCO"
                    },
                    fontSize = (screenWidth.value * 0.045).sp,
                    fontFamily = FontFamily(Font(R.font.baloo_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF7A2500),
                    textAlign = TextAlign.Center,
                    lineHeight = (screenWidth.value * 0.055).sp,
                    modifier = Modifier.padding(horizontal = screenWidth * 0.02f)
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.04f))

                // Indicadores del PIN
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(maxLength) { index ->
                        val visible = index < pin.length
                        Text(
                            text = if (visible) "•" else "○",
                            fontSize = 64.sp,
                            color = Color(0xFF6F2A00),
                            fontFamily = FontFamily(Font(R.font.baloo_regular))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(screenHeight * 0.01f))

                // Teclado numérico
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val filas = listOf(
                        listOf("1", "2", "3"),
                        listOf("4", "5", "6"),
                        listOf("7", "8", "9")
                    )

                    filas.forEach { fila ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(screenWidth * 0.045f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            fila.forEach { numero ->
                                Box(
                                    modifier = Modifier
                                        .width(buttonWidth)
                                        .height(buttonHeight)
                                        .clickable { agregarDigito(numero) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(
                                            id = when (numero) {
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
                                        contentDescription = numero,
                                        modifier = Modifier.fillMaxSize(0.95f),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            }
                        }
                    }

                    // Última fila
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(screenWidth * 0.045f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf("borrar", "0", "check").forEach { icon ->
                            Box(
                                modifier = Modifier
                                    .width(buttonWidth)
                                    .height(buttonHeight)
                                    .clickable {
                                        when (icon) {
                                            "borrar" -> borrarDigito()
                                            "0" -> agregarDigito("0")
                                            "check" -> confirmarPin()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(
                                        id = when (icon) {
                                            "borrar" -> R.drawable.ic_borrarb
                                            "check" -> R.drawable.ic_check
                                            else -> R.drawable.ic_number0
                                        }
                                    ),
                                    contentDescription = icon,
                                    modifier = Modifier.fillMaxSize(0.95f),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    }
                }
            }
        }

        // Snackbar animado
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = if (data.visuals.message.contains("incorrecto", true) ||
                    data.visuals.message.contains("error", true)
                ) Color(0xFFD32F2F) else Color(0xFF388E3C),
                contentColor = Color.White
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
