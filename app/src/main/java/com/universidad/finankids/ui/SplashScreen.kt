package com.universidad.finankids.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.universidad.finankids.events.AvatarEvent
import com.universidad.finankids.events.UserEvent
import com.universidad.finankids.navigation.AppScreens
import com.universidad.finankids.ui.Components.LoadingOverlay
import com.universidad.finankids.viewmodel.AuthViewModel
import com.universidad.finankids.viewmodel.AvataresViewModel
import com.universidad.finankids.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    avataresViewModel: AvataresViewModel
) {
    val userState by userViewModel.state.collectAsState()
    val avatarState by avataresViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        delay(3000)
        val uid = authViewModel.getCurrentUserId()

        if (uid != null) {
            userViewModel.sendEvent(UserEvent.LoadUser(uid))
        } else {
            navController.navigate(AppScreens.MainScreen.route) {
                popUpTo(AppScreens.SplashScreen.route) { inclusive = true }
            }
        }
    }

    // Cargar avatar cuando el usuario esté cargado
    LaunchedEffect(userState.userData.avatarActual, userState.isLoading) {
        if (!userState.isLoading && userState.userData.avatarActual.isNotEmpty()) {
            avataresViewModel.sendEvent(AvatarEvent.LoadAvatarById(userState.userData.avatarActual))
        }
    }

    // Navegar cuando todo esté cargado
    LaunchedEffect(userState.isLoading, avatarState.isLoading) {
        if (
            !userState.isLoading &&
            !avatarState.isLoading &&
            userState.userData.uid.isNotEmpty() &&
            avatarState.currentAvatar != null
        ) {
            navController.navigate(AppScreens.HomeScreen.route) {
                popUpTo(AppScreens.SplashScreen.route) { inclusive = true }
            }
        }
    }

    Splash()

    if (userState.isLoading || avatarState.isLoading) {
        LoadingOverlay()
    }
}


@Composable
fun Splash(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4C1D6B)), // Color de fondo
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "FINANKIDS",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(70.dp))
            Text(
                text = "Gana jugando, aprende ahorrando",
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    Splash()
}

