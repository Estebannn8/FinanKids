package com.universidad.finankids.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(navController: NavController) {
    val sections = listOf(
        Section("Ahorro", Color(0xFF746474), Color(0xFF9D759B), "morado"),
        Section("Centro Comercial", Color(0xFF79AFD4), Color(0xFFAECDE3), "azul"),
        Section("Banco", Color(0xFFE4C78A), Color(0xFFF1DCB3), "amarillo"),
        Section("Inversiones", Color(0xFFB28F76), Color(0xFFFFBA88), "naranja")
    )

    var currentSectionIndex by remember { mutableStateOf(0) }
    val section = sections[currentSectionIndex]

    val navIconNames = listOf("perfil", "trofeo", "inicio", "progreso", "tienda")

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

        Text(
            text = section.name,
            fontSize = 26.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(
                    color = section.menuBackgroundColor,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(vertical = 12.dp, horizontal = 32.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                navIconNames.forEach { iconName ->
                    val iconResId = getIconRes("ic_${section.color}_${iconName}")
                    IconButton(
                        onClick = { /* TODO: Handle navigation */ },
                        modifier = Modifier.size(60.dp)
                    ) {
                        if (iconResId != 0) {
                            Image(
                                painter = painterResource(id = iconResId),
                                contentDescription = iconName,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun getIconRes(name: String): Int {
    val context = LocalContext.current
    return remember(name) {
        context.resources.getIdentifier(name, "drawable", context.packageName)
    }
}

data class Section(
    val name: String,
    val backgroundColor: Color,
    val menuBackgroundColor: Color,
    val color: String
)

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun HomeScreenPreview() {
    // Crear un NavController mock para el preview
    val navController = rememberNavController()

    // Proporcionar un contexto para el preview
    CompositionLocalProvider(LocalContext provides LocalContext.current) {
        HomeScreen(navController = navController)
    }
}