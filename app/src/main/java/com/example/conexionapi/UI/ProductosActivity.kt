package com.example.conexionapi.UI

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.conexionapi.UI.dialogs.ProductoDialog
import com.example.conexionapi.UI.adapters.ProductosAdapter
import com.example.conexionapi.R
import com.example.conexionapi.utils.SecurePreferences
import com.example.conexionapi.databinding.ActivityProductosBinding
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
    }

    private fun setupRecyclerView() {
        adapter = ProductosAdapter { producto ->
            // Abrir diálogo para editar producto
            ProductoDialog.showEditDialog(
                this,
                producto,
                onSave = { updatedProduct ->
                    viewModel.actualizarProducto(token, updatedProduct.id, updatedProduct.toRequest())
                },
                onDelete = {
                    viewModel.eliminarProducto(token, producto.id)
                }
            )
        }

        binding.rvProductos.layoutManager = LinearLayoutManager(this)
        binding.rvProductos.adapter = adapter
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
    }

    private fun setupListeners() {
        binding.fabAddProducto.setOnClickListener {
            ProductoDialog.showAddDialog(this) { newProduct ->
                viewModel.crearProducto(token, newProduct)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_productos, menu)
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
            else -> super.onOptionsItemSelected(item)
        }
    }
}