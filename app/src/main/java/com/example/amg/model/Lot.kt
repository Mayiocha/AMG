package com.example.amg.model

data class Lot(
    val id: Int,
    val quantity: Int,
    val stage: Int,
    val createdAt: String,
    // Conteo de animales por categoría
    val totalAnimals: Int = 0,
    val vacas: Int = 0,
    val toros: Int = 0,
    val terneras: Int = 0
)
