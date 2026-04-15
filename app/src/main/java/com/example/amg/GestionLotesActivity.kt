package com.example.amg

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amg.data.AdminSQLiteOpenHelper
import com.example.amg.model.Lot

class GestionLotesActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper
    private lateinit var container: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_lotes)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper  = AdminSQLiteOpenHelper(this)
        container = findViewById(R.id.containerLotes)

        findViewById<Button>(R.id.btnAgregarLote).setOnClickListener {
            mostrarDialogAgregarLote()
        }

        findViewById<Button>(R.id.btnVolverLotes).setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        cargarLotes()
    }

    private fun cargarLotes() {
        container.removeAllViews()
        val lotes = dbHelper.getAllLots()

        if (lotes.isEmpty()) {
            val tv = TextView(this).apply {
                text = "No hay lotes registrados"
                textSize = 16f
                setPadding(0, 16, 0, 16)
                gravity = android.view.Gravity.CENTER
            }
            container.addView(tv)
            return
        }

        val inflater = LayoutInflater.from(this)
        for (lote in lotes) {
            val card = inflater.inflate(R.layout.item_lote, container, false)
            bindLoteCard(card, lote)
            card.setOnClickListener {
                val intent = Intent(this, EditarLoteActivity::class.java).apply {
                    putExtra("LOTE_ID",       lote.id)
                    putExtra("LOTE_CANTIDAD", lote.quantity)
                    putExtra("LOTE_ETAPA",    lote.stage)
                }
                startActivity(intent)
            }
            container.addView(card)
        }
    }

    private fun bindLoteCard(view: android.view.View, lote: Lot) {
        view.findViewById<TextView>(R.id.tvLoteNumero).text = "Lote #${lote.id}"
        view.findViewById<TextView>(R.id.tvLoteTotal).text  = "Total de animales: ${lote.totalAnimals}"

        val etapaNombre = when (lote.stage) {
            1    -> "Etapa 1 — Cría"
            2    -> "Etapa 2 — Desarrollo"
            3    -> "Etapa 3 — Engorda"
            else -> "Etapa ${lote.stage}"
        }
        view.findViewById<TextView>(R.id.tvLoteEtapa).text = etapaNombre

        // Solo el número en cada contador (el label lo pone el XML)
        view.findViewById<TextView>(R.id.tvLoteVacas).text    = "${lote.vacas}"
        view.findViewById<TextView>(R.id.tvLoteToros).text    = "${lote.toros}"
        view.findViewById<TextView>(R.id.tvLoteTerneras).text = "${lote.terneras}"

        val fecha = lote.createdAt.take(10).ifEmpty { "Sin fecha" }
        view.findViewById<TextView>(R.id.tvLoteFecha).text = "Creado: $fecha"
    }

    private fun mostrarDialogAgregarLote() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_lote, null)

        val etCapacidad = dialogView.findViewById<EditText>(R.id.etLoteCapacidad)
        val spEtapa     = dialogView.findViewById<Spinner>(R.id.spEtapa)

        val etapas = arrayOf("Etapa 1 — Cría", "Etapa 2 — Desarrollo", "Etapa 3 — Engorda")
        spEtapa.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, etapas)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.btnConfirmarLote).setOnClickListener {
            val capStr = etCapacidad.text.toString().trim()
            if (capStr.isEmpty()) {
                Toast.makeText(this, "Ingresa la capacidad del lote", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val capacidad = capStr.toIntOrNull() ?: 0
            val etapa     = spEtapa.selectedItemPosition + 1

            val id = dbHelper.insertLot(capacidad, etapa)
            if (id != -1L) {
                Toast.makeText(this, "Lote #$id creado exitosamente", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                cargarLotes()
            } else {
                Toast.makeText(this, "Error al crear el lote", Toast.LENGTH_SHORT).show()
            }
        }

        dialogView.findViewById<Button>(R.id.btnCancelarLote).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
