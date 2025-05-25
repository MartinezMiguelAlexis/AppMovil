package com.example.conexionapi.UI.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.conexionapi.R
import com.example.conexionapi.data.Producto

class ProductosAdapter(
    private val onItemClick: (Producto) -> Unit,
    private val onQuantityChange: (Producto, Int) -> Unit // Cambiado para manejar cantidad
) : ListAdapter<Producto, ProductosAdapter.ProductoViewHolder>(ProductoDiffCallback()) {

    private val productosSeleccionados = mutableMapOf<Int, Int>() // Map<productId, cantidad>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getProductosSeleccionados(): List<Pair<Producto, Int>> {
        return currentList.filter { productosSeleccionados.containsKey(it.id) }
            .map { it to (productosSeleccionados[it.id] ?: 1) }
    }

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        private val tvCantidad: TextView = itemView.findViewById(R.id.tvCantidad)
        private val tvUnidad: TextView = itemView.findViewById(R.id.tvUnidad)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        private val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
        private val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
        private val etCantidad: EditText = itemView.findViewById(R.id.etCantidad) // Nuevo

        fun bind(producto: Producto) {
            tvNombre.text = producto.nombre
            tvCantidad.text = "Stock: ${producto.cantidad}"
            tvUnidad.text = "Unidad: ${producto.unidad}"
            tvFecha.text = "Comprado: ${producto.fecha_compra.split("T")[0]}"
            tvPrecio.text = "Precio: $${"%.2f".format(producto.precio)}"

            checkbox.isChecked = productosSeleccionados.containsKey(producto.id)
            etCantidad.setText(productosSeleccionados[producto.id]?.toString() ?: "1")
            etCantidad.visibility = if (checkbox.isChecked) View.VISIBLE else View.GONE

            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    productosSeleccionados[producto.id] = 1
                    etCantidad.visibility = View.VISIBLE
                } else {
                    productosSeleccionados.remove(producto.id)
                    etCantidad.visibility = View.GONE
                }
            }

            etCantidad.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val cantidad = s.toString().toIntOrNull() ?: 1
                    if (cantidad > 0 && cantidad <= producto.cantidad) {
                        productosSeleccionados[producto.id] = cantidad
                        onQuantityChange(producto, cantidad)
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            itemView.setOnClickListener {
                if (!isCheckboxClick(it)) {
                    onItemClick(producto)
                }
            }
        }



        private fun isCheckboxClick(view: View): Boolean {
            return view is CheckBox || (view.parent is ViewGroup && (view.parent as ViewGroup).id == R.id.checkbox)
        }
    }

    class ProductoDiffCallback : DiffUtil.ItemCallback<Producto>() {
        override fun areItemsTheSame(oldItem: Producto, newItem: Producto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Producto, newItem: Producto): Boolean {
            return oldItem == newItem
        }
    }
}