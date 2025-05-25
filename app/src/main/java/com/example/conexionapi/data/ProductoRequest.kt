package com.example.conexionapi.data

data class ProductoRequest(
    val nombre: String,
    val precio: Double,
    val cantidad: Double,
    val unidad: String,
    val fecha_compra: String? = null
)