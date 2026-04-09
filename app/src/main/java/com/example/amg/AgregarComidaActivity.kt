package com.example.amg

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amg.data.AdminSQLiteOpenHelper

class AgregarComidaActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_comida)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = AdminSQLiteOpenHelper(this)

        val etNombre   = findViewById<EditText>(R.id.etNombreComida)
        val etCantidad = findViewById<EditText>(R.id.etCantidadComida)
        val spUnidad   = findViewById<Spinner>(R.id.spinnerUnidad)

        // Configurar spinner de unidades
        val unidades = arrayOf("kg", "g", "ton", "lb", "L", "mL")
        spUnidad.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, unidades)

        findViewById<Button>(R.id.btnGuardarComida).setOnClickListener {
            val nombre   = etNombre.text.toString().trim()
            val cantStr  = etCantidad.text.toString().trim()

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingresa el nombre del alimento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (cantStr.isEmpty()) {
                Toast.makeText(this, "Ingresa la cantidad disponible", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cantidad = cantStr.toFloatOrNull()
            if (cantidad == null || cantidad < 0) {
                Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val unidad = spUnidad.selectedItem.toString()

            val id = dbHelper.insertInventoryItem(nombre, cantidad, unidad)
            if (id != -1L) {
                Toast.makeText(this, "\"$nombre\" agregado al inventario", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al guardar el alimento", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnVolverComida).setOnClickListener {
            finish()
        }
    }
}