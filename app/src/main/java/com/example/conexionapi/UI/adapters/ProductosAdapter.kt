package com.example.conexionapi.UI.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.conexionapi.R
import com.example.conexionapi.data.Producto

class ProductosAdapter(
    private val onItemClick: (Producto) -> Unit
) : ListAdapter<Producto, ProductosAdapter.ProductoViewHolder>(ProductoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        private val tvCantidad: TextView = itemView.findViewById(R.id.tvCantidad)
        private val tvUnidad: TextView = itemView.findViewById(R.id.tvUnidad)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)

        fun bind(producto: Producto) {
            tvNombre.text = producto.nombre
            tvCantidad.text = producto.cantidad.toString()
            tvUnidad.text = producto.unidad
            tvFecha.text = producto.fecha_compra.split("T")[0] // Mostrar solo la fecha

            itemView.setOnClickListener { onItemClick(producto) }
        }
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