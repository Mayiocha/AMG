package com.example.amg

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetalleAnimalActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_EDITAR = 1001
    }

    // Vistas
    private lateinit var tvId: TextView
    private lateinit var tvArete: TextView
    private lateinit var tvRaza: TextView
    private lateinit var tvCategoria: TextView
    private lateinit var tvPeso: TextView
    private lateinit var tvMeses: TextView
    private lateinit var tvLote: TextView
    private lateinit var tvEstadoSanitario: TextView

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
        tvId              = findViewById(R.id.tvId)
        tvArete           = findViewById(R.id.tvArete)
        tvRaza            = findViewById(R.id.tvRaza)
        tvCategoria       = findViewById(R.id.tvSexo)
        tvPeso            = findViewById(R.id.tvPeso)
        tvMeses           = findViewById(R.id.tvMeses)
        tvLote            = findViewById(R.id.tvLote)
        tvEstadoSanitario = findViewById(R.id.tvEstadoSanitario)

        // Cargar datos del Intent
        mostrarDatosDelIntent(intent)

        // Botón Editar → lanzar EditarAnimalActivity
        findViewById<Button>(R.id.btnEditarAnimal).setOnClickListener {
            val editIntent = Intent(this, EditarAnimalActivity::class.java).apply {
                putExtra("ANIMAL_ID", intent.getIntExtra("ANIMAL_ID", -1))
                putExtra("TAG_ID",    tvArete.text.toString())
                putExtra("RACE",      tvRaza.text.toString())
                putExtra("CATEGORY",  tvCategoria.text.toString())
                // Peso: quitamos " kg" para enviarlo limpio al editor
                putExtra("WEIGHT",    tvPeso.text.toString().replace(" kg", "").trim())
                // Meses: quitamos " meses" para enviarlo limpio al editor
                putExtra("MONTHS",    tvMeses.text.toString().replace(" meses", "").trim())
                // Lote: usamos el tag que guarda el número puro (ej. "1")
                putExtra("LOT",       (tvLote.tag as? String) ?: tvLote.text.toString().removePrefix("Lote ").trim())
                putExtra("HEALTHY",   tvEstadoSanitario.text.toString())
            }
            @Suppress("DEPRECATION")
            startActivityForResult(editIntent, REQUEST_EDITAR)
        }

        // Botón Volver
        findViewById<Button>(R.id.btnVolverDetalle).setOnClickListener { finish() }
    }

    private fun mostrarDatosDelIntent(data: Intent) {
        tvId.text              = data.getStringExtra("ID") ?: "---"
        tvArete.text           = data.getStringExtra("TAG_ID") ?: "---"
        tvRaza.text            = data.getStringExtra("RACE") ?: "---"
        tvCategoria.text       = data.getStringExtra("CATEGORY") ?: "---"
        tvPeso.text            = data.getStringExtra("WEIGHT") ?: "---"
        tvMeses.text           = data.getStringExtra("MONTHS") ?: "---"
        // Guardamos el número de lote en tvLote para pasarlo limpio al editor
        val lot = data.getStringExtra("LOT") ?: "---"
        tvLote.text            = "Lote $lot"  // Display: "Lote 1"
        // Guardamos el número puro en un tag para recuperarlo al editar
        tvLote.tag             = lot
        tvEstadoSanitario.text = data.getStringExtra("HEALTHY") ?: "---"
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDITAR && resultCode == Activity.RESULT_OK && data != null) {
            // Actualizar las vistas con los nuevos datos devueltos por EditarAnimalActivity
            tvArete.text           = data.getStringExtra("TAG_ID") ?: tvArete.text
            tvRaza.text            = data.getStringExtra("RACE") ?: tvRaza.text
            tvCategoria.text       = data.getStringExtra("CATEGORY") ?: tvCategoria.text
            tvPeso.text            = data.getStringExtra("WEIGHT") ?: tvPeso.text
            tvMeses.text           = data.getStringExtra("MONTHS") ?: tvMeses.text
            tvEstadoSanitario.text = data.getStringExtra("HEALTHY") ?: tvEstadoSanitario.text
            // Lote: actualizar display y conservar número puro en el tag
            data.getStringExtra("LOT")?.let { lotNum ->
                tvLote.text = "Lote $lotNum"
                tvLote.tag  = lotNum
            }
        }
    }
}