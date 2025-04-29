package com.universidad.finankids.data.model

data class Leccion(
    val id: String = "",                  // ID de la lección
    val nombre: String = "",               // Nombre visible ("Ahorra tu primer dólar")
    val descripcion: String = "",          // Descripción opcional
    val expBase: Int = 100,                // EXP que otorga si se completa perfecto
    val coinsBase: Int = 50,               // Dinero base que otorga
    val vidasMaximas: Int = 5,             // Siempre 5 en tu caso
    val actividades: List<Actividad> = emptyList(), // Lista ordenada de actividades
    val orden: Int = 1                     // Orden para saber qué lección sigue en la categoría
)
