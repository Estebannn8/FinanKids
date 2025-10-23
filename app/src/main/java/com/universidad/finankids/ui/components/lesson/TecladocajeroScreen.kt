package com.universidad.finankids.ui.components.lesson

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.universidad.finankids.R
import com.universidad.finankids.navigation.navigateToScreen
import com.universidad.finankids.ui.components.BottomMenu

@Composable
fun TecladocajeroScreen(navController: NavController) {
    var selectedItem by remember { mutableStateOf("banco") }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val buttonWidth = screenWidth * 0.12f
    val buttonHeight = screenHeight * 0.07f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = screenHeight * 0.1f), // Espacio para el menú inferior
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

            // Contenido interno del cajero (textos, teclado, botones)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = screenHeight * 0.095f,   // margen superior interno
                        bottom = screenHeight * 0.08f, // margen inferior interno
                        start = screenWidth * 0.1f,   // margen izquierdo interno
                        end = screenWidth * 0.1f      // margen derecho interno
                    ),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ======== TEXTOS SUPERIORES ========
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ESCRIBE LA CANTIDAD DE\nPESITOS QUE QUIERES USAR",
                        color = Color(0xFF5B2C00),
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        fontSize = (screenWidth.value * 0.04).sp,
                        lineHeight = (screenWidth.value * 0.05).sp
                    )
                    Spacer(modifier = Modifier.height(screenHeight * 0.005f))
                    Text(
                        text = "MENSAJE DE ERROR",
                        color = Color(0xFFD33333),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = (screenWidth.value * 0.035).sp
                    )
                    Spacer(modifier = Modifier.height(screenHeight * 0.05f))
                    Image(
                        painter = painterResource(id = R.drawable.ic_barra),
                        contentDescription = "Barra superior",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width(screenWidth * 0.70f)
                            .height(screenHeight * 0.08f)
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
                        verticalArrangement = Arrangement.spacedBy(screenHeight * 0.0f)
                    ) {
                        val filas = listOf(
                            listOf(
                                R.drawable.ic_number1,
                                R.drawable.ic_number2,
                                R.drawable.ic_number3
                            ),
                            listOf(
                                R.drawable.ic_number4,
                                R.drawable.ic_number5,
                                R.drawable.ic_number6
                            ),
                            listOf(
                                R.drawable.ic_number7,
                                R.drawable.ic_number8,
                                R.drawable.ic_number9
                            ),
                            listOf(
                                R.drawable.ic_tecla_vacia,
                                R.drawable.ic_number0,
                                R.drawable.ic_tecla_vacia
                            )
                        )

                        filas.forEach { fila ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(screenWidth * 0.01f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                fila.forEach { icono ->
                                    Box(
                                        modifier = Modifier
                                            .width(buttonWidth)
                                            .height(buttonHeight),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = icono),
                                            contentDescription = null,
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
                        verticalArrangement = Arrangement.spacedBy(screenHeight * 0.011f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val botonesLaterales = listOf(
                            R.drawable.ic_boton_borrar,
                            R.drawable.ic_boton_retirar,
                            R.drawable.ic_boton_depositar
                        )
                        botonesLaterales.forEach { boton ->
                            Image(
                                painter = painterResource(id = boton),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .width(screenWidth * 0.25f)
                                    .height(screenHeight * 0.08f)
                            )
                        }
                    }
                }
            }
        }

// Menú inferior
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 0.dp), // 👈 ahora toca el borde inferior real
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BottomMenu(
                isHomeSection = false,
                sectionColor = "",
                menuBackgroundColor = Color(0xFFC9CED6),
                selectedItem = selectedItem,
                onItemSelected = { item ->
                    selectedItem = item
                    navigateToScreen(navController, item)
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TecladocajeroScreenPreview() {
    val navController = rememberNavController()
    TecladocajeroScreen(navController)
}
