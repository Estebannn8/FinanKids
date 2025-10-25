package com.universidad.finankids.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.universidad.finankids.R
import com.universidad.finankids.data.model.UserSettings
import kotlinx.coroutines.delay
import androidx.core.graphics.toColorInt
import com.github.skydoves.colorpicker.compose.BrightnessSlider

@Composable
fun Settings(
    onToggleMusica: () -> Unit = {},
    onToggleSonido: () -> Unit = {},
    onChangeFondoPerfil: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    userSettings: UserSettings = UserSettings()
) {

    var showColorPicker by remember { mutableStateOf(false) }

    // Usar el color actual de los settings
    var selectedColor by remember(userSettings.colorFondoPerfil) {
        mutableStateOf(
            try {
                Color(userSettings.colorFondoPerfil.toColorInt())
            } catch (e: Exception) {
                Color(0xFFDCDEE2)
            }
        )
    }

    val colorPickerController = rememberColorPickerController()

    var musicaOn = userSettings.musicaActiva
    var sonidoOn = userSettings.sonidoActiva


    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .size(300.dp),
        contentAlignment = Alignment.Center
    ) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.background_menu_settings),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )

        // Título
        Image(
            painter = painterResource(id = R.drawable.ajustes),
            contentDescription = "ajustes",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-20).dp)
        )

        // Opciones centrales
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-20).dp)
                .padding(horizontal = 26.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Música
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Música",
                    fontFamily = FontFamily(Font(R.font.baloo_regular)),
                    fontSize = 25.sp,
                    color = Color(0xFF666666)
                )

                Spacer(modifier = Modifier.width(10.dp))

                var musicaClicked by remember { mutableStateOf(false) }
                val musicaScale by animateFloatAsState(
                    targetValue = if (musicaClicked) 1.1f else 1f,
                    animationSpec = tween(durationMillis = 200),
                    finishedListener = { musicaClicked = false }
                )

                Image(
                    painter = painterResource(
                        id = if (musicaOn) R.drawable.ic_altavoz_on else R.drawable.ic_altavoz_off
                    ),
                    contentDescription = "Toggle música",
                    modifier = Modifier
                        .size(32.dp)
                        .scale(musicaScale)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            musicaClicked = true
                            musicaOn = !musicaOn
                            onToggleMusica()
                        }
                )

            }

            // Sonido
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Sonido",
                    fontFamily = FontFamily(Font(R.font.baloo_regular)),
                    fontSize = 25.sp,
                    color = Color(0xFF666666)
                )

                Spacer(modifier = Modifier.width(10.dp))

                var sonidoClicked by remember { mutableStateOf(false) }
                val sonidoScale by animateFloatAsState(
                    targetValue = if (sonidoClicked) 1.1f else 1f,
                    animationSpec = tween(durationMillis = 200),
                    finishedListener = { sonidoClicked = false }
                )

                Image(
                    painter = painterResource(
                        id = if (sonidoOn) R.drawable.ic_altavoz_on else R.drawable.ic_altavoz_off
                    ),
                    contentDescription = "Toggle sonido",
                    modifier = Modifier
                        .size(32.dp)
                        .scale(sonidoScale)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            sonidoClicked = true
                            sonidoOn = !sonidoOn
                            onToggleSonido()
                        }
                )

            }


            // Fondo de perfil
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Fondo de perfil",
                    fontFamily = FontFamily(Font(R.font.baloo_regular)),
                    fontSize = 25.sp,
                    color = Color(0xFF666666)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(6.dp),
                            clip = false
                        )
                        .background(selectedColor)
                        .clickable { showColorPicker = true }
                )

            }
        }

        // Botón cerrar sesión
        var logoutClicked by remember { mutableStateOf(false) }

        val logoutScale by animateFloatAsState(
            targetValue = if (logoutClicked) 1.1f else 1f,
            animationSpec = tween(durationMillis = 200),
            finishedListener = { logoutClicked = false }
        )

        Image(
            painter = painterResource(id = R.drawable.cerrar_sesion),
            contentDescription = "cerrar sesion",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (0).dp)
                .size(160.dp)
                .padding(top = 50.dp)
                .scale(logoutScale)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    logoutClicked = true
                    onLogout()
                }
        )

    }

    if (showColorPicker) {
        AlertDialog(
            onDismissRequest = { showColorPicker = false },
            title = {
                Text(
                    text = "Selecciona tu color",
                    fontFamily = FontFamily(Font(R.font.baloo_regular)),
                    fontSize = 26.sp,
                    color = Color(0xFF52154E)
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- Color Picker principal ---
                    HsvColorPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(2.dp, Color(0xFFCCCCCC), RoundedCornerShape(16.dp)),
                        controller = colorPickerController,
                        onColorChanged = { colorEnvelope ->
                            selectedColor = colorEnvelope.color
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // --- Barra de Brillo ---
                    BrightnessSlider(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)),
                        controller = colorPickerController
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // --- Barra de escala de grises ---
                    GrayScaleRow(
                        selectedColor = selectedColor,
                        onSelect = { selectedColor = it }
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- Vista previa del color ---
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(selectedColor, RoundedCornerShape(8.dp))
                                .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp))
                        )

                        Text(
                            text = selectedColor.toHexWithAlpha(),
                            fontFamily = FontFamily(Font(R.font.baloo_regular)),
                            fontSize = 16.sp,
                            color = Color(0xFF444444)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onChangeFondoPerfil(selectedColor.toHexWithAlpha())
                        showColorPicker = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF52154E))
                ) {
                    Text(
                        text = "Guardar",
                        fontFamily = FontFamily(Font(R.font.baloo_regular)),
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showColorPicker = false }) {
                    Text(
                        text = "Cancelar",
                        fontFamily = FontFamily(Font(R.font.baloo_regular)),
                        color = Color(0xFF666666)
                    )
                }
            }
        )
    }

}

fun Color.toHexWithAlpha(): String {
    val alpha = (alpha * 255).toInt().toString(16).padStart(2, '0').uppercase()
    val red = (red * 255).toInt().toString(16).padStart(2, '0').uppercase()
    val green = (green * 255).toInt().toString(16).padStart(2, '0').uppercase()
    val blue = (blue * 255).toInt().toString(16).padStart(2, '0').uppercase()
    return "#$alpha$red$green$blue"
}

@Composable
fun GrayScaleRow(
    selectedColor: Color,
    onSelect: (Color) -> Unit
) {
    val grays = List(10) { i -> Color.hsl(0f, 0f, i / 9f) } // 10 tonos de gris

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        grays.forEach { gray ->
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(gray, RoundedCornerShape(6.dp))
                    .border(
                        width = if (gray == selectedColor) 2.dp else 1.dp,
                        color = if (gray == selectedColor) Color(0xFF52154E) else Color.Gray,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable { onSelect(gray) }
            )
        }
    }
}



@Preview
@Composable
fun SettingsPreview(){
    Settings()
}