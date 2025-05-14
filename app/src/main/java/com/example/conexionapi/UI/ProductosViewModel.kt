package com.example.conexionapi.UI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conexionapi.data.ApiClient
import com.example.conexionapi.data.ApiService
import com.example.conexionapi.data.AuthResponse
import com.example.conexionapi.data.LoginRequest
import com.example.conexionapi.data.Producto
import com.example.conexionapi.data.ProductoRequest
import com.example.conexionapi.data.RegistroRequest
import kotlinx.coroutines.launch
import retrofit2.Response

class ProductosViewModel : ViewModel() {
    private val apiService = ApiClient.instance.create(ApiService::class.java)

    private val _authToken = MutableLiveData<String?>()
    val authToken: LiveData<String?> = _authToken

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _productos = MutableLiveData<List<Producto>>()
    val productos: LiveData<List<Producto>> = _productos

    // Autenticación
    fun registrarUsuario(nombreUsuario: String, password: String) {
        viewModelScope.launch {
            try {
                val response = apiService.registrarUsuario(RegistroRequest(nombreUsuario, password))
                handleAuthResponse(response)
            } catch (e: Exception) {
                _errorMessage.postValue("Error de conexión: ${e.message}")
            }
        }
    }

    fun loginUsuario(nombreUsuario: String, password: String) {
        viewModelScope.launch {
            try {
                val response = apiService.loginUsuario(LoginRequest(nombreUsuario, password))
                handleAuthResponse(response)
            } catch (e: Exception) {
                _errorMessage.postValue("Error de conexión: ${e.message}")
            }
        }
    }

    private fun handleAuthResponse(response: Response<AuthResponse>) {
        if (response.isSuccessful) {
            _authToken.postValue(response.body()?.token)
        } else {
            _errorMessage.postValue("Error: ${response.errorBody()?.string()}")
        }
    }

    // Productos
    fun obtenerProductos(token: String) {
        viewModelScope.launch {
            try {
                val response = apiService.obtenerProductos(token)
                if (response.isSuccessful) {
                    _productos.postValue(response.body())
                } else {
                    _errorMessage.postValue("Error al obtener productos")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error de conexión: ${e.message}")
            }
        }
    }

    fun crearProducto(token: String, producto: ProductoRequest) {
        viewModelScope.launch {
            try {
                val response = apiService.crearProducto(token, producto)
                if (response.isSuccessful) {
                    obtenerProductos(token) // Actualizar lista
                } else {
                    _errorMessage.postValue("Error al crear producto")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error de conexión: ${e.message}")
            }
        }
    }

    // Actualizar
    fun actualizarProducto(token: String, id: Int, productoRequest: ProductoRequest) {
        viewModelScope.launch {
            try {
                val response = apiService.actualizarProducto(
                    token = token,
                    id = id,
                    producto = productoRequest
                )
                if (response.isSuccessful) {
                    obtenerProductos(token) // Refresca la lista
                } else {
                    _errorMessage.postValue("Error al actualizar: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun eliminarProducto(token: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.eliminarProducto(token, id)
                if (response.isSuccessful) {
                    obtenerProductos(token) // Actualiza la lista
                } else {
                    _errorMessage.postValue("Error al eliminar: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error de red: ${e.message}")
            }
        }
    }

}