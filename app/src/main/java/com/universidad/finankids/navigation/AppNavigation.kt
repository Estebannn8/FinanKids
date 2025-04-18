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
import com.universidad.finankids.ui.ProfileScreen
import com.universidad.finankids.ui.ProgressScreen
import com.universidad.finankids.ui.RecoveryScreen
import com.universidad.finankids.ui.SplashScreen
import com.universidad.finankids.ui.StoreScreen
import com.universidad.finankids.ui.TrophyScreen
import com.universidad.finankids.viewmodel.AuthViewModel
import com.universidad.finankids.viewmodel.UserViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // Crear los ViewModels aquí para que sean compartidos
    val authViewModel: AuthViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.route
    ) {

        composable(AppScreens.SplashScreen.route) {
            SplashScreen(navController, authViewModel, userViewModel)
        }

        composable(AppScreens.MainScreen.route) {
            MainScreen(navController)
        }

        composable(
            route = AppScreens.AuthScreen.route,
            arguments = listOf(navArgument("startInLogin") { type = NavType.BoolType })
        ) { backStackEntry ->
            val startInLogin = backStackEntry.arguments?.getBoolean("startInLogin") ?: true
            AuthScreen(startInLogin, navController = navController, authViewModel = authViewModel, userViewModel = userViewModel)
        }

        composable(AppScreens.RecoveryScreen.route) {
            RecoveryScreen(navController)
        }

        composable(AppScreens.HomeScreen.route) {
            HomeScreen(navController, userViewModel)
        }

        composable(AppScreens.ProfileScreen.route) {
            ProfileScreen(navController)
        }

        composable(AppScreens.TrophyScreen.route) {
            TrophyScreen(navController)
        }

        composable(AppScreens.ProgressScreen.route) {
            ProgressScreen(navController)
        }

        composable(AppScreens.StoreScreen.route) {
            StoreScreen(navController)
        }
    }
}
