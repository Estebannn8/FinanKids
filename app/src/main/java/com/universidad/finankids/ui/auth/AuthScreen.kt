package com.universidad.finankids.ui.auth

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.universidad.finankids.R
import com.universidad.finankids.events.AuthEvent
import com.universidad.finankids.events.AvatarEvent
import com.universidad.finankids.events.UserEvent
import com.universidad.finankids.navigation.AppScreens
import com.universidad.finankids.state.AuthState
import com.universidad.finankids.ui.Components.CustomButton
import com.universidad.finankids.ui.Components.CustomTextField
import com.universidad.finankids.ui.Components.LoadingOverlay
import com.universidad.finankids.ui.theme.AppTypography
import com.universidad.finankids.viewmodel.AuthViewModel
import com.universidad.finankids.viewmodel.AvataresViewModel
import com.universidad.finankids.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    startInLogin: Boolean,
    navController: NavController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    avataresViewModel: AvataresViewModel
) {

    // Estados observables
    val authState by authViewModel.state.collectAsState()
    val userState by userViewModel.state.collectAsState()
    val avatarState by avataresViewModel.state.collectAsState()

    // Estados locales
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val hasNavigated = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current


    LaunchedEffect(authState.isLoading) {
        Log.d("AuthScreen", "isLoading state changed: ${authState.isLoading}")
    }

    // Establecer el estado inicial basado en startInLogin
    LaunchedEffect(startInLogin) {
        if (startInLogin != authState.isLoginSelected) {
            authViewModel.onEvent(
                if (startInLogin) AuthEvent.NavigateToLogin
                else AuthEvent.NavigateToRegister
            )
        }
    }

    // Cargar datos del usuario tras login o registro exitoso
    LaunchedEffect(authState.isSuccess) {
        if (authState.isSuccess && !hasNavigated.value) {
            authViewModel.getCurrentUserId()?.let { uid ->
                userViewModel.sendEvent(UserEvent.LoadUser(uid))
            }
        }
    }

    // Cargar avatar actual después de que se cargue el usuario
    LaunchedEffect(userState.userData.avatarActual, userState.isLoading) {
        if (!userState.isLoading && userState.userData.avatarActual.isNotEmpty() && !hasNavigated.value) {
            avataresViewModel.sendEvent(AvatarEvent.LoadAvatarById(userState.userData.avatarActual))
        }
    }

    // Navegar al Home cuando todo esté cargado
    LaunchedEffect(userState.userData.uid, userState.isLoading, avatarState.isLoading) {
        if (!hasNavigated.value &&
            userState.userData.uid.isNotEmpty() &&
            !userState.isLoading &&
            !avatarState.isLoading &&
            avatarState.currentAvatar != null
        ) {
            hasNavigated.value = true
            navController.navigate(AppScreens.HomeScreen.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // Mostrar errores de autenticación
    LaunchedEffect(authState.errorMessage) {
        authState.errorMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                authViewModel.clearError()
            }
        }
    }

    // Mostrar errores de carga de usuario
    LaunchedEffect(userState.errorMessage) {
        userState.errorMessage?.let { error ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(error)
                userViewModel.clearError()
            }
        }
    }

    // Mostrar errores de carga de avatar
    LaunchedEffect(avatarState.error) {
        avatarState.error?.let { error ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(error)
                avataresViewModel.sendEvent(AvatarEvent.ClearError)
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .clickable(
                    // Evita el ripple cuando tocas en blanco
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                }
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(29.dp))

                AuthHeader(
                    isLoginSelected = authState.isLoginSelected,
                    onLoginClick = { authViewModel.onEvent(AuthEvent.NavigateToLogin) },
                    onRegisterClick = { authViewModel.onEvent(AuthEvent.NavigateToRegister) }
                )

                Spacer(modifier = Modifier.height(29.dp))

                if (authState.isLoginSelected) {
                    LoginForm(
                        painterEmail = painterResource(id = R.drawable.ic_email),
                        painterPassword = painterResource(id = R.drawable.ic_password),
                        state = authState,
                        onEvent = authViewModel::onEvent,
                        onSignInWithGoogle = { authViewModel.signInWithGoogle(context) },
                        navController = navController
                    )
                } else {
                    RegisterForm(
                        painterUser = painterResource(id = R.drawable.ic_person),
                        painterEmail = painterResource(id = R.drawable.ic_email),
                        painterPassword = painterResource(id = R.drawable.ic_password),
                        state = authState,
                        onEvent = authViewModel::onEvent,
                        onSignInWithGoogle = { authViewModel.signInWithGoogle(context) }
                    )
                }
            }

            // Mostrar loading si es necesario
            if (authState.isLoading || userState.isLoading || avatarState.isLoading) {
                LoadingOverlay()
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
    state: AuthState,
    onEvent: (AuthEvent) -> Unit,
    onSignInWithGoogle: () -> Unit,
    navController: NavController
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Correo electrónico", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
        Spacer(modifier = Modifier.height(14.dp))
        CustomTextField(
            value = state.email,
            onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
            placeholder = "Ingrese correo",
            leadingIcon = painterEmail
        )

        Spacer(modifier = Modifier.height(29.dp))

        Text("Contraseña", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
        Spacer(modifier = Modifier.height(14.dp))
        CustomTextField(
            value = state.password,
            onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
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
                onClick = { onEvent(AuthEvent.Login) }
            )
        }

        Spacer(modifier = Modifier.height(29.dp))

        SocialLoginSection(
            isLogin = true,
            onSignInWithGoogle = onSignInWithGoogle,
            onNavigateToRegister = { onEvent(AuthEvent.NavigateToRegister) }
        )
    }
}

@Composable
fun RegisterForm(
    painterUser: Painter,
    painterEmail: Painter,
    painterPassword: Painter,
    state: AuthState,
    onEvent: (AuthEvent) -> Unit,
    onSignInWithGoogle: () -> Unit
) {
    var showTermsDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Usuario
        Text("Nombre de usuario", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
        Spacer(modifier = Modifier.height(14.dp))
        CustomTextField(
            value = state.username,
            onValueChange = { onEvent(AuthEvent.UsernameChanged(it)) },
            placeholder = "Ingrese nombre de usuario",
            leadingIcon = painterUser
        )

        Spacer(modifier = Modifier.height(29.dp))

        // Correo
        Text("Correo electrónico", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
        Spacer(modifier = Modifier.height(14.dp))
        CustomTextField(
            value = state.email,
            onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
            placeholder = "Ingrese correo",
            leadingIcon = painterEmail
        )

        Spacer(modifier = Modifier.height(29.dp))

        // Contraseña
        Text("Contraseña", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2A2A2A), fontFamily = AppTypography.PoppinsFont)
        Spacer(modifier = Modifier.height(14.dp))
        CustomTextField(
            value = state.password,
            onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
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
                checked = state.termsAccepted,
                onCheckedChange = { onEvent(AuthEvent.TermsAcceptedChanged(it)) },
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
                onClick = { onEvent(AuthEvent.Register) }
            )
        }

        Spacer(modifier = Modifier.height(29.dp))

        SocialLoginSection(
            isLogin = false,
            onSignInWithGoogle = onSignInWithGoogle,
            onNavigateToLogin = { onEvent(AuthEvent.NavigateToLogin) }
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
    onSignInWithGoogle: () -> Unit,
    onNavigateToRegister: (() -> Unit)? = null,
    onNavigateToLogin: (() -> Unit)? = null
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
            onClick = onSignInWithGoogle,
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

