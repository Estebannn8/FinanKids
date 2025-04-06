package com.universidad.finankids.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.universidad.finankids.R
import com.universidad.finankids.navigation.AppScreens
import com.universidad.finankids.ui.theme.AppTypography

@Composable
fun RecoveryScreen(navController: NavController) {
    val painterEmail = painterResource(id = R.drawable.ic_email)
    val painterBack = painterResource(id = R.drawable.ic_atras_recovery)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Icon(
            painter = painterBack,
            contentDescription = "Atrás",
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    navController.navigate(
                        AppScreens.AuthScreen.createRoute(startInLogin = true)
                    )
                },
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Recuperar contraseña",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = AppTypography.PoppinsFont,
            color = Color(0xFF52154E),
            modifier = Modifier
                .align(Alignment.Start)
                .drawBehind {
                    val strokeWidth = 4f
                    val y = size.height - strokeWidth / 2
                    drawLine(
                        color = Color(0xFF52154E),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Introduzca su dirección de correo electrónico para restablecer la contraseña",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = AppTypography.PoppinsFont,
            color = Color(0xFF2A2A2A)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Correo electrónico",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = AppTypography.PoppinsFont,
            color = Color(0xFF2A2A2A)
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = "",
            onValueChange = {},
            placeholder = "Ingrese correo",
            leadingIcon = painterEmail
        )

        Spacer(modifier = Modifier.height(36.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CustomButton(
                buttonText = "RESTABLECER",
                gradientLight = Color(0xFF9C749A),
                gradientDark = Color(0xFF431441),
                baseColor = Color(0xFF53164F),
                onClick = {
                    // Acción del botón
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}


@Preview(showBackground = true, name = "RecoveryScreen Preview")
@Composable
fun RecoveryScreenPreview() {
    // Crea un NavController simulado
    val navController = rememberNavController()

    RecoveryScreen(navController = navController)
}
