package com.universidad.finankids.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.universidad.finankids.ui.auth.AuthScreen
import com.universidad.finankids.ui.HomeScreen
import com.universidad.finankids.ui.MainScreen
import com.universidad.finankids.ui.RecoveryScreen
import com.universidad.finankids.ui.SplashScreen
import com.universidad.finankids.viewmodel.AuthViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.route
    ){

        composable(AppScreens.SplashScreen.route) {
            SplashScreen(navController)
        }

        composable(AppScreens.MainScreen.route) {
            MainScreen(navController)
        }

        composable(
            route = AppScreens.AuthScreen.route,
            arguments = listOf(navArgument("startInLogin") { type = NavType.BoolType })
        ) { backStackEntry ->
            val startInLogin = backStackEntry.arguments?.getBoolean("startInLogin") ?: true
            val authViewModel: AuthViewModel = viewModel()
            AuthScreen(startInLogin, navController = navController, viewModel = authViewModel)
        }

        composable(AppScreens.RecoveryScreen.route) {
            RecoveryScreen(navController)
        }

        composable(AppScreens.HomeScreen.route){
            HomeScreen(navController)
        }

    }
}