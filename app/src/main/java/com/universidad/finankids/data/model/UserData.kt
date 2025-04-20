package com.universidad.finankids.data.model

data class UserData(
    val uid: String = "",
    val nickname: String = "",
    val correo: String = "",
    val avatarActual: String = "",
    val marcoActual: String = "",
    val avataresDesbloqueados: List<String> = emptyList(),
    val marcosDesbloqueados: List<String> = emptyList(),
    val nivel: Int = 1,
    val exp: Int = 0,
    val dinero: Int = 0,
    val logros: List<String> = emptyList(),
    val insignias: List<String> = emptyList(),
    val progresoCategorias: Map<String, Int> = emptyMap(),
    val leccionesCompletadas: Map<String, Any> = emptyMap(),
    val racha: Map<String, Any?> = emptyMap()
)