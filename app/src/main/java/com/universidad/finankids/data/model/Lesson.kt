package com.universidad.finankids.data.model

data class Lesson(
    val id: String = "",
    val title: String = "", // "ahorro", "compra", "basica", "inversion"
    val order: Int = 0,
    val baseExp: Int = 100,
    val baseDinero: Int = 500,
    val lives: Int = 5,
    val activities: List<ActivityContent> = emptyList(),
    val categoryId: String = ""
)