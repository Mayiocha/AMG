package com.example.amg.model

data class Rutina(
    val id: Int = 0,
    val nombre: String,
    val hora: String,        // formato "HH:mm"
    val mixtureId: Int,
    val mixtureName: String, // nombre de la mezcla (para display)
    val cantidadKg: Float,
    val activa: Boolean
)
