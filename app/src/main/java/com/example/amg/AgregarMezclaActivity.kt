package com.example.amg

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AgregarMezclaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_mezcla)

        // Campos principales
        val etNombreMezcla = findViewById<EditText>(R.id.etNombreMezcla)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val etCantidadTotal = findViewById<EditText>(R.id.etCantidadTotal)

        // Nuevos campos para la estructura de mezcla
        val etTipo = findViewById<EditText>(R.id.etTipo)
        val etUnidad = findViewById<EditText>(R.id.etUnidad)
        val etFechaCreacion = findViewById<EditText>(R.id.etFechaCreacion)
        val etEstado = findViewById<EditText>(R.id.etEstado)
        val etObservaciones = findViewById<EditText>(R.id.etObservaciones)

        // Botones
        val btnGuardarMezcla = findViewById<Button>(R.id.btnGuardarMezcla)
        val btnVolver = findViewById<Button>(R.id.btnVolverMezcla)

        btnGuardarMezcla.setOnClickListener {
            // Aquí después se agregará la lógica para guardar la mezcla
            finish()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}