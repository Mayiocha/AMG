package com.example.amg

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ConfiguracionTolvaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion_tolva)

        // Soporte para Notch y System Bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvMezclaSeleccionada = findViewById<TextView>(R.id.tvMezclaSeleccionada)
        val tvHora = findViewById<TextView>(R.id.tvHora)
        val tvCantidad = findViewById<TextView>(R.id.tvCantidad)
        val tvEstado = findViewById<TextView>(R.id.tvEstado)

        val btnSeleccionarMezcla = findViewById<Button>(R.id.btnSeleccionarMezcla)
        val btnSeleccionarHora = findViewById<Button>(R.id.btnSeleccionarHora)
        val btnCantidadTolva = findViewById<Button>(R.id.btnCantidadTolva)
        val swTolvaActiva = findViewById<SwitchCompat>(R.id.swTolvaActiva)
        val btnVolver = findViewById<Button>(R.id.btnVolverConfiguracion)
        val btnHistorialTolva = findViewById<Button>(R.id.btnHistorialTolva)

        // Estado inicial
        actualizarEstadoVisual(swTolvaActiva.isChecked, tvEstado)

        btnHistorialTolva.setOnClickListener {
            startActivity(android.content.Intent(this, HistorialTolvaActivity::class.java))
        }

        btnSeleccionarMezcla.setOnClickListener {
            Toast.makeText(this, "Módulo de mezclas próximamente", Toast.LENGTH_SHORT).show()
        }

        btnSeleccionarHora.setOnClickListener {
            // Aquí podrías implementar un TimePickerDialog
            Toast.makeText(this, "Selección de hora activa", Toast.LENGTH_SHORT).show()
        }

        btnCantidadTolva.setOnClickListener {
            Toast.makeText(this, "Ajuste de peso activo", Toast.LENGTH_SHORT).show()
        }

        swTolvaActiva.setOnCheckedChangeListener { _, isChecked ->
            actualizarEstadoVisual(isChecked, tvEstado)
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun actualizarEstadoVisual(estaActivo: Boolean, tv: TextView) {
        if (estaActivo) {
            tv.text = "Activo"
            tv.setTextColor(ContextCompat.getColor(this, R.color.green_primary))
        } else {
            tv.text = "Inactivo"
            tv.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        }
    }
}