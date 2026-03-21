package com.example.amg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistorialTolvaAdapter(
    private val listaHistorial: List<HashMap<String, String>>
) : RecyclerView.Adapter<HistorialTolvaAdapter.HistorialTolvaViewHolder>() {

    class HistorialTolvaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItemIdHistorial: TextView = itemView.findViewById(R.id.tvItemIdHistorial)
        val tvItemNombreMezclaHistorial: TextView = itemView.findViewById(R.id.tvItemNombreMezclaHistorial)
        val tvItemFechaHoraHistorial: TextView = itemView.findViewById(R.id.tvItemFechaHoraHistorial)
        val tvItemCantidadUnidadHistorial: TextView = itemView.findViewById(R.id.tvItemCantidadUnidadHistorial)
        val tvItemEstadoHistorial: TextView = itemView.findViewById(R.id.tvItemEstadoHistorial)
        val tvItemObservacionesHistorial: TextView = itemView.findViewById(R.id.tvItemObservacionesHistorial)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialTolvaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historial_tolva, parent, false)
        return HistorialTolvaViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistorialTolvaViewHolder, position: Int) {
        val item = listaHistorial[position]

        val id = item["id"] ?: ""
        val nombreMezcla = item["nombreMezcla"] ?: ""
        val fecha = item["fecha"] ?: ""
        val hora = item["hora"] ?: ""
        val cantidad = item["cantidad"] ?: ""
        val unidad = item["unidad"] ?: ""
        val estado = item["estado"] ?: ""
        val observaciones = item["observaciones"] ?: ""

        holder.tvItemIdHistorial.text = "ID: $id"
        holder.tvItemNombreMezclaHistorial.text = nombreMezcla
        holder.tvItemFechaHoraHistorial.text = "Fecha: $fecha  |  Hora: $hora"
        holder.tvItemCantidadUnidadHistorial.text = "Cantidad: $cantidad $unidad"
        holder.tvItemEstadoHistorial.text = "Estado: $estado"
        holder.tvItemObservacionesHistorial.text = "Observaciones: $observaciones"
    }

    override fun getItemCount(): Int {
        return listaHistorial.size
    }
}