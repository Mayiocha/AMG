package com.example.amg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.amg.data.AdminSQLiteOpenHelper

class AgregarNuevaMezclaActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_nueva_mezcla)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = AdminSQLiteOpenHelper(this)

        val etNombreMezcla = findViewById<EditText>(R.id.etNombreMezcla)
        val spTipoMezcla = findViewById<Spinner>(R.id.spTipoMezcla)
        val cbEstado = findViewById<CheckBox>(R.id.cbEstado)
        val llContenedorIngredientes = findViewById<LinearLayout>(R.id.llContenedorIngredientes)
        val btnAgregarIngrediente = findViewById<Button>(R.id.btnAgregarIngrediente)

        // Spinners logic
        val tipos = arrayOf("Engorda", "Producción", "Desarrollo", "Mantenimiento")
        spTipoMezcla.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)

        // Obtener inventario
        val inventarioItems = dbHelper.getInventoryWithIds()
        val inventarioNombres = inventarioItems.map { "${it.name} (${it.quantity}${it.unit} disp.)" }.toTypedArray()

        fun agregarFilaIngrediente() {
            if (inventarioItems.isEmpty()) {
                Toast.makeText(this, "No hay alimentos en el inventario", Toast.LENGTH_SHORT).show()
                return
            }
            val vistaFila = LayoutInflater.from(this).inflate(R.layout.item_mezcla_ingrediente_input, llContenedorIngredientes, false)
            val spIngrediente = vistaFila.findViewById<Spinner>(R.id.spIngrediente)
            val btnEliminar = vistaFila.findViewById<Button>(R.id.btnEliminarIngrediente)

            spIngrediente.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, inventarioNombres)

            btnEliminar.setOnClickListener {
                llContenedorIngredientes.removeView(vistaFila)
            }
            llContenedorIngredientes.addView(vistaFila)
        }

        // Agregar una fila inicial por defecto
        agregarFilaIngrediente()

        btnAgregarIngrediente.setOnClickListener {
            agregarFilaIngrediente()
        }


        val btnGuardarMezcla = findViewById<Button>(R.id.btnGuardarMezcla)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        btnGuardarMezcla.setOnClickListener {
            val nombre = etNombreMezcla.text.toString().trim()
            val tipo = spTipoMezcla.selectedItem.toString()
            val estado = if (cbEstado.isChecked) "Activa" else "Inactiva"

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Completa el nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (llContenedorIngredientes.childCount == 0) {
                Toast.makeText(this, "Agrega al menos un ingrediente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val ingredientesAUsar = mutableListOf<Pair<Int, Float>>()
            
            for (i in 0 until llContenedorIngredientes.childCount) {
                val vista = llContenedorIngredientes.getChildAt(i)
                val spIngrediente = vista.findViewById<Spinner>(R.id.spIngrediente)
                val etCantidad = vista.findViewById<EditText>(R.id.etCantidadIngrediente)

                val cantStr = etCantidad.text.toString().trim()
                if (cantStr.isEmpty()) {
                    Toast.makeText(this, "Las cantidades no pueden estar vacías", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                
                val cantidadIngresada = cantStr.toFloatOrNull()
                if (cantidadIngresada == null || cantidadIngresada <= 0f) {
                    Toast.makeText(this, "Cantidades inválidas", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val itemSeleccionado = inventarioItems[spIngrediente.selectedItemPosition]
                
                // check stock (not counting identical duplicates correctly yet, but basic ok)
                if (cantidadIngresada > itemSeleccionado.quantity) {
                    Toast.makeText(this, "Stock insuficiente para ${itemSeleccionado.name}", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                ingredientesAUsar.add(Pair(itemSeleccionado.id, cantidadIngresada))
            }

            // check repeated and sum logic 
            val sumMap = mutableMapOf<Int, Float>()
            for (ing in ingredientesAUsar) {
                sumMap[ing.first] = sumMap.getOrDefault(ing.first, 0f) + ing.second
            }
            
            for ((idInv, qty) in sumMap) {
                val item = inventarioItems.find { it.id == idInv }
                if (item != null && qty > item.quantity) {
                    Toast.makeText(this, "Stock insuficiente en total para ${item.name}", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }

            // Realizar Insercion
            val finalId = dbHelper.insertMixtureWithIngredients(nombre, tipo, estado, ingredientesAUsar)
            if (finalId != -1L) {
                Toast.makeText(this, "Mezcla guardada y stock descontado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al guardar mezcla", Toast.LENGTH_SHORT).show()
            }
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}
