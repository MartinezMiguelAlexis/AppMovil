package com.example.conexionapi.data

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Autenticación
    @POST("registro")
    suspend fun registrarUsuario(@Body request: RegistroRequest): Response<AuthResponse>

    @POST("login")
    suspend fun loginUsuario(@Body request: LoginRequest): Response<AuthResponse>

    // Productos
    @GET("productos")
    suspend fun obtenerProductos(@Header("Authorization") token: String): Response<List<Producto>>

    @POST("productos")
    suspend fun crearProducto(
        @Header("Authorization") token: String,
        @Body producto: ProductoRequest
    ): Response<Producto>

    @PUT("productos/{id}")
    suspend fun actualizarProducto(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body producto: ProductoRequest
    ): Response<Void>

    @DELETE("productos/{id}")
    suspend fun eliminarProducto(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Void>

    @GET("productos/{id}")
    suspend fun obtenerProducto(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Producto>
}

// Modelos de datos
data class RegistroRequest(val nombreUsuario: String, val password: String)
data class LoginRequest(val nombreUsuario: String, val password: String)
data class AuthResponse(val token: String, val usuario: Usuario)

data class Usuario(
    val id: Int,
    val nombreUsuario: String,
    @SerializedName("es_admin") val esAdminInt: Int  // Recibe 0 o 1
) {
    val esAdmin: Boolean
        get() = esAdminInt == 1  // Convierte a booleano cuando lo necesites
}