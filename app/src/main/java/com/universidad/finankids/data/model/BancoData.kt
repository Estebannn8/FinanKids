package com.universidad.finankids.data.model
data class BancoData(
    val saldo: Int = 0,
    val pin: String? = null,
    val interes: Double = 0.02, // 2% mensual
    val ultimaActualizacion: Long = System.currentTimeMillis(),
    val historial: List<Transaccion> = emptyList()
)