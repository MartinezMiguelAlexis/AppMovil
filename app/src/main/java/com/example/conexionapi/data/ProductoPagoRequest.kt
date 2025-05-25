package com.example.conexionapi.data

import com.google.gson.annotations.SerializedName

data class ProductoPagoRequest(
    @SerializedName("productId") val productId: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("precio") val precio: Double,
    @SerializedName("cantidad") val cantidad: Int
)