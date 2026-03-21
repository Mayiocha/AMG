package com.example.amg

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnIrComidas = findViewById<Button>(R.id.btnIrComidas)
        val btnIrAnimales = findViewById<Button>(R.id.btnIrAnimales)
        val btnConfiguracion = findViewById<Button>(R.id.btnConfiguracion)

        btnIrComidas.setOnClickListener {
            startActivity(Intent(this, ComidasActivity::class.java))
        }

        btnIrAnimales.setOnClickListener {
            startActivity(Intent(this, AnimalesActivity::class.java))
        }

        btnConfiguracion.setOnClickListener {
            startActivity(Intent(this, ConfiguracionTolvaActivity::class.java))
        }

    }
}