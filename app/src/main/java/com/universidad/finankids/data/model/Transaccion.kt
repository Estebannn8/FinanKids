package com.universidad.finankids.data.model

data class Transaccion(
    val tipo: String = "",
    val monto: Int = 0,
    val fecha: Long = System.currentTimeMillis()
)