package com.universidad.finankids.data.model

data class Logro(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val dineroRecompensa: Int = 0,
    val progresoRequerido: Int? = null,
    val tipo: String = "",
    val iconoUrl: String = "",
    val orden: Int = 0
)
