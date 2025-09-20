package com.universidad.finankids.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.universidad.finankids.R

@Composable
fun BottomMenu(
    isHomeSection: Boolean,  // Nuevo parámetro para distinguir Home de otras pantallas
    sectionColor: String,     // Color de la sección (solo se usa en Home)
    menuBackgroundColor: androidx.compose.ui.graphics.Color,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    val navIconNames = listOf("perfil", "trofeo", "inicio", "banco", "tienda")

    val iconMap = mapOf(
        "morado_perfil" to R.drawable.ic_morado_perfil,
        "morado_trofeo" to R.drawable.ic_morado_trofeo,
        "morado_inicio" to R.drawable.ic_ciudad_menu,
        "morado_banco" to R.drawable.ic_morado_inicio,
        "morado_tienda" to R.drawable.ic_morado_tienda,

        "azul_perfil" to R.drawable.ic_azul_perfil,
        "azul_trofeo" to R.drawable.ic_azul_trofeo,
        "azul_inicio" to R.drawable.ic_ciudad_menu,
        "azul_banco" to R.drawable.ic_azul_inicio,
        "azul_tienda" to R.drawable.ic_azul_tienda,

        "amarillo_perfil" to R.drawable.ic_amarillo_perfil,
        "amarillo_trofeo" to R.drawable.ic_amarillo_trofeo,
        "amarillo_inicio" to R.drawable.ic_ciudad_menu,
        "amarillo_banco" to R.drawable.ic_amarillo_inicio,
        "amarillo_tienda" to R.drawable.ic_amarillo_tienda,

        "naranja_perfil" to R.drawable.ic_naranja_perfil,
        "naranja_trofeo" to R.drawable.ic_naranja_trofeo,
        "naranja_inicio" to R.drawable.ic_ciudad_menu,
        "naranja_banco" to R.drawable.ic_naranja_inicio,
        "naranja_tienda" to R.drawable.ic_naranja_tienda,

        "gris_inicio" to R.drawable.ic_ciudad_menu
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(
                color = menuBackgroundColor,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navIconNames.forEach { iconName ->
                // Lógica mejorada para determinar el color
                val colorKey = when {
                    // En Home, usa el color de la sección para todos los íconos
                    isHomeSection && selectedItem == "inicio" -> sectionColor
                    // En otras pantallas, usa colores fijos según el ícono
                    else -> getStaticColorForIcon(iconName)
                }

                val key = "${colorKey}_$iconName"
                val iconResId = iconMap[key] ?: R.drawable.placeholder

                val isSelected = selectedItem == iconName

                val animatedSize by animateDpAsState(
                    targetValue = if (isSelected) 56.dp else 42.dp,
                    label = "IconSizeAnimation"
                )

                Box(
                    modifier = Modifier
                        .height(56.dp)
                ) {
                    IconButton(
                        onClick = { onItemSelected(iconName) },
                        modifier = Modifier
                            .size(animatedSize)
                            .align(Alignment.Center)
                    ) {
                        Image(
                            painter = painterResource(id = iconResId),
                            contentDescription = iconName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}

// Función sin cambios
fun getStaticColorForIcon(iconName: String): String {
    return when (iconName) {
        "perfil" -> "morado"
        "trofeo" -> "amarillo"
        "inicio" -> "gris"
        "banco" -> "naranja"
        "tienda" -> "azul"
        else -> "morado"
    }
}
