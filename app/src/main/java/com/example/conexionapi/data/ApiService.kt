package com.example.conexionapi.data

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Autenticación (existente)
    @POST("registro")
    suspend fun registrarUsuario(@Body request: RegistroRequest): Response<AuthResponse>

    @POST("login")
    suspend fun loginUsuario(@Body request: LoginRequest): Response<AuthResponse>

    // Productos (existente con precio añadido)
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

    // Nuevos endpoints para pagos
    @POST("crear-sesion-pago")
    suspend fun crearSesionPago(
        @Header("Authorization") token: String,
        @Body request: CrearSesionPagoRequest
    ): Response<PaymentResponse>

    // Nueva clase para el cuerpo de la petición
    data class CrearSesionPagoRequest(
        val productos: List<ProductoPagoRequest>
    )

    @GET("verificar-pago/{sessionId}")
    suspend fun verificarPago(
        @Header("Authorization") token: String,
        @Path("sessionId") sessionId: String
    ): Response<PaymentStatus>
}

// Modelos existentes
data class RegistroRequest(val nombreUsuario: String, val password: String)
data class LoginRequest(val nombreUsuario: String, val password: String)
data class AuthResponse(val token: String, val usuario: Usuario)


// Nuevos modelos para pagos
data class PaymentResponse(
    val id: String,
    val url: String,
    val monto_total: Double
)

data class CrearSesionPagoRequest(
    val productos: List<ProductoPagoRequest>
)

data class PaymentStatus(
    val status: String,
    val amount_total: Double
)

data class Usuario(
    val id: Int,
    val nombreUsuario: String,
    @SerializedName("es_admin") val esAdminInt: Int
) {
    val esAdmin: Boolean
        get() = esAdminInt == 1
}