package com.example.conexionapi.UI.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import com.example.conexionapi.R
import com.example.conexionapi.data.Producto
import com.example.conexionapi.data.ProductoRequest

object ProductoDialog {

    fun showAddDialog(
        context: Context,
        onSave: (ProductoRequest) -> Unit
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_producto, null)
        val etNombre = view.findViewById<EditText>(R.id.etNombre)
        val etCantidad = view.findViewById<EditText>(R.id.etCantidad)
        val etUnidad = view.findViewById<EditText>(R.id.etUnidad)
        val etFecha = view.findViewById<EditText>(R.id.etFecha)

        AlertDialog.Builder(context)
            .setTitle("Agregar Producto")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString()
                val cantidad = etCantidad.text.toString().toDoubleOrNull() ?: 0.0
                val unidad = etUnidad.text.toString()
                val fecha = etFecha.text.toString().takeIf { it.isNotEmpty() }

                if (nombre.isNotEmpty() && cantidad > 0 && unidad.isNotEmpty()) {
                    onSave(ProductoRequest(nombre, cantidad, unidad, fecha))
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    fun showEditDialog(
        context: Context,
        producto: Producto,
        onSave: (Producto) -> Unit,
        onDelete: () -> Unit
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_producto, null)
        val etNombre = view.findViewById<EditText>(R.id.etNombre)
        val etCantidad = view.findViewById<EditText>(R.id.etCantidad)
        val etUnidad = view.findViewById<EditText>(R.id.etUnidad)
        val etFecha = view.findViewById<EditText>(R.id.etFecha)

        etNombre.setText(producto.nombre)
        etCantidad.setText(producto.cantidad.toString())
        etUnidad.setText(producto.unidad)
        etFecha.setText(producto.fecha_compra.split("T")[0])

        AlertDialog.Builder(context)
            .setTitle("Editar Producto")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString()
                val cantidad = etCantidad.text.toString().toDoubleOrNull() ?: 0.0
                val unidad = etUnidad.text.toString()
                val fecha = etFecha.text.toString().takeIf { it.isNotEmpty() }

                if (nombre.isNotEmpty() && cantidad > 0 && unidad.isNotEmpty()) {
                    onSave(producto.copy(
                        nombre = nombre,
                        cantidad = cantidad,
                        unidad = unidad,
                        fecha_compra = fecha ?: producto.fecha_compra
                    ))
                }
            }
            .setNegativeButton("Cancelar", null)
            .setNeutralButton("Eliminar") { _, _ ->
                onDelete()
            }
            .show()
    }
}