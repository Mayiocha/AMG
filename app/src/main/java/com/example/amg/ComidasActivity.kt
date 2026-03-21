package com.example.amg

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ComidasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comidas)

        val btnInventario = findViewById<Button>(R.id.btnInventario)
        val btnNuevaCarga = findViewById<Button>(R.id.btnNuevaCarga)
        val btnHistorialTolva = findViewById<Button>(R.id.btnHistorialTolva)
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val btnListaMezclas = findViewById<Button>(R.id.btnListaMezclas)

        btnInventario.setOnClickListener {
            startActivity(Intent(this, InventarioComidasActivity::class.java))
        }

        btnNuevaCarga.setOnClickListener {
            startActivity(Intent(this, AgregarMezclaActivity::class.java))
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