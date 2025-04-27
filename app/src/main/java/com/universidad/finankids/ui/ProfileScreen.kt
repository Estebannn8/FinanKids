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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.universidad.finankids.R
import com.universidad.finankids.navigation.navigateToScreen
import com.universidad.finankids.ui.Components.BottomMenu
import com.universidad.finankids.ui.Components.LoadingOverlay
import com.universidad.finankids.ui.theme.AppTypography
import com.universidad.finankids.viewmodel.AvataresViewModel
import com.universidad.finankids.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    /*
    userViewModel: UserViewModel,
    avataresViewModel: AvataresViewModel
     */
) {

    /*

    // Estados observables
    val userState by userViewModel.state.collectAsState()
    val avatarState by avataresViewModel.state.collectAsState()

    // Verificar si los datos están cargados
    val isDataLoaded = remember(userState, avatarState) {
        userState.userData.uid.isNotEmpty() && !userState.isLoading &&
                avatarState.currentAvatar != null && !avatarState.isLoading
    }

    // Mostrar loading si los datos no están listos
    if (!isDataLoaded) {
        LoadingOverlay()
    }

     */

    var selectedItem by remember { mutableStateOf("perfil") }

    val sectionMenuColor = Color(0xFFC9CED6)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDCDEE2)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Spacer(modifier = Modifier.height(24.dp))

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

                //Title
                Text(
                    text = "MI PERFIL",
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 18.99.sp,
                        fontFamily = FontFamily(Font(R.font.baloo_regular)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF666666),
                        textAlign = TextAlign.Right,
                        letterSpacing = 3.07.sp,
                    )
                )

                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            // --- Avatar ---
                            Image(
                                painter = painterResource(id = R.drawable.ic_avatar_placeholder),  //PlaceHolder
                                contentDescription = "Avatar predeterminado",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                                    .offset(x = 0.6.dp, y = 2.4.dp),
                                contentScale = ContentScale.Inside
                            )

                            // --- Marco ---
                            Image(
                                painter = painterResource(id = R.drawable.ic_frame_naranja),  // PlaceHolder
                                contentDescription = "Marco del avatar",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    // Nivel
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(32.dp)
                            .align(Alignment.BottomStart)
                            .offset(x = 114.dp, y = (-50).dp)
                            .zIndex(3f),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(
                            modifier = Modifier
                                .size(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_nivel_naranja),
                                contentDescription = "Icono de nivel",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = "1",  // <- Nivel
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }


                        // --- Titulo de nivel ---
                        Text(
                            text = "NOVATO AHORRADOR",
                            modifier = Modifier
                                .padding(start = 10.dp),
                            style = TextStyle(
                                fontSize = 15.sp,
                                lineHeight = 10.99.sp,
                                fontFamily = FontFamily(Font(R.font.baloo_regular)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF666666),
                                textAlign = TextAlign.Right,
                                letterSpacing = 2.07.sp,
                            )
                        )

                    }

                    // --- Nickname ---
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .offset(x = 114.dp, y = (-105).dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        AppTypography.ItimStroke(
                            text = "EstebanR12333",   // <- Nickname
                            strokeColor = Color(0xFF666666),
                            fillColor = Color(0xFF666666),
                            fontSize = 21.sp,
                            textAlign = TextAlign.Start,
                            lineHeight = 1.sp,
                            letterSpacing = 1.sp
                        )

                    }


                    // --- Ajustes y Editar ---
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 0.dp, y = (-107).dp),
                    ) {

                        // --- Editar ---
                        Image(
                            painter = painterResource(id = R.drawable.ic_editar),
                            contentDescription = "Moneda",
                            modifier = Modifier.size(30.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        // --- Ajustes ---
                        Image(
                            painter = painterResource(id = R.drawable.ic_ajustes),
                            contentDescription = "Moneda",
                            modifier = Modifier.size(30.dp)
                        )
                    }


                }


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