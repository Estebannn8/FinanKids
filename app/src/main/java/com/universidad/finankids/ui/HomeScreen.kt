package com.universidad.finankids.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
fun HomeScreen(navController: NavController) {
    val sections = listOf(
        Section(
            "Ahorro",
            Color(0xFF746474),
            Color(0xFF9D759B),
            "morado",
            R.drawable.edificio_ahorro
        ),
        Section(
            "Centro Comercial",
            Color(0xFF79AFD4),
            Color(0xFFAECDE3),
            "azul",
            R.drawable.edificio_centro_comercial
        ),
        Section(
            "Banco",
            Color(0xFFE4C78A),
            Color(0xFFF1DCB3),
            "amarillo",
            R.drawable.edificio_banco
        ),
        Section(
            "Inversiones",
            Color(0xFFB28F76),
            Color(0xFFFFBA88),
            "naranja",
            R.drawable.edificio_inversiones
        )
    )

    var currentSectionIndex by remember { mutableStateOf(0) }
    var selectedItem by remember { mutableStateOf("inicio") }
    val section = sections[currentSectionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(section.backgroundColor)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    when {
                        dragAmount < -50 && currentSectionIndex < sections.lastIndex -> currentSectionIndex++
                        dragAmount > 50 && currentSectionIndex > 0 -> currentSectionIndex--
                    }
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = section.buildingImage),
                contentDescription = "Edificio de ${section.name}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        BottomMenu(
            isHomeSection = true,
            sectionColor = section.color,
            menuBackgroundColor = section.menuBackgroundColor,
            selectedItem = selectedItem,
            onItemSelected = { item ->
                selectedItem = item
                navigateToScreen(navController, item)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

data class Section(
    val name: String,
    val backgroundColor: Color,
    val menuBackgroundColor: Color,
    val color: String,
    val buildingImage: Int
)


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalContext provides LocalContext.current) {
        HomeScreen(navController = navController)
    }
}