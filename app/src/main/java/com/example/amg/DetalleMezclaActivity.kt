package com.example.amg

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amg.data.AdminSQLiteOpenHelper

class DetalleMezclaActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_mezcla)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = AdminSQLiteOpenHelper(this)

        val mezclaId = intent.getIntExtra("MEZCLA_ID", -1)
        if (mezclaId == -1) {
            finish()
            return
        }

        findViewById<Button>(R.id.btnEditarMezcla).setOnClickListener {
            val intent = Intent(this, EditarMezclaActivity::class.java).apply {
                putExtra("MEZCLA_ID", mezclaId)
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnVolverListado).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val mezclaId = intent.getIntExtra("MEZCLA_ID", -1)
        if (mezclaId == -1) return

        val mezcla = dbHelper.getMixtureById(mezclaId) ?: return

        findViewById<TextView>(R.id.tvDetalleNombre).text = mezcla.name
        findViewById<TextView>(R.id.tvDetalleTipo).text = mezcla.type
        findViewById<TextView>(R.id.tvDetalleCantidadTOTAL).text = "${mezcla.quantity} ${mezcla.unit}"
        
        val tvEstado = findViewById<TextView>(R.id.tvDetalleEstado)
        tvEstado.text = mezcla.status
        tvEstado.setTextColor(
            if (mezcla.status.equals("Activa", ignoreCase = true)) 
                ContextCompat.getColor(this, R.color.green_primary)
            else 
                ContextCompat.getColor(this, android.R.color.darker_gray)
        )

        // Cargar Ingredientes
        val llIngredientes = findViewById<LinearLayout>(R.id.llDetalleIngredientes)
        llIngredientes.removeAllViews()

        val ingredientes = dbHelper.getIngredientsForMixture(mezclaId)
        if (ingredientes.isEmpty()) {
            val tvEmpty = TextView(this)
            tvEmpty.text = "No hay ingredientes registrados."
            tvEmpty.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
            llIngredientes.addView(tvEmpty)
        } else {
            for (ing in ingredientes) {
                val tvIng = TextView(this)
                tvIng.text = "• ${ing.third} kg de ${ing.second}"
                tvIng.textSize = 15f
                tvIng.setTextColor(ContextCompat.getColor(this, R.color.black))
                tvIng.setPadding(0, 4, 0, 4)
                llIngredientes.addView(tvIng)
            }
        }
    }
}
