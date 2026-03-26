package com.example.amg.model

data class Animal(
    val id: Int? = null,
    val tagId: String,
    val lotId: Int,
    val category: String, // Aquí guardaremos "Vaca", "Toro" o "Ternera"
    val race: String,
    val weight: Float,
    val monthOld: Int,
    val isHealthy: Boolean,
)