package com.example.amg

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amg.data.AdminSQLiteOpenHelper

class GestionMezclasActivity : AppCompatActivity() {

    private lateinit var contenedorMezclas: LinearLayout
    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_mezclas)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = AdminSQLiteOpenHelper(this)
        contenedorMezclas = findViewById(R.id.contenedorMezclas)

        val btnAgregarNuevaMezcla = findViewById<Button>(R.id.btnAgregarNuevaMezcla)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        mostrarMezclas()

        btnAgregarNuevaMezcla.setOnClickListener {
            startActivity(Intent(this, AgregarNuevaMezclaActivity::class.java))
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        mostrarMezclas()
    }

    private fun mostrarMezclas() {
        contenedorMezclas.removeAllViews()

        val listaMezclas = dbHelper.getAllMixtures()

        if (listaMezclas.isEmpty()) {
            val tvEmpty = TextView(this).apply {
                text = "No hay mezclas registradas"
                textSize = 16f
                setTextColor(ContextCompat.getColor(context, R.color.black))
                gravity = android.view.Gravity.CENTER
                setPadding(0, 32, 0, 32)
            }
            contenedorMezclas.addView(tvEmpty)
            return
        }

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
            tarjeta.setBackgroundResource(R.drawable.borde_celda)
            
            // Make tarjeta clickable
            tarjeta.isClickable = true
            tarjeta.isFocusable = true
            tarjeta.setOnClickListener {
                val intent = Intent(this, DetalleMezclaActivity::class.java).apply {
                    putExtra("MEZCLA_ID", mezcla.id)
                }
                startActivity(intent)
            }

            val tvNombre = TextView(this)
            tvNombre.text = mezcla.name
            tvNombre.textSize = 18f
            tvNombre.setTextColor(ContextCompat.getColor(this, R.color.green_primary))
            tvNombre.setTypeface(null, Typeface.BOLD)

            val tvTipo = TextView(this)
            tvTipo.text = "Tipo: ${mezcla.type}"
            tvTipo.textSize = 16f
            tvTipo.setTextColor(ContextCompat.getColor(this, R.color.black))

            val tvCantidad = TextView(this)
            tvCantidad.text = "Cantidad total: ${mezcla.quantity} ${mezcla.unit}"
            tvCantidad.textSize = 16f
            tvCantidad.setTextColor(ContextCompat.getColor(this, R.color.black))

            val tvEstado = TextView(this)
            tvEstado.text = "Estado: ${mezcla.status}"
            tvEstado.textSize = 16f
            tvEstado.setTextColor(
                if (mezcla.status.equals("Activa", ignoreCase = true)) 
                    ContextCompat.getColor(this, R.color.green_primary)
                else 
                    ContextCompat.getColor(this, android.R.color.darker_gray)
            )
            tvEstado.setTypeface(null, Typeface.BOLD)

            tarjeta.addView(tvNombre)
            tarjeta.addView(tvTipo)
            tarjeta.addView(tvCantidad)
            tarjeta.addView(tvEstado)

            contenedorMezclas.addView(tarjeta)
        }
    }
}
