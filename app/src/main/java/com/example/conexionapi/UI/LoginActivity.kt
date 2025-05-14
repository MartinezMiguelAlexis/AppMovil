package com.example.conexionapi.UI

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.conexionapi.databinding.ActivityLoginBinding
import android.view.View
import com.example.conexionapi.utils.SecurePreferences

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: ProductosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verificar si ya hay un token guardado
        val token = SecurePreferences.getToken(this)
        if (!token.isNullOrEmpty()) {
            startProductosActivity(token)
            return
        }

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.authToken.observe(this) { token ->
            token?.let {
                SecurePreferences.saveToken(this, it)
                startProductosActivity(it)
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            binding.tvError.setText(error)
            binding.tvError.visibility = if (error.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                viewModel.loginUsuario(username, password)
            } else {
                Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                viewModel.registrarUsuario(username, password)
            } else {
                Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startProductosActivity(token: String) {
        val intent = Intent(this, ProductosActivity::class.java).apply {
            putExtra("TOKEN", token)
        }
        startActivity(intent)
        finish()
    }
}