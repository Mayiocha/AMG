package com.example.amg

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amg.data.AdminSQLiteOpenHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class InventarioComidasActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper

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
        val listView = findViewById<ListView>(R.id.listViewComidas)
        val fabAgregar = findViewById<FloatingActionButton>(R.id.fabAgregar)

        actualizarLista(listView)

        fabAgregar.setOnClickListener {
            startActivity(Intent(this, AgregarComidaActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val listView = findViewById<ListView>(R.id.listViewComidas)
        actualizarLista(listView)
    }

    private fun actualizarLista(listView: ListView) {
        // Obtenemos los datos reales de la BD
        val datosBD = dbHelper.getAllInventory().map { it.second }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            datosBD
        )
        listView.adapter = adapter
    }
}