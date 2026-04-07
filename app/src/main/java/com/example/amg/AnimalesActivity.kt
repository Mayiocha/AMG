package com.example.amg

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.amg.data.AdminSQLiteOpenHelper

class AnimalesActivity : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private lateinit var dbHelper: AdminSQLiteOpenHelper
    private lateinit var spinnerLote: Spinner

    // Almacena el lot_id seleccionado (null = todos)
    private var lotIdSeleccionado: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animales)

        dbHelper     = AdminSQLiteOpenHelper(this)
        tableLayout  = findViewById(R.id.tableAnimales)
        spinnerLote  = findViewById(R.id.spinnerLote)

        configurarSpinnerLote()

        findViewById<Button>(R.id.btnAgregarAnimal).setOnClickListener {
            startActivity(Intent(this, AgregarAnimalActivity::class.java))
        }

        findViewById<Button>(R.id.btnGestionLotes).setOnClickListener {
            startActivity(Intent(this, GestionLotesActivity::class.java))
        }

        findViewById<Button>(R.id.btnVolver).setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        // Reconstruir spinner por si se agregó un lote nuevo
        configurarSpinnerLote()
    }

    private fun configurarSpinnerLote() {
        val lotIds   = dbHelper.getAllLotIds()
        val opciones = mutableListOf("Todos los lotes") +
                lotIds.map { "Lote $it" }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)
        spinnerLote.adapter = adapter

        // Conservar selección previa si sigue existiendo
        val selAnterior = lotIdSeleccionado
        spinnerLote.setSelection(0)  // default: todos
        if (selAnterior != null) {
            val idx = lotIds.indexOf(selAnterior.toString())
            if (idx >= 0) spinnerLote.setSelection(idx + 1)
        }

        spinnerLote.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, pos: Int, id: Long) {
                lotIdSeleccionado = if (pos == 0) null else lotIds[pos - 1].toInt()
                mostrarAnimales()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun mostrarAnimales() {
        // Limpia filas excepto encabezado
        if (tableLayout.childCount > 1) {
            tableLayout.removeViews(1, tableLayout.childCount - 1)
        }

        val listaAnimales = dbHelper.getAnimalsByLot(lotIdSeleccionado)

        if (listaAnimales.isEmpty()) {
            val row = TableRow(this)
            val tv = TextView(this).apply {
                text = "Sin animales en este lote"
                setTextColor(ContextCompat.getColor(this@AnimalesActivity, R.color.black))
                setPadding(16, 28, 16, 28)
                gravity = Gravity.CENTER
            }
            val params = TableRow.LayoutParams().apply { span = 3 }
            row.addView(tv, params)
            tableLayout.addView(row)
            return
        }

        for (animal in listaAnimales) {
            val row = TableRow(this).apply {
                setBackgroundResource(R.drawable.borde_celda)
                val params = TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 4, 0, 4)
                layoutParams = params
            }

            val tvTag = TextView(this).apply {
                text = animal.tagId
                setTextColor(ContextCompat.getColor(this@AnimalesActivity, R.color.black))
                setTypeface(null, Typeface.BOLD)
                setPadding(16, 28, 16, 28)
                gravity = Gravity.CENTER
            }
            val tvTipo = TextView(this).apply {
                text = animal.category
                setTextColor(ContextCompat.getColor(this@AnimalesActivity, R.color.black))
                setPadding(16, 28, 16, 28)
                gravity = Gravity.CENTER
            }
            val tvPeso = TextView(this).apply {
                text = "${animal.weight} kg"
                setTextColor(ContextCompat.getColor(this@AnimalesActivity, R.color.green_primary))
                setTypeface(null, Typeface.BOLD)
                setPadding(16, 28, 16, 28)
                gravity = Gravity.CENTER
            }

            row.addView(tvTag)
            row.addView(tvTipo)
            row.addView(tvPeso)

            row.setOnClickListener {
                val intent = Intent(this, DetalleAnimalActivity::class.java).apply {
                    putExtra("ANIMAL_ID", animal.id ?: -1)
                    putExtra("ID",        animal.id.toString())
                    putExtra("TAG_ID",    animal.tagId)
                    putExtra("CATEGORY",  animal.category)
                    putExtra("RACE",      animal.race)
                    putExtra("WEIGHT",    "${animal.weight} kg")
                    putExtra("MONTHS",    "${animal.monthOld} meses")
                    putExtra("LOT",       animal.lotId.toString())
                    putExtra("HEALTHY",   if (animal.isHealthy) "Saludable" else "En tratamiento")
                }
                startActivity(intent)
            }

            tableLayout.addView(row)
        }
    }
}