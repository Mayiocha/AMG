package com.example.amg

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amg.data.AdminSQLiteOpenHelper

class EditarComidaActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_comida)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = AdminSQLiteOpenHelper(this)

        val itemId   = intent.getIntExtra("ITEM_ID",   -1)
        val nombre   = intent.getStringExtra("ITEM_NOMBRE")   ?: ""
        val cantidad = intent.getFloatExtra("ITEM_CANTIDAD", 0f)
        val unidad   = intent.getStringExtra("ITEM_UNIDAD")   ?: "kg"

        if (itemId == -1) {
            Toast.makeText(this, "Error: alimento no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Subtítulo con el nombre del item
        findViewById<TextView>(R.id.tvSubtituloEditarComida).text =
            "Editando: $nombre"

        val etNombre   = findViewById<EditText>(R.id.etEditarNombreComida)
        val etCantidad = findViewById<EditText>(R.id.etEditarCantidadComida)
        val spUnidad   = findViewById<Spinner>(R.id.spEditarUnidadComida)

        // Pre-rellenar campos
        etNombre.setText(nombre)
        etCantidad.setText(cantidad.toString())

        val unidades = arrayOf("kg", "g", "ton", "lb", "L", "mL")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, unidades)
        spUnidad.adapter = adapter
        val idx = unidades.indexOf(unidad).takeIf { it >= 0 } ?: 0
        spUnidad.setSelection(idx)

        // Guardar cambios
        findViewById<Button>(R.id.btnGuardarEditarComida).setOnClickListener {
            val nuevoNombre   = etNombre.text.toString().trim()
            val nuevaCantidad = etCantidad.text.toString().trim()

            if (nuevoNombre.isEmpty()) {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (nuevaCantidad.isEmpty()) {
                Toast.makeText(this, "Ingresa la cantidad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cantFloat  = nuevaCantidad.toFloatOrNull() ?: 0f
            val nuevaUnidad = spUnidad.selectedItem.toString()

            val rows = dbHelper.updateInventoryItem(itemId, nuevoNombre, cantFloat, nuevaUnidad)
            if (rows > 0) {
                Toast.makeText(this, "\"$nuevoNombre\" actualizado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        }

        // Cancelar
        findViewById<Button>(R.id.btnCancelarEdicionComida).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnEliminarRegistroComida).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar Alimento")
                .setMessage("¿Estás seguro de que deseas eliminar este alimento del inventario? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar") { _, _ ->
                    val rows = dbHelper.deleteInventoryItem(itemId)
                    if (rows > 0) {
                        Toast.makeText(this, "Alimento eliminado exitosamente", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Error al eliminar el alimento", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}
