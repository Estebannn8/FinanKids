package com.universidad.finankids.ui.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.universidad.finankids.R
import com.universidad.finankids.navigation.AppScreens
import com.universidad.finankids.ui.CustomButton
import com.universidad.finankids.ui.CustomTextField
import com.universidad.finankids.ui.theme.AppTypography
import com.universidad.finankids.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    startInLogin: Boolean,
    navController: NavController,
    viewModel: AuthViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var isLoginSelected by rememberSaveable { mutableStateOf(startInLogin) }

    // Observar estados del ViewModel
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successEvent by viewModel.successEvent.collectAsState()
    val loading by viewModel.loading.collectAsState()

    // Manejar eventos de éxito/error
    LaunchedEffect(successEvent) {
        if (successEvent) {
            navController.navigate(AppScreens.HomeScreen.route) {
                popUpTo(AppScreens.AuthScreen.route) { inclusive = true }
                popUpTo(AppScreens.MainScreen.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp),
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = Color(0xFF52154E),
                        contentColor = Color.White
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
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
                    painterEmail = painterResource(id = R.drawable.ic_email),
                    painterPassword = painterResource(id = R.drawable.ic_password),
                    viewModel = viewModel,
                    navController = navController,
                    showSnackbar = { message ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                    },
                    onSwitchToRegister = {
                        viewModel.clearFields()
                        isLoginSelected = false
                    }
                )
            } else {
                RegisterForm(
                    painterUser = painterResource(id = R.drawable.ic_person),
                    painterEmail = painterResource(id = R.drawable.ic_email),
                    painterPassword = painterResource(id = R.drawable.ic_password),
                    viewModel = viewModel,
                    navController = navController,
                    showSnackbar = { message ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                    },
                    onSwitchToLogin = {
                        viewModel.clearFields()
                        isLoginSelected = true // <-- cambiamos el estado local para mostrar el login
                    }
                )
            }
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
fun LoginForm(
    painterEmail: Painter,
    painterPassword: Painter,
    viewModel: AuthViewModel,
    navController: NavController,
    showSnackbar: (String) -> Unit,
    onSwitchToRegister: () -> Unit
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Correo electrónico", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
        Spacer(modifier = Modifier.height(14.dp))
        CustomTextField(
            value = email,
            onValueChange = { viewModel.onEmailChanged(it) },
            placeholder = "Ingrese correo",
            leadingIcon = painterEmail
        )

        Spacer(modifier = Modifier.height(29.dp))

        Text("Contraseña", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
        Spacer(modifier = Modifier.height(14.dp))
        CustomTextField(
            value = password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            placeholder = "Ingrese contraseña",
            leadingIcon = painterPassword,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(29.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Contraseña Incorrecta", fontSize = 12.sp, color = Color(0xFFFF9E1B), fontFamily = AppTypography.PoppinsFont)
            Text("¿Olvidaste la Contraseña?", fontSize = 12.sp, color = Color(0xFF52154E),fontWeight = FontWeight.SemiBold, fontFamily = AppTypography.PoppinsFont, modifier = Modifier.clickable {
                navController.navigate(AppScreens.RecoveryScreen.route)
            })
        }

        Spacer(modifier = Modifier.height(29.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CustomButton(
                buttonText = "CONTINUAR",
                gradientLight = Color(0xFF9C749A),
                gradientDark = Color(0xFF431441),
                baseColor = Color(0xFF53164F),
                onClick = {
                    viewModel.login(
                        onSuccess = {
                            navController.navigate(AppScreens.HomeScreen.route) {
                                popUpTo(AppScreens.AuthScreen.route) { inclusive = true }
                                popUpTo(AppScreens.MainScreen.route) { inclusive = true }
                            }
                        },
                        onError = { message ->
                            showSnackbar(message)
                        }
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(29.dp))

        SocialLoginSection(
            isLogin = true,
            viewModel = viewModel,
            onAuthSuccess = {
                // Se maneja automáticamente con el successEvent
            },
            showError = { message ->
                showSnackbar(message)
            },
            onNavigateToRegister = { onSwitchToRegister() }
        )
    }
}

@Composable
fun RegisterForm(
    painterUser: Painter,
    painterEmail: Painter,
    painterPassword: Painter,
    viewModel: AuthViewModel,
    navController: NavController,
    showSnackbar: (String) -> Unit,
    onSwitchToLogin: () -> Unit
) {
    val username by viewModel.username.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val termsAccepted by viewModel.termsAccepted.collectAsState()

    var showTermsDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Usuario
        Text("Nombre de usuario", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
        Spacer(modifier = Modifier.height(14.dp))
        CustomTextField(
            value = username,
            onValueChange = { viewModel.onUsernameChanged(it) },
            placeholder = "Ingrese nombre de usuario",
            leadingIcon = painterUser
        )

        Spacer(modifier = Modifier.height(29.dp))

        // Correo
        Text("Correo electrónico", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
        Spacer(modifier = Modifier.height(14.dp))
        CustomTextField(
            value = email,
            onValueChange = { viewModel.onEmailChanged(it) },
            placeholder = "Ingrese correo",
            leadingIcon = painterEmail
        )

        Spacer(modifier = Modifier.height(29.dp))

        // Contraseña
        Text("Contraseña", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
        Spacer(modifier = Modifier.height(14.dp))
        CustomTextField(
            value = password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            placeholder = "Ingrese contraseña",
            leadingIcon = painterPassword,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(29.dp))

        // Checkbox y texto
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = { viewModel.onTermsAcceptedChanged(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF52154E),
                    checkmarkColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                buildAnnotatedString {
                    append("Aceptar los ")
                    withStyle(style = SpanStyle(color = Color(0xFFFF9E1B), fontWeight = FontWeight.Bold)) {
                        append("Términos y Condiciones")
                    }
                    append(".")
                },
                fontSize = 12.sp,
                fontFamily = AppTypography.PoppinsFont,
                color = Color(0xFF52154E),
                modifier = Modifier.clickable { showTermsDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(29.dp))

        // Botón de registro
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CustomButton(
                buttonText = "CREAR CUENTA",
                gradientLight = Color(0xFF9C749A),
                gradientDark = Color(0xFF431441),
                baseColor = Color(0xFF53164F),
                onClick = {
                    viewModel.register(
                        onSuccess = {
                            showSnackbar("Registro exitoso")
                            onSwitchToLogin()
                        },
                        onError = { message ->
                            showSnackbar(message)
                        }
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(29.dp))

        SocialLoginSection(
            isLogin = false,
            viewModel = viewModel,
            onAuthSuccess = {
                // Esto se manejará automáticamente con el successEvent
            },
            showError = { message ->
                showSnackbar(message)
            },
            onNavigateToLogin = { onSwitchToLogin() }
        )

        // Diálogo de Términos
        if (showTermsDialog) {
            AlertDialog(
                onDismissRequest = { showTermsDialog = false },
                title = { Text("Términos y Condiciones", style = MaterialTheme.typography.titleLarge) },
                text = {
                    Text(
                        buildString {
                            append("Bienvenido a nuestra app de educación financiera.\n\n")
                            append("Al utilizar esta aplicación, aceptas los siguientes términos:\n\n")
                            append("1. Uso Responsable:\n")
                            append("Esta aplicación está diseñada para enseñar finanzas personales de forma lúdica y educativa. No debe usarse como asesoramiento financiero profesional.\n\n")
                            append("2. Edad Mínima:\n")
                            append("Esta app está dirigida a niños y jóvenes. Si no tienes una cuenta de correo electrónico propia, puedes usar la de uno de tus padres o tutores con su permiso y bajo su supervisión. El uso de la aplicación en estos casos es responsabilidad del adulto responsable.\n\n")
                            append("3. Cuenta de Usuario:\n")
                            append("Para guardar tu progreso, necesitas registrarte con un correo válido. Eres responsable de mantener segura tu cuenta.\n\n")
                            append("4. Privacidad:\n")
                            append("Tus datos se almacenan de forma segura en Firebase. No compartimos tu información personal con terceros sin tu consentimiento.\n\n")
                            append("5. Contenido y Recompensas:\n")
                            append("Todos los elementos visuales, monedas virtuales, niveles, logros y avatares son parte de la experiencia educativa y no tienen valor monetario real.\n\n")
                            append("6. Modificaciones:\n")
                            append("Nos reservamos el derecho de modificar estos términos en cualquier momento. Te notificaremos si hay cambios importantes.\n\n")
                            append("Al continuar usando esta app, aceptas estos términos.")
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showTermsDialog = false }) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}


@Composable
fun SocialLoginSection(
    isLogin: Boolean,
    viewModel: AuthViewModel,
    onAuthSuccess: () -> Unit,
    showError: (String) -> Unit,
    onNavigateToRegister: (() -> Unit)? = null,
    onNavigateToLogin: (() -> Unit)? = null
) {

    val context = LocalContext.current

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
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 2.dp,
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
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 2.dp,
                color = Color(0xFFF8B528)
            )
        }

        Spacer(modifier = Modifier.height(29.dp))

        // Botón Google
        OutlinedButton(
            onClick = { viewModel.signInWithGoogle(context) },
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
                    if (isLogin) onNavigateToRegister?.invoke() else onNavigateToLogin?.invoke()
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}



@Preview(showBackground = true, name = "AuthScreen - Login")
@Composable
fun PreviewAuthScreenLogin() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    AuthScreen(startInLogin = true, navController = navController, viewModel = authViewModel)
}

@Preview(showBackground = true, name = "AuthScreen - Register")
@Composable
fun PreviewAuthScreenRegister() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    AuthScreen(startInLogin = false, navController = navController, viewModel = authViewModel)
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

