package com.universidad.finankids.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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

    var isLoginSelected by rememberSaveable { mutableStateOf(startInLogin) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(29.dp))

        AuthHeader(
            isLoginSelected = isLoginSelected,
            onLoginClick = { isLoginSelected = true },
            onRegisterClick = { isLoginSelected = false }
        )

        Spacer(modifier = Modifier.height(29.dp))

        if (isLoginSelected) {
            LoginForm(
                painterEmail = painterEmail,
                painterPassword = painterPassword,
                onNavigateToRegister = { isLoginSelected = false }
            )
        } else {
            RegisterForm(
                painterUser = painterUser,
                painterEmail = painterEmail,
                painterPassword = painterPassword,
                onNavigateToLogin = { isLoginSelected = true }
            )
        }
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
                Spacer(modifier = Modifier.height(3.dp))
            }
        }

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
fun LoginForm(painterEmail: Painter, painterPassword: Painter, onNavigateToRegister: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Correo electrónico", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
            Spacer(modifier = Modifier.height(14.dp))
            CustomTextField(value = "", onValueChange = {}, placeholder = "Ingrese correo", leadingIcon = painterEmail)
        }

        Spacer(modifier = Modifier.height(29.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Contraseña", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
            Spacer(modifier = Modifier.height(14.dp))
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

        // Botón centrado
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CustomButton(
                buttonText = "CONTINUAR",
                gradientLight = Color(0xFF9C749A),
                gradientDark = Color(0xFF431441),
                baseColor = Color(0xFF53164F),
                onClick = { /* Acción */ }
            )
        }

        Spacer(modifier = Modifier.height(29.dp))

        SocialLoginSection(
            isLogin = true,
            onNavigateToRegister = onNavigateToRegister
        )
    }
}

@Composable
fun RegisterForm(
    painterUser: Painter,
    painterEmail: Painter,
    painterPassword: Painter,
    onNavigateToLogin: () -> Unit
) {
    var isChecked by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Nombre de usuario",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2A2A2A),
                fontFamily = AppTypography.PoppinsFont
            )
            Spacer(modifier = Modifier.height(14.dp))
            CustomTextField(
                value = "",
                onValueChange = {},
                placeholder = "Ingrese nombre de usuario",
                leadingIcon = painterUser
            )
        }

        Spacer(modifier = Modifier.height(29.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Correo electrónico",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2A2A2A),
                fontFamily = AppTypography.PoppinsFont
            )
            Spacer(modifier = Modifier.height(14.dp))
            CustomTextField(
                value = "",
                onValueChange = {},
                placeholder = "Ingrese correo",
                leadingIcon = painterEmail
            )
        }

        Spacer(modifier = Modifier.height(29.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Contraseña",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2A2A2A),
                fontFamily = AppTypography.PoppinsFont
            )
            Spacer(modifier = Modifier.height(14.dp))
            CustomTextField(
                value = "",
                onValueChange = {},
                placeholder = "Ingrese contraseña",
                leadingIcon = painterPassword,
                isPassword = true
            )
        }

        Spacer(modifier = Modifier.height(29.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isChecked = !isChecked }
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF52154E),
                    checkmarkColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                buildAnnotatedString {
                    append("Aceptar los ")
                    withStyle(style = SpanStyle(color = Color(0xFFFF9E1B))) {
                        append("Términos y Condiciones")
                    }
                    append(".")
                },
                fontSize = 12.sp,
                color = Color(0xFF52154E),
                fontFamily = AppTypography.PoppinsFont,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(29.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CustomButton(
                buttonText = "CREAR CUENTA",
                gradientLight = Color(0xFF9C749A),
                gradientDark = Color(0xFF431441),
                baseColor = Color(0xFF53164F),
                onClick = { /* Acción de registro */ }
            )
        }

        Spacer(modifier = Modifier.height(29.dp))

        SocialLoginSection(
            isLogin = false,
            onNavigateToLogin = onNavigateToLogin
        )
    }
}


@Composable
fun SocialLoginSection(
    isLogin: Boolean,
    onNavigateToRegister: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Línea divisoria con "o"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Divider(
                modifier = Modifier.weight(1f).height(2.dp),
                color = Color(0xFFF8B528)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "o",
                fontSize = 15.sp,
                fontFamily = AppTypography.Baloo,
                color = Color(0xFFF8B528)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Divider(
                modifier = Modifier.weight(1f).height(2.dp),
                color = Color(0xFFF8B528)
            )
        }

        Spacer(modifier = Modifier.height(29.dp))

        // Botón Google
        OutlinedButton(
            onClick = { /* Acción Google */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(3.dp, Color.LightGray),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFE7E7E7))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google",
                modifier = Modifier.size(26.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                if (isLogin) "Iniciar Sesión con Google" else "Registrarse con Google",
                color = Color.Black,
                fontFamily = AppTypography.PoppinsFont
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón Facebook
        OutlinedButton(
            onClick = { /* Acción Facebook */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(3.dp, Color.LightGray),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFE7E7E7))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_facebook),
                contentDescription = "Facebook",
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                if (isLogin) "Iniciar Sesión con Facebook" else "Registrarse con Facebook",
                color = Color.Black,
                fontFamily = AppTypography.PoppinsFont
            )
        }

        Spacer(modifier = Modifier.height(29.dp))

        // Texto de navegación
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (isLogin) "¿No tienes cuenta?" else "¿Ya tienes una cuenta?",
                fontSize = 12.sp,
                color = Color(0xFFFF9E1B),
                fontFamily = AppTypography.PoppinsFont
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                if (isLogin) "Regístrate aquí." else "Inicia sesión aquí.",
                fontSize = 12.sp,
                color = Color(0xFF52154E),
                fontWeight = FontWeight.Bold,
                fontFamily = AppTypography.PoppinsFont,
                modifier = Modifier.clickable {
                    if (isLogin) onNavigateToRegister() else onNavigateToLogin()
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
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

