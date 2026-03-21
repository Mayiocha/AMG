package com.example.amg

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class AgregarComidaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_comida)

        val etNombre = findViewById<EditText>(R.id.etNombreComida)
        val etTipo = findViewById<EditText>(R.id.etTipoComida)
        val etCantidad = findViewById<EditText>(R.id.etCantidadComida)
        val spinnerUnidad = findViewById<Spinner>(R.id.spinnerUnidad)
        val etPrecio = findViewById<EditText>(R.id.etPrecioUnidad)
        val etStockMinimo = findViewById<EditText>(R.id.etStockMinimo)

        val btnGuardar = findViewById<Button>(R.id.btnGuardarComida)
        val btnVolver = findViewById<Button>(R.id.btnVolverComida)

        btnGuardar.setOnClickListener {
            finish()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}