package com.universidad.finankids.ui.Components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.R

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: Painter,
    isPassword: Boolean = false,
    showCharacterCounter: Boolean = false,
    maxLength: Int = Int.MAX_VALUE
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= maxLength) {
                onValueChange(newValue)
            }
        },
        placeholder = { Text(placeholder) },
        leadingIcon = {
            Icon(painter = leadingIcon, contentDescription = null, tint = Color.Unspecified)
        },
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Mostrar contador si está habilitado
                if (showCharacterCounter) {
                    Text(
                        text = "${value.length}/$maxLength",
                        color = if (value.length > maxLength) Color.Red else Color.Gray,
                        fontSize = 12.sp
                    )
                }

                // Mostrar icono de visibilidad si es campo de contraseña
                if (isPassword) {
                    val visibilityIcon = if (passwordVisible)
                        painterResource(id = R.drawable.ic_visibility_24)
                    else
                        painterResource(id = R.drawable.ic_visibility_off)

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = visibilityIcon,
                            contentDescription = "Toggle Password Visibility",
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        },
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color(0xFF52154E),
                shape = RoundedCornerShape(8.29.dp)
            ),
        shape = RoundedCornerShape(8.29.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        var username by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        CustomTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = "Usuario",
            leadingIcon = painterResource(id = R.drawable.ic_person)
        )

        CustomTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Correo electrónico",
            leadingIcon = painterResource(id = R.drawable.ic_email)
        )

        CustomTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Contraseña",
            leadingIcon = painterResource(id = R.drawable.ic_password),
            isPassword = true
        )
    }
}


