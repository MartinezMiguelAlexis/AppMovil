package com.example.conexionapi.UI

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.conexionapi.R
import com.example.conexionapi.UI.adapters.ProductosAdapter
import com.example.conexionapi.UI.dialogs.ProductoDialog
import com.example.conexionapi.data.Producto
import com.example.conexionapi.data.ProductoPagoRequest
import com.example.conexionapi.databinding.ActivityProductosBinding
import com.example.conexionapi.utils.SecurePreferences
import com.example.conexionapi.utils.toRequest

class ProductosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductosBinding
    private val viewModel: ProductosViewModel by viewModels()
    private lateinit var token: String
    private lateinit var adapter: ProductosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = intent.getStringExtra("TOKEN") ?: run {
            Toast.makeText(this, "Sesión inválida", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupToolbar()
        setupRecyclerView()
        setupObservers()
        setupListeners()

        viewModel.obtenerProductos(token)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        adapter = ProductosAdapter(
            onItemClick = { producto ->
                ProductoDialog.showEditDialog(
                    this,
                    producto,
                    onSave = { updatedProduct ->
                        viewModel.actualizarProducto(token, updatedProduct.id, updatedProduct.toRequest())
                    },
                    onDelete = {
                        showDeleteConfirmationDialog(producto)
                    }
                )
            },
            onQuantityChange = { producto, cantidad ->
                // Opcional: puedes manejar cambios en la cantidad aquí
            }
        )

        binding.rvProductos.apply {
            layoutManager = LinearLayoutManager(this@ProductosActivity)
            adapter = this@ProductosActivity.adapter
        }
    }

    private fun setupObservers() {
        viewModel.productos.observe(this) { productos ->
            adapter.submitList(productos)
        }

        viewModel.errorMessage.observe(this) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.paymentUrl.observe(this) { url ->
            url?.let {
                openStripeCheckout(it)
            }
        }
    }

    private fun setupListeners() {
        binding.fabAddProducto.setOnClickListener {
            ProductoDialog.showAddDialog(this) { newProduct ->
                viewModel.crearProducto(token, newProduct)
            }
        }
    }

    private fun showDeleteConfirmationDialog(producto: Producto) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar producto")
            .setMessage("¿Estás seguro de eliminar ${producto.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.eliminarProducto(token, producto.id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun openStripeCheckout(url: String) {
        try {
            Log.d("StripeCheckout", "Intentando abrir URL: $url") // Log para depuración

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                // Añade flags para asegurar que se abre en una nueva tarea
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                // Opcional: paquete del navegador por defecto
                setPackage("com.android.chrome")
            }

            // Verifica si hay una aplicación que pueda manejar el intent
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // Fallback sin especificar paquete
                val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(fallbackIntent)
            }
        } catch (e: Exception) {
            Log.e("StripeCheckout", "Error al abrir URL", e) // Log detallado
            Toast.makeText(
                this,
                "Error al abrir el pago. Por favor instala un navegador web",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_productos, menu)
        menuInflater.inflate(R.menu.menu_pagos, menu) // Añade el menú de pagos
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                SecurePreferences.clearToken(this)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            R.id.action_pay -> {
                val seleccionados = adapter.getProductosSeleccionados()
                if (seleccionados.isNotEmpty()) {
                    if (seleccionados.any { it.first.precio <= 0 }) {
                        Toast.makeText(this, "Algunos productos no tienen precio válido", Toast.LENGTH_SHORT).show()
                        true
                    } else {
                        val productosParaPago = seleccionados.map { (producto, cantidad) ->
                            ProductoPagoRequest(
                                productId = producto.id,
                                nombre = producto.nombre,
                                precio = producto.precio,
                                cantidad = cantidad
                            )
                        }
                        viewModel.iniciarPago(token, productosParaPago)
                        true
                    }
                } else {
                    Toast.makeText(this, "Selecciona al menos un producto", Toast.LENGTH_SHORT).show()
                    true
                }
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}