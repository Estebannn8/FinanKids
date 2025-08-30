package com.universidad.finankids.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.universidad.finankids.R
import com.universidad.finankids.navigation.navigateToScreen
import com.universidad.finankids.ui.components.BottomMenu

@Composable
fun ProgressScreen(navController: NavController) {
    var selectedItem by remember { mutableStateOf("trofeo") }

    val sectionBackgroundColor = Color(0xFFC9CED6)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE01313)), // Fondo general
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Contenedor que ocupa todo el espacio restante
        Column(
            modifier = Modifier.weight(1f) // Toma todo el espacio disponible
        ) {

            // verde
            Box(
                modifier = Modifier
                    .background(Color(0xFF13E04D))
                    .fillMaxWidth()
                    .height(50.dp)
            ){

            }

            Spacer(modifier = Modifier.height(10.dp))

            // azul
            Box(
                modifier = Modifier
                    .background(Color(0xFF143B65))
                    .fillMaxWidth()
                    .height(50.dp)
            ){

            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .background(Color(0xFFEFFAFC))
                    .fillMaxWidth()
                    .height(500.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .background(Color(0xFF00BCD4))
                        .fillMaxWidth()
                        .height(150.dp),
                    horizontalArrangement = Arrangement.Center, // Centrado horizontalmente
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0x549C27B0))
                            .width(100.dp)
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                            .background(Color(0xFF9C27B0))
                            .width(90.dp)
                            .height(90.dp)
                        ){
                            Reward()
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp)) // Espacio entre el primer y segundo Box
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF673AB7))
                            .width(100.dp)
                            .height(100.dp)
                    ) {
                        // Contenido del segundo Box
                    }
                    Spacer(modifier = Modifier.width(16.dp)) // Espacio entre el segundo y tercer Box
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF3F51B5))
                            .width(100.dp)
                            .height(100.dp)
                    ) {
                        // Contenido del tercer Box
                    }
                }


                Row(
                    modifier = Modifier
                        .background(Color(0xA800BCD4))
                        .fillMaxWidth()
                        .height(150.dp),
                    horizontalArrangement = Arrangement.Center, // Centrado horizontalmente
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF9C27B0))
                            .width(100.dp)
                            .height(100.dp)
                    ) {
                        // Contenido del primer Box
                    }
                    Spacer(modifier = Modifier.width(16.dp)) // Espacio entre el primer y segundo Box
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF673AB7))
                            .width(100.dp)
                            .height(100.dp)
                    ) {
                        // Contenido del segundo Box
                    }
                    Spacer(modifier = Modifier.width(16.dp)) // Espacio entre el segundo y tercer Box
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF3F51B5))
                            .width(100.dp)
                            .height(100.dp)
                    ) {
                        // Contenido del tercer Box
                    }
                }


                Row(
                    modifier = Modifier
                        .background(Color(0xFF03A9F4))
                        .fillMaxWidth()
                        .height(150.dp),
                    horizontalArrangement = Arrangement.Center, // Centrado horizontalmente
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF9C27B0))
                            .width(100.dp)
                            .height(100.dp)
                    ) {
                        // Contenido del primer Box
                    }
                    Spacer(modifier = Modifier.width(16.dp)) // Espacio entre el primer y segundo Box
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF673AB7))
                            .width(100.dp)
                            .height(100.dp)
                    ) {
                        // Contenido del segundo Box
                    }
                    Spacer(modifier = Modifier.width(16.dp)) // Espacio entre el segundo y tercer Box
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF3F51B5))
                            .width(100.dp)
                            .height(100.dp)
                    ) {
                        // Contenido del tercer Box
                    }
                }
            }






            // Aquí va tu contenido de arriba
        }

        // Menú en la parte inferior
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
    }

}


@Composable
fun Reward (){
    Column (
        modifier = Modifier
            .width(90.dp)
            .height(90.dp)
    ){
        Box(
            modifier = Modifier
                .background(Color(0xFFB77F3A))
                .fillMaxSize()
        ) {  }
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressScreenPreview() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalContext provides LocalContext.current) {
        ProgressScreen(navController = navController)
    }
}

@Preview
@Composable
fun RewardPreview(){
    Reward()
}
