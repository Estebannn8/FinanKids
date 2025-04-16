package com.universidad.finankids.navigation

import androidx.navigation.NavController

fun navigateToScreen(navController: NavController, item: String) {
    val route = when (item) {
        "inicio" -> AppScreens.HomeScreen.route
        "perfil" -> AppScreens.ProfileScreen.route
        "trofeo" -> AppScreens.TrophyScreen.route
        "progreso" -> AppScreens.ProgressScreen.route
        "tienda" -> AppScreens.StoreScreen.route
        else -> return
    }

    navController.navigate(route) {
        // Evita múltiples copias de la misma pantalla
        launchSingleTop = true

        // Para Home, limpia la pila de navegación
        if (item == "inicio") {
            popUpTo(AppScreens.HomeScreen.route) { inclusive = false }
        }
    }
}