package com.universidad.finankids.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.universidad.finankids.navigation.navigateToScreen
import com.universidad.finankids.ui.components.BottomMenu

@Composable
fun StoreScreen(navController: NavController) {
    var selectedItem by remember { mutableStateOf("tienda") }
    val sectionBackgroundColor = Color(0xFFC9CED6)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7AAFD4)) // fondo azul claro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 84.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // Título
            Text(
                text = "PESIMARKET",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Fila con recuadro e imagen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Monedas Pesitos
                Row(
                    modifier = Modifier
                        .background(Color(0xFFB3D9F2), RoundedCornerShape(16.dp)) // azul claro
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // Imagen Pesito
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFFFD966), RoundedCornerShape(50)), // ejemplo moneda
                        contentAlignment = Alignment.Center
                    ) {
                        Text("P", color = Color(0xFF163259), fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Textos en columna
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "300",
                            color = Color(0xFF111344),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "PESITOS",
                            color = Color(0xFF111344),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                // Imagen Pesito Azul
                Box(
                    modifier = Modifier
                        .size(200.dp, 120.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp)), // espacio imagen
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aquí va la imagen", color = Color.DarkGray, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Recuadro Grid
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(450.dp)
                    .background(Color(0xFF0A2D57), RoundedCornerShape(20.dp))
            ) {
                val scroll = rememberScrollState()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scroll)
                        .padding(top = 40.dp, bottom = 40.dp, start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    repeat(4) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            repeat(3) {
                                AvatarItem(
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // Título "AVATARES" sobrepuesto
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = 20.dp, y = (-20).dp)
                        .background(Color(0xFF435A7E), RoundedCornerShape(12.dp))
                        .padding(horizontal = 30.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "AVATARES",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp

                    )
                }
                
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        //Menú inferior
        Column(
            modifier = Modifier.fillMaxSize(),
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
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AvatarItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(Color(0xFF6194C4), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Imagen del avatar
        Box(
            modifier = Modifier
                .size(90.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
        )

        // Badge inferior con precio
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 14.dp)
                .background(Color(0xFF6C767E), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp) // espacio entre imagen y texto
            ) {
                // Imagen pequeña
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.LightGray, RoundedCornerShape(50)), // espacio para la imagen
                    contentAlignment = Alignment.Center
                ) {
                    Text("I", fontSize = 10.sp, color = Color.DarkGray) // marcador temporal
                }

                // Texto a la derecha
                Text(
                    text = "100",
                    color = Color(0xFFFF9505),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StoreScreenPreview() {
    val navController = rememberNavController()
    StoreScreen(navController = navController)
}
