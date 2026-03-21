package com.example.amg

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class InventarioComidasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventario_comidas)

        val listView = findViewById<ListView>(R.id.listViewComidas)
        val fabAgregar = findViewById<FloatingActionButton>(R.id.fabAgregar)

        val comidas = arrayOf(
            "Maíz - 500 kg",
            "Silo - 1200 kg",
            "Balanceado - 300 kg"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            comidas
        )

        listView.adapter = adapter

        fabAgregar.setOnClickListener {
            startActivity(Intent(this, AgregarComidaActivity::class.java))
        }
    }
}