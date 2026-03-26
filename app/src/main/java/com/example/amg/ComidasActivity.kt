package com.example.amg

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ComidasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comidas)

        // Soporte dinámico para Notch y barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnInventario = findViewById<Button>(R.id.btnInventario)
        val btnNuevaCarga = findViewById<Button>(R.id.btnNuevaCarga)
        val btnHistorialTolva = findViewById<Button>(R.id.btnHistorialTolva)
        val btnListaMezclas = findViewById<Button>(R.id.btnListaMezclas)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        btnInventario.setOnClickListener {
            // Reemplazar con la Activity real cuando la tengas creada
             startActivity(Intent(this, InventarioComidasActivity::class.java))
        }

        btnNuevaCarga.setOnClickListener {
             //startActivity(Intent(this, AgregarMezclaActivity::class.java))
            Toast.makeText(this, "Funcion en desarrollo", Toast.LENGTH_SHORT).show()
        }

        btnHistorialTolva.setOnClickListener {
             startActivity(Intent(this, HistorialTolvaActivity::class.java))
        }

        btnListaMezclas.setOnClickListener {
             startActivity(Intent(this, ListaMezclasActivity::class.java))
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}