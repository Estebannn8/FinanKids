package com.universidad.finankids.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.universidad.finankids.R
import com.universidad.finankids.navigation.AppScreens
import com.universidad.finankids.ui.components.CustomButton
import com.universidad.finankids.ui.theme.AppTypography

@Composable
fun MainScreen(navController: NavController) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.ic_background_splash),
            contentDescription = "Fondo de pantalla",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Logo
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.TopCenter)
                .offset(y = 30.dp, x = 10.dp),
            contentScale = ContentScale.Fit
        )

        // Contenido principal sobre la imagen de fondo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.asPaddingValues()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Sección media
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp, vertical = 15.dp)
                    .offset(y = (160).dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pesitos_inicio),
                    contentDescription = "Ilustración central",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            // Botones
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.29f)
                    .padding(15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomButton(
                    buttonText = "REGISTRARSE",
                    gradientLight = Color(0xFF9C749A),
                    gradientDark = Color(0xFF431441),
                    baseColor = Color(0xFF53164F),
                    onClick = { navController.navigate(AppScreens.AuthScreen.createRoute(startInLogin = false)) }
                )
                Spacer(modifier = Modifier.height(12.dp))
                CustomButton(
                    buttonText = "INICIAR SESIÓN",
                    gradientLight = Color(0xFF9C749A),
                    gradientDark = Color(0xFF431441),
                    baseColor = Color(0xFF53164F),
                    onClick = { navController.navigate(AppScreens.AuthScreen.createRoute(startInLogin = true)) }
                )
            }
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "MainScreen Preview"
)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()

    MainScreen(navController = navController)
}