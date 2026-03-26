package com.example.amg

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amg.data.AdminSQLiteOpenHelper
import com.example.amg.model.Animal

class AgregarAnimalActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    // UI Elements
    private lateinit var etArete: EditText
    private lateinit var spTipo: Spinner
    private lateinit var etRaza: EditText
    private lateinit var etPeso: EditText
    private lateinit var etMeses: EditText
    private lateinit var spLote: Spinner
    private lateinit var cbSaludable: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_animal)

        // Configuración para respetar Notch y barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = AdminSQLiteOpenHelper(this)

        // Vincular vistas
        etArete = findViewById(R.id.etArete)
        spTipo = findViewById(R.id.spTipo)
        etRaza = findViewById(R.id.etRaza)
        etPeso = findViewById(R.id.etPeso)
        etMeses = findViewById(R.id.etMeses)
        spLote = findViewById(R.id.spLote)
        cbSaludable = findViewById(R.id.cbSaludable)

        configurarSpinners()

        findViewById<Button>(R.id.btnGuardarAnimal).setOnClickListener {
            guardarAnimalEnBD()
        }

        findViewById<Button>(R.id.btnCancelarAnimal).setOnClickListener {
            finish()
        }
    }

    private fun configurarSpinners() {
        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayOf("Vaca", "Toro", "Ternera"))
        spTipo.adapter = adapterTipo

        // IDs de lotes existentes (Semilla)
        val adapterLote = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayOf("1", "2", "3"))
        spLote.adapter = adapterLote
    }

    private fun guardarAnimalEnBD() {
        val tagId = etArete.text.toString()
        val weightStr = etPeso.text.toString()
        val monthsStr = etMeses.text.toString()
        val race = etRaza.text.toString()

        if (tagId.isEmpty() || weightStr.isEmpty() || monthsStr.isEmpty()) {
            Toast.makeText(this, "Por favor llena los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevoAnimal = Animal(
            tagId = tagId,
            lotId = spLote.selectedItem.toString().toInt(),
            category = spTipo.selectedItem.toString(),
            race = race,
            weight = weightStr.toFloatOrNull() ?: 0f,
            monthOld = monthsStr.toIntOrNull() ?: 0,
            isHealthy = cbSaludable.isChecked
        )

        val resultado = dbHelper.insertAnimal(nuevoAnimal)

        if (resultado != -1L) {
            Toast.makeText(this, "Animal guardado con éxito", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show()
        }
    }
}