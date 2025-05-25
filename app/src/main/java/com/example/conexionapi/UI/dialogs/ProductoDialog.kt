package com.example.conexionapi.UI.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
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
        val etPrecio = view.findViewById<EditText>(R.id.etPrecio)
        val etCantidad = view.findViewById<EditText>(R.id.etCantidad)
        val etUnidad = view.findViewById<EditText>(R.id.etUnidad)
        val etFecha = view.findViewById<EditText>(R.id.etFecha)

        AlertDialog.Builder(context)
            .setTitle("Agregar Producto")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString()
                val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
                val cantidad = etCantidad.text.toString().toDoubleOrNull() ?: 0.0
                val unidad = etUnidad.text.toString()
                val fecha = etFecha.text.toString().takeIf { it.isNotEmpty() }

                if (nombre.isNotEmpty() && precio >= 0 && cantidad > 0 && unidad.isNotEmpty()) {
                    onSave(ProductoRequest(
                        nombre = nombre,
                        precio = precio,
                        cantidad = cantidad,
                        unidad = unidad,
                        fecha_compra = fecha
                    ))
                } else {
                    Toast.makeText(context, "Complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
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
        val etPrecio = view.findViewById<EditText>(R.id.etPrecio)
        val etCantidad = view.findViewById<EditText>(R.id.etCantidad)
        val etUnidad = view.findViewById<EditText>(R.id.etUnidad)
        val etFecha = view.findViewById<EditText>(R.id.etFecha)

        etNombre.setText(producto.nombre)
        etPrecio.setText(producto.precio.toString())
        etCantidad.setText(producto.cantidad.toString())
        etUnidad.setText(producto.unidad)
        etFecha.setText(producto.fecha_compra.split("T")[0])

        AlertDialog.Builder(context)
            .setTitle("Editar Producto")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString()
                val precio = etPrecio.text.toString().toDoubleOrNull() ?: producto.precio
                val cantidad = etCantidad.text.toString().toDoubleOrNull() ?: producto.cantidad
                val unidad = etUnidad.text.toString()
                val fecha = etFecha.text.toString().takeIf { it.isNotEmpty() }

                if (nombre.isNotEmpty() && precio >= 0 && cantidad > 0 && unidad.isNotEmpty()) {
                    onSave(producto.copy(
                        nombre = nombre,
                        precio = precio,
                        cantidad = cantidad,
                        unidad = unidad,
                        fecha_compra = fecha ?: producto.fecha_compra
                    ))
                } else {
                    Toast.makeText(context, "Complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .setNeutralButton("Eliminar") { _, _ ->
                onDelete()
            }
            .show()
    }
}