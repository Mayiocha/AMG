package com.example.amg

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amg.data.AdminSQLiteOpenHelper

class InventarioComidasActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper
    private lateinit var tableInventario: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventario_comidas)

        // Soporte para Notch y System Bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = AdminSQLiteOpenHelper(this)
        tableInventario = findViewById(R.id.tableInventario)
        val btnAgregar = findViewById<Button>(R.id.btnAgregarComida)
        val btnVolver  = findViewById<Button>(R.id.btnVolver)

        actualizarTabla()

        btnAgregar.setOnClickListener {
            startActivity(Intent(this, AgregarComidaActivity::class.java))
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarTabla()
    }

    private fun actualizarTabla() {
        // Limpiamos las filas previas excepto el encabezado (índice 0)
        if (tableInventario.childCount > 1) {
            tableInventario.removeViews(1, tableInventario.childCount - 1)
        }

        val inventoryItems = dbHelper.getInventoryWithIds()

        for (item in inventoryItems) {
            val row = TableRow(this).apply {
                setBackgroundResource(R.drawable.borde_celda)
                val params = TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 4, 0, 4)
                layoutParams = params
                isClickable = true
                isFocusable = true
            }

            val tvNombre = TextView(this).apply {
                text = item.name
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setTypeface(null, Typeface.BOLD)
                setPadding(16, 28, 16, 28)
                gravity = Gravity.CENTER
            }

            val tvUnidad = TextView(this).apply {
                text = item.unit
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setPadding(16, 28, 16, 28)
                gravity = Gravity.CENTER
            }

            val tvCantidad = TextView(this).apply {
                text = item.quantity.toString()
                setTextColor(ContextCompat.getColor(context, R.color.green_primary))
                setTypeface(null, Typeface.BOLD)
                setPadding(16, 28, 16, 28)
                gravity = Gravity.CENTER
            }

            row.addView(tvNombre)
            row.addView(tvUnidad)
            row.addView(tvCantidad)

            // Abrir detalle al presionar la fila
            row.setOnClickListener {
                val intent = Intent(this, DetalleComidaActivity::class.java).apply {
                    putExtra("ITEM_ID",       item.id)
                    putExtra("ITEM_NOMBRE",   item.name)
                    putExtra("ITEM_CANTIDAD", item.quantity)
                    putExtra("ITEM_UNIDAD",   item.unit)
                }
                startActivity(intent)
            }

            tableInventario.addView(row)
        }
    }
}