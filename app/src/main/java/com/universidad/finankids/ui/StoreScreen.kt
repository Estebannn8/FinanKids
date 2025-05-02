package com.universidad.finankids.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
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
import com.universidad.finankids.R // Asegúrate de tener la imagen en res/drawable
import com.universidad.finankids.navigation.navigateToScreen
import com.universidad.finankids.ui.Components.BottomMenu

@Composable
fun StoreScreen(navController: NavController) {
    var selectedItem by remember { mutableStateOf("tienda") }

    val sectionBackgroundColor = Color(0xFFC9CED6)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF7AAFD4)),
        contentAlignment = Alignment.Center
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.home_tienda), // Reemplaza con tu nombre de imagen
            contentDescription = null,
            modifier = Modifier
                .width(500.dp) // Cambia aquí el tamaño como quieras
                .height(800.dp),
            contentScale = ContentScale.Fit
        )

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
fun StoreScreenPreview() {
    val navController = rememberNavController()
    StoreScreen(navController = navController)
}
