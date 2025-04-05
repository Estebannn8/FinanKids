package com.universidad.finankids.navigation

sealed class  AppScreens(val route: String){
    object SplashScreen: AppScreens(route = "splash_screen")
    object MainScreen: AppScreens(route = "main_screen")
    object AuthScreen : AppScreens("auth_screen/{startInLogin}") {
        fun createRoute(startInLogin: Boolean) = "auth_screen/$startInLogin"
    }
}

