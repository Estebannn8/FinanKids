package com.universidad.finankids.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.R

@Composable
fun BankKeyboard(
    modifier: Modifier = Modifier,
    onKeyClick: (String) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // === ESCALA RESPONSIVA ===
    val scaleFactor = when {
        screenHeight < 550.dp -> 0.8f
        screenHeight < 700.dp -> 0.9f
        else -> 1f
    }

    val buttonWidth = screenWidth * 0.12f * scaleFactor
    val buttonHeight = screenHeight * 0.07f * scaleFactor
    val verticalSpacing = if (scaleFactor < 1f) screenHeight * 0.015f else screenHeight * 0.03f

    val balooFont = FontFamily(Font(R.font.baloo_regular))

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = screenHeight * 0.1f),
        contentAlignment = Alignment.Center
    ) {
        // Caja principal del cajero
        Box(
            modifier = Modifier
                .width(screenWidth * 0.98f)
                .height(screenHeight * 0.65f),
            contentAlignment = Alignment.Center
        ) {
            // Fondo beige del cajero
            Image(
                painter = painterResource(id = R.drawable.ic_bank_beigh),
                contentDescription = "Fondo cajero",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            // Contenido interno
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
                // ======== TEXTOS SUPERIORES ========
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ESCRIBE LA CANTIDAD DE\nPESITOS QUE QUIERES USAR",
                        color = Color(0xFF5B2C00),
                        fontFamily = balooFont,
                        fontWeight = FontWeight(400),
                        textAlign = TextAlign.Center,
                        fontSize = (screenWidth.value * 0.04 * scaleFactor).sp,
                        lineHeight = (screenWidth.value * 0.05 * scaleFactor).sp
                    )
                    Spacer(modifier = Modifier.height(screenHeight * 0.003f))
                    Text(
                        text = "MENSAJE DE ERROR",
                        color = Color(0xFFD33333),
                        fontFamily = balooFont,
                        fontWeight = FontWeight(400),
                        textAlign = TextAlign.Center,
                        fontSize = (screenWidth.value * 0.035 * scaleFactor).sp
                    )
                    Spacer(modifier = Modifier.height(screenHeight * 0.01f)) // Espacio reducido
                    Image(
                        painter = painterResource(id = R.drawable.ic_barra),
                        contentDescription = "Barra superior",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width(screenWidth * 0.70f)
                            .height(screenHeight * 0.08f * scaleFactor)
                    )
                }

                // ======== TECLADO Y BOTONES ========
                Row(
                    horizontalArrangement = Arrangement.spacedBy(screenWidth * 0.04f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // ----- Teclado numérico -----
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
                                Pair(R.drawable.ic_tecla_vacia, ""),  // mismo tamaño
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
                                    Box(
                                        modifier = Modifier
                                            .width(buttonWidth)
                                            .height(buttonHeight)
                                            .clickable(enabled = true) {
                                                // TODO: Aquí va la acción al hacer clic (número o tecla vacía)
                                                onKeyClick(value)
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

                    // ----- Botones laterales -----
                    Column(
                        verticalArrangement = Arrangement.spacedBy(screenHeight * 0.011f * scaleFactor),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val botonesLaterales = listOf(
                            Pair(R.drawable.ic_boton_borrar, "BORRAR"),
                            Pair(R.drawable.ic_boton_retirar, "RETIRAR"),
                            Pair(R.drawable.ic_boton_depositar, "DEPOSITAR")
                        )
                        botonesLaterales.forEach { (boton, action) ->
                            Image(
                                painter = painterResource(id = boton),
                                contentDescription = action,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .width(screenWidth * 0.25f)
                                    .height(screenHeight * 0.08f * scaleFactor)
                                    .clickable {
                                        // TODO: Aquí va la acción del botón lateral (borrar, retirar, depositar)
                                        onKeyClick(action)
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}

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
