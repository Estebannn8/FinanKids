package com.universidad.finankids.data.model

data class Actividad(
    val tipo: String = "",                 // "enseñanza", "completar", "ordenar", "parejas", "seleccion_multiple", "arrastrar"
    val contenido: Map<String, Any> = emptyMap()  // Contenido específico según el tipo
)
