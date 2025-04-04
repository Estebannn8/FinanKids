package com.universidad.finankids.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen() {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(
                WindowInsets.statusBars.asPaddingValues()) // ← esto deja espacio arriba para la barra de estado
    ) {
        // Encabezado
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.14f)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .background(color = Color.Black)
        ) {
        }

        // Sección media
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.56f)
                .align(Alignment.TopCenter)
                .padding(top = screenHeight * 0.14f) // mismo alto del encabezado
                .background(color = Color(0xFFfae4a7))
                .padding(horizontal = 20.dp, vertical = 15.dp)
        ) {
        }

        // Botones al final
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.29f)
                .align(Alignment.BottomCenter)
                .padding(15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomButton(
                buttonText = "REGISTRARSE",
                gradientLight = Color(0xFF9C749A),
                gradientDark = Color(0xFF431441),
                baseColor = Color(0xFF53164F),
                onClick = { /* Acción */ }
            )
            Spacer(modifier = Modifier.height(12.dp))
            CustomButton(
                buttonText = "INICIAR SESIÓN",
                gradientLight = Color(0xFF9C749A),
                gradientDark = Color(0xFF431441),
                baseColor = Color(0xFF53164F),
                onClick = { /* Acción */ }
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "MainScreen Preview",
)
@Composable
fun MainScreenPreview() {
    // Simular el insets manualmente (ya que no se aplican bien en preview)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp) // Simula espacio para la status bar
    ) {
        MainScreen()
    }
}
