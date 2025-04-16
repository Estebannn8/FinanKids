package com.universidad.finankids.navigation

sealed class  AppScreens(val route: String){
    object SplashScreen: AppScreens(route = "splash_screen")
    object MainScreen: AppScreens(route = "main_screen")
    object AuthScreen: AppScreens("auth_screen/{startInLogin}") {
        fun createRoute(startInLogin: Boolean) = "auth_screen/$startInLogin"
    }
    object RecoveryScreen: AppScreens(route = "recovery_screen")
    object HomeScreen: AppScreens(route = "home_screen")
    object ProfileScreen: AppScreens(route = "profile_screen")
    object TrophyScreen: AppScreens(route = "trophy_screen")
    object ProgressScreen: AppScreens(route = "progress_screen")
    object StoreScreen: AppScreens(route = "store_screen")
}

