package com.universidad.finankids.state

import com.universidad.finankids.data.model.BancoData
data class BancoState(
    val banco: BancoData? = null,
    val loading: Boolean = false,
    val pinValidado: Boolean = false,
    val mensaje: String? = null,
    val errorMessage: String? = null,
    val mensajeOperacion: String? = null,
    val errorOperacion: String? = null
)