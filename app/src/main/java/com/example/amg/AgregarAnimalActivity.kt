package com.example.amg

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AgregarAnimalActivity : AppCompatActivity() {

    private lateinit var tvIdInterno: TextView
    private var contadorId = 4

    private lateinit var etArete: EditText
    private lateinit var spTipo: Spinner
    private lateinit var spSexo: Spinner
    private lateinit var etPeso: EditText
    private lateinit var etMeses: EditText
    private lateinit var etMesIngreso: EditText
    private lateinit var spLote: Spinner
    private lateinit var etFechaNacimiento: EditText
    private lateinit var spEstadoProductivo: Spinner
    private lateinit var etAlimentacion: EditText
    private lateinit var etObservaciones: EditText
    private lateinit var spEstadoSanitario: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_animal)

        tvIdInterno = findViewById(R.id.tvIdInterno)
        etArete = findViewById(R.id.etArete)
        spTipo = findViewById(R.id.spTipo)
        spSexo = findViewById(R.id.spSexo)
        etPeso = findViewById(R.id.etPeso)
        etMeses = findViewById(R.id.etMeses)
        etMesIngreso = findViewById(R.id.etMesIngreso)
        spLote = findViewById(R.id.spLote)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        spEstadoProductivo = findViewById(R.id.spEstadoProductivo)
        etAlimentacion = findViewById(R.id.etAlimentacion)
        etObservaciones = findViewById(R.id.etObservaciones)
        spEstadoSanitario = findViewById(R.id.spEstadoSanitario)

        generarIdAutomatico()
        configurarSpinners()

        etFechaNacimiento.setOnClickListener {
            mostrarDatePicker(etFechaNacimiento)
        }

        val btnGuardar = findViewById<Button>(R.id.btnGuardarAnimal)
        btnGuardar.setOnClickListener {
            guardarAnimal()
        }
    }

    private fun generarIdAutomatico() {
        val idGenerado = "AUTO-${String.format("%03d", contadorId)}"
        tvIdInterno.text = "ID: $idGenerado"
    }

    private fun configurarSpinners() {
        fun configurarSpinner(spinner: Spinner, opciones: Array<String>) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                opciones
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        configurarSpinner(spTipo, arrayOf("Vaca", "Toro", "Ternera"))
        configurarSpinner(spSexo, arrayOf("Hembra", "Macho"))
        configurarSpinner(spLote, arrayOf("Lote A", "Lote B", "Lote C"))
        configurarSpinner(spEstadoProductivo, arrayOf("Lactando", "Seco", "Engorde", "Crecimiento"))
        configurarSpinner(spEstadoSanitario, arrayOf("Sano", "En tratamiento", "Observación"))
    }

    private fun mostrarDatePicker(editText: EditText) {
        val calendario = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, day ->
                val fecha = Calendar.getInstance()
                fecha.set(year, month, day)
                val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                editText.setText(formato.format(fecha.time))
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun guardarAnimal() {

        val id = tvIdInterno.text.toString().replace("ID: ", "")
        val arete = etArete.text.toString()
        val tipo = spTipo.selectedItem.toString()
        val sexo = spSexo.selectedItem.toString()
        val peso = etPeso.text.toString()
        val meses = etMeses.text.toString()
        val mesIngreso = etMesIngreso.text.toString()
        val lote = spLote.selectedItem.toString()
        val fechaNacimiento = etFechaNacimiento.text.toString()
        val estadoProductivo = spEstadoProductivo.selectedItem.toString()
        val alimentacion = etAlimentacion.text.toString()
        val observaciones = etObservaciones.text.toString()
        val estadoSanitario = spEstadoSanitario.selectedItem.toString()

        Toast.makeText(this, "Animal guardado: $id - $tipo", Toast.LENGTH_LONG).show()
    }
}