package com.universidad.finankids.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.universidad.finankids.R
import com.universidad.finankids.navigation.navigateToScreen
import com.universidad.finankids.ui.Components.BottomMenu

@Composable
fun ProgressScreen(navController: NavController) {
    var selectedItem by remember { mutableStateOf("trofeo") }

    val sectionBackgroundColor = Color(0xFFC9CED6)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDCDEE2)), // Fondo general
        contentAlignment = Alignment.Center
    ) {
        // Imagen de fondo con padding y sin recorte
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.home_progreso),
                contentDescription = null,
                modifier = Modifier
                    .width(500.dp) // Cambia aquí el tamaño como quieras
                    .height(800.dp),
                contentScale = ContentScale.Crop // No recorta la imagen
            )
        }

        // Menú inferior
        Column(
            modifier = Modifier
                .fillMaxSize(),
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
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProgressScreenPreview() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalContext provides LocalContext.current) {
        ProgressScreen(navController = navController)
    }
}
