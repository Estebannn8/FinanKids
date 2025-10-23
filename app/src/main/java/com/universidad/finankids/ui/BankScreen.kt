package com.universidad.finankids.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.universidad.finankids.R
import com.universidad.finankids.events.BancoEvent
import com.universidad.finankids.navigation.navigateToScreen
import com.universidad.finankids.ui.components.BankKeypad
import com.universidad.finankids.ui.components.BottomMenu
import com.universidad.finankids.viewmodel.BancoViewModel

@Composable
fun BankScreen(
    navController: NavController,
    bancoViewModel: BancoViewModel = viewModel()
) {
    val state by bancoViewModel.state.collectAsState()

    var showDialog by remember { mutableStateOf(true) }
    var pinInput by remember { mutableStateOf("") }
    var confirmPinInput by remember { mutableStateOf("") }
    var confirmMode by remember { mutableStateOf(false) }

    // Cargar banco al iniciar
    LaunchedEffect(Unit) {
        bancoViewModel.onEvent(BancoEvent.CargarBanco)
    }

    // Si se valida el PIN correctamente, cerrar el diálogo y aplicar intereses
    LaunchedEffect(state.pinValidado) {
        if (state.pinValidado) {
            showDialog = false
            bancoViewModel.onEvent(BancoEvent.CalcularIntereses)
        }
    }

    // Logs (puedes reemplazar por Snackbar)
    LaunchedEffect(state.errorMessage, state.mensaje) {
        state.errorMessage?.let { Log.e("BankScreen", it) }
        state.mensaje?.let { Log.d("BankScreen", it) }
    }

    var selectedItem by remember { mutableStateOf("banco") }
    val sectionBackgroundColor = Color(0xFFC9CED6)

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Box(modifier = Modifier.fillMaxSize()) {

        // Fondo del cajero
        Image(
            painter = painterResource(id = R.drawable.fondo_cajero),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // --- Vista principal del banco ---
        if (state.pinValidado) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                // --- Header con saldo ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = screenHeight * 0.08f, end = screenWidth * 0.1f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_pesito_moneda),
                        contentDescription = "Pesito",
                        modifier = Modifier.size(screenWidth * 0.07f)
                    )
                    Text(
                        text = "${state.banco?.saldo ?: 0}",
                        color = Color.Black,
                        fontSize = (screenWidth.value * 0.06).sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // --- Pantalla del cajero ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = screenHeight * 0.15f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Pantalla de saldo
                        Box(contentAlignment = Alignment.TopStart) {
                            Image(
                                painter = painterResource(id = R.drawable.pesitos_guardados_bank),
                                contentDescription = "Pesitos guardados",
                                modifier = Modifier
                                    .width(screenWidth * 0.55f)
                                    .height(screenHeight * 0.15f)
                            )
                            Text(
                                text = "${state.banco?.saldo ?: 0}",
                                color = Color.Black,
                                fontSize = (screenWidth.value * 0.075).sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(
                                        top = screenHeight * 0.08f,
                                        start = screenWidth * 0.15f
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(screenHeight * 0.02f))

                        // Intereses
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = "¡Tu ahorro ha aumentado!",
                                color = Color.Black,
                                fontSize = (screenWidth.value * 0.045).sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = screenHeight * 0.005f)
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(screenWidth * 0.02f)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.flecha_intereses_bank),
                                    contentDescription = "Flecha",
                                    modifier = Modifier.size(screenWidth * 0.07f)
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.ic_pesito_moneda),
                                    contentDescription = "Pesito",
                                    modifier = Modifier.size(screenWidth * 0.07f)
                                )
                                Text(
                                    text = "+${state.banco?.interes?.times(100)?.toInt() ?: 0}%",
                                    color = Color.Black,
                                    fontSize = (screenWidth.value * 0.055).sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // --- Menú inferior ---
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomMenu(
                        isHomeSection = false,
                        sectionColor = "",
                        menuBackgroundColor = sectionBackgroundColor,
                        selectedItem = selectedItem,
                        onItemSelected = { item ->
                            selectedItem = item
                            navigateToScreen(navController, item)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // --- Diálogo para PIN ---
        if (!state.pinValidado) {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                BankKeypad(
                    bancoViewModel = bancoViewModel,
                    onPinConfirmado = { /* Se cierra automáticamente */ }
                )
            }
        }


    }
}



@Preview(showBackground = true, showSystemUi = true, name = "Phone")
@Composable
fun BankScreenPreviewPhone() {
    val navController = rememberNavController()
    BankScreen(navController = navController)
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420",
    name = "Medium Phone"
)
@Composable
fun BankScreenPreviewMediumPhone() {
    val navController = rememberNavController()
    BankScreen(navController = navController)
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=390dp,height=800dp,dpi=420",
    name = "Small Phone"
)
@Composable
fun BankScreenPreviewSmallPhone() {
    val navController = rememberNavController()
    BankScreen(navController = navController)
}


