package com.example.amg

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetalleAnimalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_animal)

        // Consistencia con el Notch
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Vincular Vistas
        val tvId = findViewById<TextView>(R.id.tvId)
        val tvArete = findViewById<TextView>(R.id.tvArete)
        val tvRaza = findViewById<TextView>(R.id.tvRaza)
        val tvCategoria = findViewById<TextView>(R.id.tvSexo) // Usamos el campo Sexo para Categoría por ahora
        val tvPeso = findViewById<TextView>(R.id.tvPeso)
        val tvMeses = findViewById<TextView>(R.id.tvMeses)
        val tvLote = findViewById<TextView>(R.id.tvLote)
        val tvEstadoSanitario = findViewById<TextView>(R.id.tvEstadoSanitario)
        val btnVolver = findViewById<Button>(R.id.btnVolverDetalle) // Asegúrate de tener este ID

        // Recuperar Datos del Intent
        tvId.text = intent.getStringExtra("ID")
        tvArete.text = intent.getStringExtra("TAG_ID")
        tvRaza.text = intent.getStringExtra("RACE")
        tvCategoria.text = intent.getStringExtra("CATEGORY")
        tvPeso.text = intent.getStringExtra("WEIGHT")
        tvMeses.text = intent.getStringExtra("MONTHS")
        tvLote.text = intent.getStringExtra("LOT")
        tvEstadoSanitario.text = intent.getStringExtra("HEALTHY")

        btnVolver?.setOnClickListener { finish() }
    }
}