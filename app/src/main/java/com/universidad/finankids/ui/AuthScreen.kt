package com.universidad.finankids.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.R
import com.universidad.finankids.ui.theme.AppTypography

@Composable
fun AuthScreen(startInLogin: Boolean) {
    val painterEmail = painterResource(id = R.drawable.ic_email)
    val painterPassword = painterResource(id = R.drawable.ic_password)
    val painterUser = painterResource(id = R.drawable.ic_person)

    var isLoginSelected by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        AuthHeader(
            isLoginSelected = isLoginSelected,
            onLoginClick = { isLoginSelected = true },
            onRegisterClick = { isLoginSelected = false }
        )

        Spacer(modifier = Modifier.height(29.dp))

        if (isLoginSelected) {
            // Login Form
            LoginForm(painterEmail, painterPassword)
        } else {
            // Register Form
            RegisterForm(painterUser, painterEmail, painterPassword)
        }
    }
}

@Composable
fun LoginForm(painterEmail: Painter, painterPassword: Painter) {
    Column {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Correo electrónico", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
            CustomTextField(value = "", onValueChange = {}, placeholder = "Ingrese correo", leadingIcon = painterEmail)
        }

        Spacer(modifier = Modifier.height(29.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Contraseña", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
            CustomTextField(value = "", onValueChange = {}, placeholder = "Ingrese contraseña", leadingIcon = painterPassword, isPassword = true)
        }

        Spacer(modifier = Modifier.height(29.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Contraseña Incorrecta", fontSize = 12.sp, color = Color(0xFFFF9E1B), fontFamily = AppTypography.PoppinsFont)
            Text("¿Olvidaste la Contraseña?", fontSize = 12.sp, color = Color(0xFF52154E), fontFamily = AppTypography.PoppinsFont, modifier = Modifier.clickable { })
        }

        Spacer(modifier = Modifier.height(29.dp))

        CustomButton(
            buttonText = "CONTINUAR",
            gradientLight = Color(0xFF9C749A),
            gradientDark = Color(0xFF431441),
            baseColor = Color(0xFF53164F),
            onClick = { /* Acción */ }
        )

        Spacer(modifier = Modifier.height(29.dp))

        SocialLoginSection()
    }
}

@Composable
fun RegisterForm(painterUser: Painter, painterEmail: Painter, painterPassword: Painter) {
    Column {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Nombre de usuario", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
            CustomTextField(value = "", onValueChange = {}, placeholder = "Ingrese nombre de usuario", leadingIcon = painterUser)
        }

        Spacer(modifier = Modifier.height(29.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Correo electrónico", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
            CustomTextField(value = "", onValueChange = {}, placeholder = "Ingrese correo", leadingIcon = painterEmail)
        }

        Spacer(modifier = Modifier.height(29.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Contraseña", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
            CustomTextField(value = "", onValueChange = {}, placeholder = "Ingrese contraseña", leadingIcon = painterPassword, isPassword = true)
        }

        Spacer(modifier = Modifier.height(29.dp))

        CustomButton(
            buttonText = "REGISTRARSE",
            gradientLight = Color(0xFF9C749A),
            gradientDark = Color(0xFF431441),
            baseColor = Color(0xFF53164F),
            onClick = { /* Acción */ }
        )

        Spacer(modifier = Modifier.height(29.dp))

        SocialLoginSection()
    }
}

@Composable
fun AuthHeader(
    isLoginSelected: Boolean,
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        // Iniciar sesión
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onLoginClick() }
        ) {
            Text(
                text = "Iniciar Sesión",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = AppTypography.PoppinsFont,
                color = if (isLoginSelected) Color(0xFF52154E) else Color(0xFFF8B528)
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (isLoginSelected) {
                Box(
                    modifier = Modifier
                        .height(3.dp)
                        .width(100.dp)
                        .background(Color(0xFF52154E), RoundedCornerShape(5.dp))
                )
            } else {
                Spacer(modifier = Modifier.height(3.dp)) // mantener el espacio para alineación
            }
        }

        // Registrarse
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onRegisterClick() }
        ) {
            Text(
                text = "Registrarse",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = AppTypography.PoppinsFont,
                color = if (!isLoginSelected) Color(0xFF52154E) else Color(0xFFF8B528)
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (!isLoginSelected) {
                Box(
                    modifier = Modifier
                        .height(3.dp)
                        .width(100.dp)
                        .background(Color(0xFF52154E), RoundedCornerShape(5.dp))
                )
            } else {
                Spacer(modifier = Modifier.height(3.dp))
            }
        }
    }
}

@Composable
fun SocialLoginSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Divider(modifier = Modifier.weight(1f).height(2.dp), color = Color(0xFFF8B528))
        Spacer(modifier = Modifier.width(8.dp))
        Text("o", fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.width(8.dp))
        Divider(modifier = Modifier.weight(1f).height(2.dp), color = Color(0xFFF8B528))
    }

    Spacer(modifier = Modifier.height(29.dp))

    Button(
        onClick = {},
        modifier = Modifier.fillMaxWidth().height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE7E7E7))
    ) {
        Text("Iniciar Sesión con Google", color = Color.Black)
    }

    Spacer(modifier = Modifier.height(29.dp))

    Button(
        onClick = {},
        modifier = Modifier.fillMaxWidth().height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE7E7E7))
    ) {
        Text("Iniciar Sesión con Facebook", color = Color.Black)
    }

    Spacer(modifier = Modifier.height(24.dp))

    Row {
        Text("¿No tienes cuenta?", fontSize = 12.sp, color = Color(0xFFFF9E1B), fontFamily = AppTypography.PoppinsFont)
        Spacer(modifier = Modifier.width(4.dp))
        Text("Regístrate aquí.", fontSize = 12.sp, color = Color(0xFF52154E), fontWeight = FontWeight.Bold, fontFamily = AppTypography.PoppinsFont, modifier = Modifier.clickable { })
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@Preview(showBackground = true, name = "AuthScreen - Login")
@Composable
fun PreviewAuthScreenLogin() {
    AuthScreen(startInLogin = true)
}

@Preview(showBackground = true, name = "AuthScreen - Register")
@Composable
fun PreviewAuthScreenRegister() {
    AuthScreen(startInLogin = false)
}



@Preview(showBackground = true)
@Composable
fun PreviewAuthHeaderLoginSelected() {
    AuthHeader(isLoginSelected = true)
}

@Preview(showBackground = true)
@Composable
fun PreviewAuthHeaderRegisterSelected() {
    AuthHeader(isLoginSelected = false)
}

