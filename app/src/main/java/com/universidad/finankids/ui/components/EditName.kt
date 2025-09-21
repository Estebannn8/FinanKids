package com.universidad.finankids.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.R
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.collectAsState
import com.universidad.finankids.viewmodel.UserViewModel

@Composable
fun EditNameOverlay(
    text: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    val userState by userViewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(150.dp),
        contentAlignment = Alignment.Center
    ) {
        // Imagen como contenedor
        Image(
            painter = painterResource(id = R.drawable.background_menu_name),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // Contenedor del input + nota
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = -(10).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicTextField(
                value = text,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontFamily = FontFamily(Font(R.font.baloo_regular)),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(35.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = "Escribe tu nombre...",
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.baloo_regular))
                            )
                        }
                        innerTextField()
                    }
                }
            )

            // Mensaje de error
            userState.errorMessage?.let { error ->
                Text(
                    text = error,
                    fontSize = 12.sp,
                    color = Color.Red,
                    fontFamily = FontFamily(Font(R.font.baloo_regular)),
                    modifier = Modifier
                        .padding(top = 4.dp, start = 4.dp)
                        .align(Alignment.Start)
                )
            }
        }

        // Botón guardar
        var saveClicked by remember { mutableStateOf(false) }
        val saveInteractionSource = remember { MutableInteractionSource() }
        val saveScale by animateFloatAsState(
            targetValue = if (saveClicked) 1.1f else 1f,
            animationSpec = tween(durationMillis = 200),
            finishedListener = { saveClicked = false }
        )

        Image(
            painter = painterResource(id = R.drawable.guardar),
            contentDescription = "Guardar",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(y = 20.dp, x = (-22).dp)
                .size(100.dp)
                .scale(saveScale)
                .clickable(
                    interactionSource = saveInteractionSource,
                    indication = null
                ) {
                    saveClicked = true
                    onSave()
                }
        )

        // Logo Cambiar usuario
        Image(
            painter = painterResource(id = R.drawable.cambiar_usuario),
            contentDescription = "Cambiar usuario",
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = (-80).dp)
                .size(200.dp)
        )
    }
}



