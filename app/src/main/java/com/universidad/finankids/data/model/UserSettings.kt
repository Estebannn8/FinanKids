package com.universidad.finankids.data.model

data class UserSettings(
    val uid: String = "",
    val musicaActiva: Boolean = true,
    val sonidoActiva: Boolean = true,
    val colorFondoPerfil: String = "#FFDCDEE2" // Color por defecto
)