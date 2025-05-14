package com.example.conexionapi.ui.theme
import com.example.conexionapi.data.Producto
import com.example.conexionapi.data.ProductoRequest

fun Producto.toRequest(): ProductoRequest {
    return ProductoRequest(
        nombre = this.nombre,
        cantidad = this.cantidad,
        unidad = this.unidad,
        fecha_compra = this.fecha_compra
    )
}