package com.universidad.finankids.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.universidad.finankids.R

@Composable
fun EditNameOverlay(
    text: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.0f)), // Fondo oscuro semi-transparente
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            // 🔹 Imagen como contenedor
            Image(
                painter = painterResource(id = R.drawable.background_menu),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds
            )

            // 🔹 Texto editable encima de la imagen


            // 🔹 Botón guardar (dentro en esquina inferior derecha)
            Image(
                painter = painterResource(id = R.drawable.guardar),
                contentDescription = "Guardar",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(y = (20).dp, x = (-10).dp)
                    .padding(8.dp)
                    .size(100.dp)
                    .clickable { /* Acción guardar */ }
            )

            // 🔹 Botón cambiar usuario (sobresalido arriba a la izquierda)
            Image(
                painter = painterResource(id = R.drawable.cambiar_usuario),
                contentDescription = "Cambiar usuario",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(y = (-80).dp) // sobresale del contenedor
                    .padding(0.dp)
                    .size(200.dp)
                    .clickable { onDismiss() } // Aquí podrías usarlo como "cerrar/dismiss"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun EditNameOverlayPreview() {
    EditNameOverlay(
        text = "VALENTINA",
        onValueChange = {},
        onDismiss = {}
    )
}
