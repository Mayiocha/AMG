package com.example.amg

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amg.data.AdminSQLiteOpenHelper
import com.example.amg.model.Rutina
import java.util.Calendar

class ConfiguracionTolvaActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper
    private lateinit var containerRutinas: LinearLayout
    private lateinit var tvConteo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion_tolva)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper         = AdminSQLiteOpenHelper(this)
        containerRutinas = findViewById(R.id.containerRutinas)
        tvConteo         = findViewById(R.id.tvConteoRutinas)

        findViewById<Button>(R.id.btnAgregarRutina).setOnClickListener {
            mostrarDialogRutina(null)
        }

        findViewById<Button>(R.id.btnHistorialTolva).setOnClickListener {
            startActivity(Intent(this, HistorialTolvaActivity::class.java))
        }

        findViewById<Button>(R.id.btnVolverConfiguracion).setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        cargarRutinas()
    }

    // ──────────────────────────────────────────────
    // Cargar lista de rutinas
    // ──────────────────────────────────────────────

    private fun cargarRutinas() {
        containerRutinas.removeAllViews()
        val rutinas = dbHelper.getAllRoutines()

        tvConteo.text = "${rutinas.size} ${if (rutinas.size == 1) "rutina" else "rutinas"}"

        if (rutinas.isEmpty()) {
            val tv = TextView(this).apply {
                text = "No hay rutinas configuradas.\nPresiona + AGREGAR RUTINA para comenzar."
                textSize = 14f
                setTextColor(ContextCompat.getColor(this@ConfiguracionTolvaActivity, android.R.color.darker_gray))
                gravity = android.view.Gravity.CENTER
                setPadding(0, 32, 0, 32)
            }
            containerRutinas.addView(tv)
            return
        }

        val inflater = LayoutInflater.from(this)
        for (rutina in rutinas) {
            val card = inflater.inflate(R.layout.item_rutina, containerRutinas, false)
            bindRutinaCard(card, rutina)
            containerRutinas.addView(card)
        }
    }

    // ──────────────────────────────────────────────
    // Bind de una tarjeta de rutina
    // ──────────────────────────────────────────────

    private fun bindRutinaCard(view: android.view.View, rutina: Rutina) {
        val bar       = view.findViewById<android.view.View>(R.id.viewRutinaBar)
        val tvNombre  = view.findViewById<TextView>(R.id.tvRutinaNombre)
        val cbActiva  = view.findViewById<CheckBox>(R.id.cbRutinaActiva)
        val tvHora    = view.findViewById<TextView>(R.id.tvRutinaHora)
        val tvMezcla  = view.findViewById<TextView>(R.id.tvRutinaMezcla)
        val tvCant    = view.findViewById<TextView>(R.id.tvRutinaCantidad)
        val btnEditar = view.findViewById<Button>(R.id.btnEditarRutina)
        val btnElim   = view.findViewById<Button>(R.id.btnEliminarRutina)

        tvNombre.text = rutina.nombre
        tvHora.text   = rutina.hora
        tvMezcla.text = rutina.mixtureName
        tvCant.text   = "${rutina.cantidadKg} kg"

        // Estado visual
        val colorActivo = ContextCompat.getColor(this, R.color.green_primary)
        val colorInactivo = ContextCompat.getColor(this, android.R.color.darker_gray)
        bar.setBackgroundColor(if (rutina.activa) colorActivo else colorInactivo)

        // Checkbox — evitar disparar el listener al setear el estado inicial
        cbActiva.setOnCheckedChangeListener(null)
        cbActiva.isChecked = rutina.activa
        cbActiva.setOnCheckedChangeListener { _, isChecked ->
            dbHelper.updateRutinaActiva(rutina.id, isChecked)
            bar.setBackgroundColor(if (isChecked) colorActivo else colorInactivo)
        }

        btnEditar.setOnClickListener { mostrarDialogRutina(rutina) }

        btnElim.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar rutina")
                .setMessage("¿Deseas eliminar la rutina \"${rutina.nombre}\"?")
                .setPositiveButton("Eliminar") { _, _ ->
                    dbHelper.deleteRoutine(rutina.id)
                    cargarRutinas()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    // ──────────────────────────────────────────────
    // Diálogo crear / editar rutina
    // ──────────────────────────────────────────────

    private fun mostrarDialogRutina(rutinaExistente: Rutina?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_rutina, null)

        val tvTitulo    = dialogView.findViewById<TextView>(R.id.tvDialogTitulo)
        val etNombre    = dialogView.findViewById<EditText>(R.id.etRutinaNombre)
        val tvHoraSel   = dialogView.findViewById<TextView>(R.id.tvHoraSeleccionada)
        val btnElegirH  = dialogView.findViewById<Button>(R.id.btnElegirHora)
        val spMezcla    = dialogView.findViewById<Spinner>(R.id.spRutinaMezcla)
        val etCantidad  = dialogView.findViewById<EditText>(R.id.etRutinaCantidad)
        val cbActiva    = dialogView.findViewById<CheckBox>(R.id.cbRutinaActivaDialog)

        // Modo edición o creación
        val esEdicion = rutinaExistente != null
        tvTitulo.text = if (esEdicion) "Editar Rutina" else "Nueva Rutina"

        // Cargar mezclas en el spinner
        val mezclas = dbHelper.getAllMixtures()
        if (mezclas.isEmpty()) {
            Toast.makeText(this, "No hay mezclas disponibles. Crea una primero.", Toast.LENGTH_LONG).show()
            return
        }
        val nombresMezclas = mezclas.map { it.name }
        spMezcla.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nombresMezclas)

        // Hora seleccionada (estado mutable)
        var horaSeleccionada = rutinaExistente?.hora ?: ""
        if (horaSeleccionada.isNotEmpty()) {
            tvHoraSel.text = horaSeleccionada
            tvHoraSel.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        // Pre-llenar si es edición
        if (esEdicion) {
            etNombre.setText(rutinaExistente!!.nombre)
            etCantidad.setText(rutinaExistente.cantidadKg.toString())
            cbActiva.isChecked = rutinaExistente.activa
            val idx = mezclas.indexOfFirst { it.id == rutinaExistente.mixtureId }
            if (idx >= 0) spMezcla.setSelection(idx)
        }

        // TimePicker
        btnElegirH.setOnClickListener {
            val cal = Calendar.getInstance()
            val hora = if (horaSeleccionada.contains(":")) horaSeleccionada.split(":")[0].toIntOrNull() ?: cal.get(Calendar.HOUR_OF_DAY) else cal.get(Calendar.HOUR_OF_DAY)
            val min  = if (horaSeleccionada.contains(":")) horaSeleccionada.split(":")[1].toIntOrNull() ?: 0 else 0

            TimePickerDialog(this, { _, h, m ->
                horaSeleccionada = String.format("%02d:%02d", h, m)
                tvHoraSel.text = horaSeleccionada
                tvHoraSel.setTextColor(ContextCompat.getColor(this, R.color.black))
            }, hora, min, true).show()
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.btnGuardarRutina).setOnClickListener {
            val nombre   = etNombre.text.toString().trim()
            val cantStr  = etCantidad.text.toString().trim()
            val mixIdx   = spMezcla.selectedItemPosition

            if (nombre.isEmpty()) {
                etNombre.error = "Ingresa un nombre para la rutina"
                return@setOnClickListener
            }
            if (horaSeleccionada.isEmpty()) {
                Toast.makeText(this, "Selecciona una hora", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (cantStr.isEmpty()) {
                etCantidad.error = "Ingresa la cantidad en kg"
                return@setOnClickListener
            }

            val nuevaRutina = Rutina(
                id          = rutinaExistente?.id ?: 0,
                nombre      = nombre,
                hora        = horaSeleccionada,
                mixtureId   = mezclas[mixIdx].id,
                mixtureName = mezclas[mixIdx].name,
                cantidadKg  = cantStr.toFloatOrNull() ?: 0f,
                activa      = cbActiva.isChecked
            )

            val ok = if (esEdicion) dbHelper.updateRoutine(nuevaRutina) > 0
                     else           dbHelper.insertRoutine(nuevaRutina) != -1L

            if (ok) {
                Toast.makeText(this, if (esEdicion) "Rutina actualizada" else "Rutina creada", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                cargarRutinas()
            } else {
                Toast.makeText(this, "Error al guardar la rutina", Toast.LENGTH_SHORT).show()
            }
        }

        dialogView.findViewById<Button>(R.id.btnCancelarRutina).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}