package com.universidad.finankids.data.model

data class LogroUI(
    val logro: Logro,
    val desbloqueado: Boolean,
    val reclamado: Boolean,
    val progresoActual: Int,
    val progresoTotal: Int
)
