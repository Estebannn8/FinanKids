package com.universidad.finankids.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.universidad.finankids.navigation.AppScreens
import com.universidad.finankids.ui.theme.AppTypography
import com.universidad.finankids.viewmodel.AuthViewModel


@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "Bienvenido a la pantalla principal",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = AppTypography.PoppinsFont,
            color = Color(0xFF2A2A2A)
        )

        // Botón de cierre de sesión
        Button(
            onClick = {
                authViewModel.logout()
                // Navegar a MainScreen en lugar de AuthScreen
                navController.navigate(AppScreens.MainScreen.route) {
                    // Limpiar el back stack completamente
                    popUpTo(0)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF52154E),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                "Cerrar Sesión",
                fontFamily = AppTypography.PoppinsFont,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
