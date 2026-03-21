package com.example.amg

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistorialTolvaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_tolva)

        val rvHistorialTolva = findViewById<RecyclerView>(R.id.rvHistorialTolva)
        val btnVolverHistorialTolva = findViewById<Button>(R.id.btnVolverHistorialTolva)

        val listaHistorial = listOf(
            hashMapOf(
                "id" to "1",
                "nombreMezcla" to "Mezcla Engorda 1",
                "fecha" to "21/03/2026",
                "hora" to "08:00 AM",
                "cantidad" to "25",
                "unidad" to "kg",
                "estado" to "Dispensado",
                "observaciones" to "Sin novedad"
            ),
            hashMapOf(
                "id" to "2",
                "nombreMezcla" to "Mezcla Lechera",
                "fecha" to "21/03/2026",
                "hora" to "12:00 PM",
                "cantidad" to "18",
                "unidad" to "kg",
                "estado" to "Dispensado",
                "observaciones" to "Carga normal"
            ),
            hashMapOf(
                "id" to "3",
                "nombreMezcla" to "Mezcla Crecimiento",
                "fecha" to "21/03/2026",
                "hora" to "06:00 PM",
                "cantidad" to "20",
                "unidad" to "kg",
                "estado" to "Pendiente",
                "observaciones" to "Programado para la tarde"
            )
        )

        rvHistorialTolva.layoutManager = LinearLayoutManager(this)
        rvHistorialTolva.adapter = HistorialTolvaAdapter(listaHistorial)

        btnVolverHistorialTolva.setOnClickListener {
            finish()
        }
    }
}