package com.example.amg

import android.content.Intent
import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amg.data.AdminSQLiteOpenHelper
import com.example.amg.model.Animal

class EditarAnimalActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    private lateinit var etArete: EditText
    private lateinit var spTipo: Spinner
    private lateinit var etRaza: EditText
    private lateinit var etPeso: EditText
    private lateinit var etMeses: EditText
    private lateinit var spLote: Spinner
    private lateinit var cbSaludable: CheckBox

    private var animalId: Int = -1

    private val categorias = arrayOf("Vaca", "Toro", "Ternera")
    private var lotes: List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_animal)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = AdminSQLiteOpenHelper(this)

        // Vincular vistas
        etArete    = findViewById(R.id.etEditArete)
        spTipo     = findViewById(R.id.spEditTipo)
        etRaza     = findViewById(R.id.etEditRaza)
        etPeso     = findViewById(R.id.etEditPeso)
        etMeses    = findViewById(R.id.etEditMeses)
        spLote     = findViewById(R.id.spEditLote)
        cbSaludable = findViewById(R.id.cbEditSaludable)

        configurarSpinners()
        cargarDatosDelIntent()

        findViewById<Button>(R.id.btnGuardarEdicion).setOnClickListener {
            guardarCambios()
        }

        findViewById<Button>(R.id.btnCancelarEdicion).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnEliminarRegistro).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar Animal")
                .setMessage("¿Estás seguro de que deseas eliminar este animal? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar") { _, _ ->
                    val rows = dbHelper.deleteAnimal(animalId)
                    if (rows > 0) {
                        Toast.makeText(this, "Animal eliminado exitosamente", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Error al eliminar el animal", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun configurarSpinners() {
        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)
        spTipo.adapter = adapterTipo

        // Cargar lotes reales desde la base de datos
        lotes = dbHelper.getAllLotIds()
        val adapterLote = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, lotes)
        spLote.adapter = adapterLote
    }

    private fun cargarDatosDelIntent() {
        animalId = intent.getIntExtra("ANIMAL_ID", -1)

        etArete.setText(intent.getStringExtra("TAG_ID") ?: "")
        etRaza.setText(intent.getStringExtra("RACE") ?: "")
        etPeso.setText(intent.getStringExtra("WEIGHT") ?: "")
        etMeses.setText(intent.getStringExtra("MONTHS") ?: "")
        cbSaludable.isChecked = intent.getStringExtra("HEALTHY") == "Saludable"

        // Seleccionar categoría en el spinner
        val categoria = intent.getStringExtra("CATEGORY") ?: ""
        val catIndex = categorias.indexOf(categoria)
        if (catIndex >= 0) spTipo.setSelection(catIndex)

        // Seleccionar lote en el spinner
        val lote = intent.getStringExtra("LOT") ?: "1"
        val loteIndex = lotes.indexOf(lote)
        if (loteIndex >= 0) spLote.setSelection(loteIndex)
    }

    private fun guardarCambios() {
        val tagId      = etArete.text.toString().trim()
        val weightStr  = etPeso.text.toString().trim()
        val monthsStr  = etMeses.text.toString().trim()
        val race       = etRaza.text.toString().trim()

        if (tagId.isEmpty() || weightStr.isEmpty() || monthsStr.isEmpty()) {
            Toast.makeText(this, "Por favor llena los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (animalId == -1) {
            Toast.makeText(this, "Error: ID de animal no válido", Toast.LENGTH_SHORT).show()
            return
        }

        val animalActualizado = Animal(
            id       = animalId,
            tagId    = tagId,
            lotId    = spLote.selectedItem.toString().toInt(),
            category = spTipo.selectedItem.toString(),
            race     = race,
            weight   = weightStr.toFloatOrNull() ?: 0f,
            monthOld = monthsStr.toIntOrNull() ?: 0,
            isHealthy = cbSaludable.isChecked
        )

        val filas = dbHelper.updateAnimal(animalActualizado)

        if (filas > 0) {
            Toast.makeText(this, "Animal actualizado con éxito", Toast.LENGTH_SHORT).show()
            // Devolver resultado actualizado a DetalleAnimalActivity
            val resultIntent = Intent().apply {
                putExtra("ANIMAL_ID", animalId)
                putExtra("TAG_ID",    tagId)
                putExtra("RACE",      race)
                putExtra("CATEGORY",  spTipo.selectedItem.toString())
                putExtra("WEIGHT",    "${weightStr} kg")
                putExtra("MONTHS",    "${monthsStr} meses")
                putExtra("LOT",       spLote.selectedItem.toString())
                putExtra("HEALTHY",   if (cbSaludable.isChecked) "Saludable" else "No saludable")
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        } else {
            Toast.makeText(this, "Error al actualizar en la base de datos", Toast.LENGTH_SHORT).show()
        }
    }
}
