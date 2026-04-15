package com.example.amg

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amg.data.AdminSQLiteOpenHelper

class DetalleComidaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_comida)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val itemId   = intent.getIntExtra("ITEM_ID",   -1)
        val nombre   = intent.getStringExtra("ITEM_NOMBRE")   ?: "—"
        val cantidad = intent.getFloatExtra("ITEM_CANTIDAD", 0f)
        val unidad   = intent.getStringExtra("ITEM_UNIDAD")   ?: "—"

        findViewById<TextView>(R.id.tvDetalleNombre).text   = nombre
        findViewById<TextView>(R.id.tvDetalleCantidad).text = "$cantidad $unidad"
        findViewById<TextView>(R.id.tvDetalleUnidad).text   = unidad

        findViewById<Button>(R.id.btnEditarComida).setOnClickListener {
            val intent = Intent(this, EditarComidaActivity::class.java).apply {
                putExtra("ITEM_ID",       itemId)
                putExtra("ITEM_NOMBRE",   nombre)
                putExtra("ITEM_CANTIDAD", cantidad)
                putExtra("ITEM_UNIDAD",   unidad)
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnVolverDetalleComida).setOnClickListener {
            finish()
        }
    }

    // Al volver de EditarComidaActivity, actualizamos los datos mostrados
    override fun onResume() {
        super.onResume()
        val itemId = intent.getIntExtra("ITEM_ID", -1)
        if (itemId == -1) return

        val dbHelper = AdminSQLiteOpenHelper(this)
        val item = dbHelper.getInventoryById(itemId) ?: return

        // Actualizar intent para mantener datos frescos si se vuelve a editar
        intent.putExtra("ITEM_NOMBRE",   item.name)
        intent.putExtra("ITEM_CANTIDAD", item.quantity)
        intent.putExtra("ITEM_UNIDAD",   item.unit)

        findViewById<TextView>(R.id.tvDetalleNombre).text   = item.name
        findViewById<TextView>(R.id.tvDetalleCantidad).text = "${item.quantity} ${item.unit}"
        findViewById<TextView>(R.id.tvDetalleUnidad).text   = item.unit
    }
}
