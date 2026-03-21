package com.example.amg

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ListaMezclasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_mezclas)

        val contenedorMezclas = findViewById<LinearLayout>(R.id.contenedorMezclas)
        val btnVolverListaMezclas = findViewById<Button>(R.id.btnVolverListaMezclas)

        val listaMezclas = listOf(
            hashMapOf(
                "nombre" to "Mezcla Engorda 1",
                "tipo" to "Engorda",
                "cantidadTotal" to "50",
                "unidad" to "kg",
                "estado" to "Activa"
            ),
            hashMapOf(
                "nombre" to "Mezcla Lechera",
                "tipo" to "Producción",
                "cantidadTotal" to "35",
                "unidad" to "kg",
                "estado" to "Activa"
            ),
            hashMapOf(
                "nombre" to "Mezcla Crecimiento",
                "tipo" to "Desarrollo",
                "cantidadTotal" to "20",
                "unidad" to "kg",
                "estado" to "Inactiva"
            )
        )

        for (mezcla in listaMezclas) {
            val tarjeta = LinearLayout(this)
            tarjeta.orientation = LinearLayout.VERTICAL
            tarjeta.setPadding(32, 24, 32, 24)

            val paramsTarjeta = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            paramsTarjeta.bottomMargin = 24
            tarjeta.layoutParams = paramsTarjeta
            tarjeta.setBackgroundColor(android.graphics.Color.parseColor("#F5F5F5"))

            val tvNombre = TextView(this)
            tvNombre.text = mezcla["nombre"]
            tvNombre.textSize = 18f
            tvNombre.setTextColor(android.graphics.Color.parseColor("#2E7D32"))
            tvNombre.setTypeface(null, android.graphics.Typeface.BOLD)

            val tvTipo = TextView(this)
            tvTipo.text = "Tipo: ${mezcla["tipo"]}"
            tvTipo.textSize = 16f
            tvTipo.setTextColor(android.graphics.Color.parseColor("#444444"))

            val tvCantidad = TextView(this)
            tvCantidad.text = "Cantidad total: ${mezcla["cantidadTotal"]} ${mezcla["unidad"]}"
            tvCantidad.textSize = 16f
            tvCantidad.setTextColor(android.graphics.Color.parseColor("#444444"))

            val tvEstado = TextView(this)
            tvEstado.text = "Estado: ${mezcla["estado"]}"
            tvEstado.textSize = 16f
            tvEstado.setTextColor(android.graphics.Color.parseColor("#444444"))

            tarjeta.addView(tvNombre)
            tarjeta.addView(tvTipo)
            tarjeta.addView(tvCantidad)
            tarjeta.addView(tvEstado)

            contenedorMezclas.addView(tarjeta)
        }

        btnVolverListaMezclas.setOnClickListener {
            finish()
        }
    }
}