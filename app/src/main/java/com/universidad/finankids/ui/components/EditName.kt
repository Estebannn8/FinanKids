package com.universidad.finankids.ui.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .background(Color.Black.copy(alpha = 0.3f)),
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
                painter = painterResource(id = R.drawable.background_menu_name),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds
            )

            // 🔹 Contenedor del input + nota
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = -(10).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo de texto editable
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

                // 🔹 Texto informativo debajo
                Text(
                    text = "Solo puedes cambiarlo una vez",
                    fontSize = 12.sp,
                    color = Color.Gray.copy(alpha = 0.8f),
                    fontFamily = FontFamily(Font(R.font.baloo_regular)),
                    modifier = Modifier
                        .padding(top = 4.dp, start = 4.dp)
                        .align(Alignment.Start)
                )
            }

            // 🔹 Botón guardar
            Image(
                painter = painterResource(id = R.drawable.guardar),
                contentDescription = "Guardar",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(y = 20.dp, x = (-22).dp)
                    .padding(8.dp)
                    .size(100.dp)
                    .clickable { /* Acción guardar */ }
            )

            // Cambiar usuario logo
            Image(
                painter = painterResource(id = R.drawable.cambiar_usuario),
                contentDescription = "Cambiar usuario",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(y = (-80).dp)
                    .padding(0.dp)
                    .size(200.dp)
                    .clickable { onDismiss() }
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
