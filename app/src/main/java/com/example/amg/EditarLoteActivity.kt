package com.example.amg

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amg.data.AdminSQLiteOpenHelper

class EditarLoteActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_lote)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = AdminSQLiteOpenHelper(this)

        // Datos recibidos del intent
        val loteId    = intent.getIntExtra("LOTE_ID", -1)
        val cantidad  = intent.getIntExtra("LOTE_CANTIDAD", 0)
        val etapaAct  = intent.getIntExtra("LOTE_ETAPA", 1)

        if (loteId == -1) {
            Toast.makeText(this, "Error: lote no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Actualizar subtítulo con el número de lote
        findViewById<TextView>(R.id.tvSubtituloEditarLote).text =
            "Modifica los datos del Lote #$loteId"

        val etCapacidad = findViewById<EditText>(R.id.etEditarCapacidad)
        val spEtapa     = findViewById<Spinner>(R.id.spEditarEtapa)

        // Rellenar campos con los valores actuales
        etCapacidad.setText(cantidad.toString())

        val etapas = arrayOf("Etapa 1 — Cría", "Etapa 2 — Desarrollo", "Etapa 3 — Engorda")
        spEtapa.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, etapas)
        spEtapa.setSelection((etapaAct - 1).coerceIn(0, 2))

        // Guardar cambios
        findViewById<Button>(R.id.btnGuardarLote).setOnClickListener {
            val capStr = etCapacidad.text.toString().trim()
            if (capStr.isEmpty()) {
                Toast.makeText(this, "Ingresa la capacidad del lote", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val nuevaCap   = capStr.toIntOrNull() ?: 0
            val nuevaEtapa = spEtapa.selectedItemPosition + 1

            val rows = dbHelper.updateLot(loteId, nuevaCap, nuevaEtapa)
            if (rows > 0) {
                Toast.makeText(this, "Lote #$loteId actualizado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al actualizar el lote", Toast.LENGTH_SHORT).show()
            }
        }

        // Cancelar
        findViewById<Button>(R.id.btnCancelarEditarLote).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnEliminarRegistroLote).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar Lote")
                .setMessage("¿Estás seguro de que deseas eliminar este lote? Si eliminas el lote, los registros asociados pueden verse afectados. Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar") { _, _ ->
                    val rows = dbHelper.deleteLot(loteId)
                    if (rows > 0) {
                        Toast.makeText(this, "Lote eliminado exitosamente", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Error al eliminar el lote", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}
