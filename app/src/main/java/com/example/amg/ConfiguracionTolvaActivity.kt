package com.example.amg

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class ConfiguracionTolvaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion_tolva)

        val tvMezclaSeleccionada = findViewById<TextView>(R.id.tvMezclaSeleccionada)
        val tvHora = findViewById<TextView>(R.id.tvHora)
        val tvCantidad = findViewById<TextView>(R.id.tvCantidad)
        val tvEstado = findViewById<TextView>(R.id.tvEstado)

        val btnSeleccionarMezcla = findViewById<Button>(R.id.btnSeleccionarMezcla)
        val btnSeleccionarHora = findViewById<Button>(R.id.btnSeleccionarHora)
        val btnCantidadTolva = findViewById<Button>(R.id.btnCantidadTolva)
        val swTolvaActiva = findViewById<SwitchCompat>(R.id.swTolvaActiva)
        val btnVolver = findViewById<Button>(R.id.btnVolverConfiguracion)

        tvMezclaSeleccionada.text = "Mezcla Engorda 1"
        tvHora.text = "08:00 AM"
        tvCantidad.text = "25 kg"
        tvEstado.text = "Activo"
        swTolvaActiva.isChecked = true

        btnSeleccionarMezcla.setOnClickListener {
            tvMezclaSeleccionada.text = "Mezcla Engorda 1"
        }

        btnSeleccionarHora.setOnClickListener {
            tvHora.text = "08:00 AM"
        }

        btnCantidadTolva.setOnClickListener {
            tvCantidad.text = "25 kg"
        }

        swTolvaActiva.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvEstado.text = "Activo"
            } else {
                tvEstado.text = "Inactivo"
            }
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}