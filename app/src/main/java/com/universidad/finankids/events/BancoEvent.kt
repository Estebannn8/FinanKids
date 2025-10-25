package com.universidad.finankids.events

sealed class BancoEvent {
    object CargarBanco : BancoEvent()
    object CrearBancoInicial : BancoEvent()
    data class RegistrarPin(val pin: String) : BancoEvent()
    data class ValidarPin(val pin: String) : BancoEvent()
    data class Depositar(val monto: Int) : BancoEvent()
    data class Retirar(val monto: Int) : BancoEvent()

    object LimpiarMensajeOperacion : BancoEvent()
    object CalcularIntereses : BancoEvent()
    object ResetValidacion : BancoEvent()
    data class Error(val mensaje: String) : BancoEvent()
}