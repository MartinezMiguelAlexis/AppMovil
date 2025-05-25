package com.example.conexionapi.data

import com.google.gson.annotations.SerializedName

data class Producto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("precio") val precio: Double,
    @SerializedName("cantidad") val cantidad: Double,
    @SerializedName("unidad") val unidad: String,
    @SerializedName("fecha_compra") val fecha_compra: String
)