package com.example.conexionapi.UI

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.conexionapi.R
import com.example.conexionapi.databinding.ActivityPagoResultBinding

class PagoResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPagoResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagoResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handlePaymentResult(intent?.data)
    }

    private fun handlePaymentResult(uri: Uri?) {
        uri?.let {
            // Extraer parámetros de la URL
            val sessionId = it.getQueryParameter("session_id")
            val success = it.getQueryParameter("success")

            // Aquí verificas el pago con tu backend
            if (success == "true" && !sessionId.isNullOrEmpty()) {
                showPaymentResult(true)
            } else {
                showPaymentResult(false)
            }
        } ?: run {
            showPaymentResult(false)
        }
    }

    private fun showPaymentResult(success: Boolean) {
        if (success) {
            binding.textResult.text = "¡Pago exitoso!"
            binding.imageResult.setImageResource(R.drawable.ic_payment_success)
        } else {
            binding.textResult.text = "Pago fallido"
            binding.imageResult.setImageResource(R.drawable.ic_payment_failed)
        }
    }
}