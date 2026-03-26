package com.example.amg

import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencias
        val panelCentral = findViewById<LinearLayout>(R.id.panelCentral) // Asegúrate de poner este ID al LinearLayout blanco en el XML
        val btnIrComidas = findViewById<Button>(R.id.btnIrComidas)
        val btnIrAnimales = findViewById<Button>(R.id.btnIrAnimales)
        val btnConfiguracion = findViewById<Button>(R.id.btnConfiguracion)

        // Animación de entrada (Opcional pero recomendado para pulir)
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 800
        // panelCentral?.startAnimation(fadeIn)

        // Navegación
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