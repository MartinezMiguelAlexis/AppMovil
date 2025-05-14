package com.example.conexionapi.data

data class Producto(
    val id: Int,
    val nombre: String,
    val cantidad: Double,
    val unidad: String,
    val fecha_compra: String
)