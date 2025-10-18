package com.universidad.finankids.ui

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
fun BankKeypad(navController: NavController) {
    var selectedItem by remember { mutableStateOf("banco") }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Tamaño base para cada "botón" del teclado
    val buttonWidth = screenWidth * 0.16f
    val buttonHeight = screenHeight * 0.085f

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Fondo naranja
        Image(
            painter = painterResource(id = R.drawable.ic_fondo_bank),
            contentDescription = "Fondo banco",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(screenWidth * 1.02f)
                .height(screenHeight * 1.05f)
                .offset(y = (-screenHeight * 0.08f))
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .width(screenWidth * 0.6f)
                .height(screenHeight * 0.72f)
                .offset(y = (-screenHeight * 0.06f)),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "CREA TU CLAVE SECRETA\nDE 4 NÚMEROS PARA ENTRAR AL BANCO",
                fontSize = (screenWidth.value * 0.045).sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF7A2500),
                textAlign = TextAlign.Center,
                lineHeight = (screenWidth.value * 0.055).sp,
                modifier = Modifier
                    .offset(y = (screenHeight * 0.03f))
                    .padding(horizontal = screenWidth * 0.02f)
            )

            Spacer(modifier = Modifier.height(screenHeight * 0.015f))

            // Teclado numérico
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(screenHeight * 0.015f)
            ) {
                val filas = listOf(
                    listOf(R.drawable.ic_number1, R.drawable.ic_number2, R.drawable.ic_number3),
                    listOf(R.drawable.ic_number4, R.drawable.ic_number5, R.drawable.ic_number6),
                    listOf(R.drawable.ic_number7, R.drawable.ic_number8, R.drawable.ic_number9)
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
                                    .height(buttonHeight),
                                contentAlignment = Alignment.Center
                            ) {
                                // Todos los números ahora iguales y alineados
                                Image(
                                    painter = painterResource(id = numero),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(0.95f),
                                    contentScale = ContentScale.Fit,
                                    alignment = Alignment.Center
                                )
                            }
                        }
                    }
                }

                // Última fila: borrar - 0 - check
                Row(
                    horizontalArrangement = Arrangement.spacedBy(screenWidth * 0.045f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val ultimaFila = listOf(R.drawable.ic_borrarb, R.drawable.ic_number0, R.drawable.ic_check)
                    ultimaFila.forEach { icon ->
                        Box(
                            modifier = Modifier
                                .width(buttonWidth)
                                .height(buttonHeight),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = icon),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(0.95f),
                                contentScale = ContentScale.Fit,
                                alignment = Alignment.Center
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
                .padding(bottom = screenHeight * 0.03f),
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

@Preview(showBackground = true, showSystemUi = true, name = "HomeBankScreen Preview")
@Composable
fun HomeBankScreenPreview() {
    val navController = rememberNavController()
    BankKeypad(navController)
}
