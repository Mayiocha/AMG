package com.example.amg

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amg.data.AdminSQLiteOpenHelper

class EditarMezclaActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_mezcla)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = AdminSQLiteOpenHelper(this)

        val mezclaId = intent.getIntExtra("MEZCLA_ID", -1)
        if (mezclaId == -1) {
            Toast.makeText(this, "Error: mezcla no válida", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val mezcla = dbHelper.getMixtureById(mezclaId)
        if (mezcla == null) {
            Toast.makeText(this, "Error: mezcla no encontrada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val etNombreMezcla = findViewById<EditText>(R.id.etEditarNombreMezcla)
        val spTipoMezcla = findViewById<Spinner>(R.id.spEditarTipoMezcla)
        val tvCantidadTotal = findViewById<TextView>(R.id.tvEditarCantidadTotal)
        val cbEstado = findViewById<CheckBox>(R.id.cbEditarEstado)
        val llIngredientes = findViewById<LinearLayout>(R.id.llEditarIngredientes)

        // Spinners setup
        val tipos = arrayOf("Engorda", "Producción", "Desarrollo", "Mantenimiento")
        spTipoMezcla.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)

        // Pre-fill data
        etNombreMezcla.setText(mezcla.name)
        tvCantidadTotal.text = "${mezcla.quantity} ${mezcla.unit}"

        val tipoIdx = tipos.indexOf(mezcla.type).takeIf { it >= 0 } ?: 0
        spTipoMezcla.setSelection(tipoIdx)

        cbEstado.isChecked = mezcla.status.equals("Activa", ignoreCase = true)

        val ingredientes = dbHelper.getIngredientsForMixture(mezclaId)
        llIngredientes.removeAllViews()
        if (ingredientes.isEmpty()) {
            val tvEmpty = TextView(this)
            tvEmpty.text = "Sin ingredientes registrados."
            tvEmpty.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
            llIngredientes.addView(tvEmpty)
        } else {
            for (ing in ingredientes) {
                val tvIng = TextView(this)
                tvIng.text = "• ${ing.third} kg de ${ing.second}"
                tvIng.textSize = 14f
                tvIng.setTextColor(ContextCompat.getColor(this, R.color.black))
                llIngredientes.addView(tvIng)
            }
        }


        findViewById<Button>(R.id.btnGuardarEdicionMezcla).setOnClickListener {
            val nuevoNombre = etNombreMezcla.text.toString().trim()

            if (nuevoNombre.isEmpty()) {
                Toast.makeText(this, "Completa el nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevoTipo = spTipoMezcla.selectedItem.toString()
            val nuevoEstado = if (cbEstado.isChecked) "Activa" else "Inactiva"

            val rows = dbHelper.updateMixture(mezclaId, nuevoNombre, nuevoTipo, nuevoEstado)
            if (rows > 0) {
                Toast.makeText(this, "Mezcla actualizada exitosamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnCancelarEdicionMezcla).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnEliminarRegistroMezcla).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar Mezcla")
                .setMessage("¿Estás seguro de que deseas eliminar esta mezcla? Al eliminarla no se devolverá el inventario al estado original. Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar") { _, _ ->
                    val rows = dbHelper.deleteMixture(mezclaId)
                    if (rows > 0) {
                        Toast.makeText(this, "Mezcla eliminada exitosamente", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Error al eliminar la mezcla", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}
