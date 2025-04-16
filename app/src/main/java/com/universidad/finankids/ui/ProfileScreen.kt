package com.universidad.finankids.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.universidad.finankids.navigation.navigateToScreen
import com.universidad.finankids.ui.Components.BottomMenu

@Composable
fun ProfileScreen(navController: NavController) {
    var selectedItem by remember { mutableStateOf("perfil") }

    val sectionMenuColor = Color(0xFFC9CED6)

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Spacer(modifier = Modifier.height(24.dp))

        // Contenido principal del perfil
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Mi Perfil",
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                // Aquí iría el contenido real del perfil
            }
        }

        BottomMenu(
            isHomeSection = false,
            sectionColor = "", // No se usa en esta pantalla
            menuBackgroundColor = sectionMenuColor,
            selectedItem = selectedItem,
            onItemSelected = { item ->
                selectedItem = item
                navigateToScreen(navController, item)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalContext provides LocalContext.current) {
        ProfileScreen(navController = navController)
    }
}