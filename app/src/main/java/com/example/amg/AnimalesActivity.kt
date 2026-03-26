package com.example.amg

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.amg.data.AdminSQLiteOpenHelper

class AnimalesActivity : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animales)

        // Inicializar persistencia
        dbHelper = AdminSQLiteOpenHelper(this)

        tableLayout = findViewById(R.id.tableAnimales)
        val btnAgregar = findViewById<Button>(R.id.btnAgregarAnimal)

        btnAgregar.setOnClickListener {
            // Navegación a la pantalla de registro
            startActivity(Intent(this, AgregarAnimalActivity::class.java))
        }
    }

    // El método onResume asegura que la lista se refresque al volver de agregar un animal
    override fun onResume() {
        super.onResume()
        mostrarAnimales()
    }

    private fun mostrarAnimales() {
        // Limpiamos las filas previas excepto el encabezado (índice 0)
        if (tableLayout.childCount > 1) {
            tableLayout.removeViews(1, tableLayout.childCount - 1)
        }

        // Recuperar lista desde la base de datos local
        val listaAnimales = dbHelper.getAllAnimals()

        for (animal in listaAnimales) {
            val row = TableRow(this).apply {
                // Usamos tu drawable de bordes para cada celda
                setBackgroundResource(R.drawable.borde_celda)
                val params = TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 4, 0, 4)
                layoutParams = params
            }

            // Columna: Tag/Arete (Negrita para identificación)
            val tvTag = TextView(this).apply {
                text = animal.tagId
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setTypeface(null, Typeface.BOLD)
                setPadding(16, 28, 16, 28)
                gravity = Gravity.CENTER
            }

            // Columna: Categoría (Vaca/Toro/Ternera)
            val tvTipo = TextView(this).apply {
                text = animal.category
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setPadding(16, 28, 16, 28)
                gravity = Gravity.CENTER
            }

            // Columna: Peso (En verde para resaltar dato productivo)
            val tvPeso = TextView(this).apply {
                text = "${animal.weight} kg"
                setTextColor(ContextCompat.getColor(context, R.color.green_primary))
                setTypeface(null, Typeface.BOLD)
                setPadding(16, 28, 16, 28)
                gravity = Gravity.CENTER
            }

            // Agregar celdas a la fila
            row.addView(tvTag)
            row.addView(tvTipo)
            row.addView(tvPeso)

            // Listener para interactuar con el registro (puedes añadir el Detalle aquí)
            row.setOnClickListener {
                val intent = Intent(this, DetalleAnimalActivity::class.java)

                // Pasamos los datos que existen en tu modelo Animal
                intent.putExtra("ID", animal.id.toString())
                intent.putExtra("TAG_ID", animal.tagId)
                intent.putExtra("CATEGORY", animal.category)
                intent.putExtra("RACE", animal.race)
                intent.putExtra("WEIGHT", "${animal.weight} kg")
                intent.putExtra("MONTHS", "${animal.monthOld} meses")
                intent.putExtra("LOT", "Lote ${animal.lotId}")
                intent.putExtra("HEALTHY", if (animal.isHealthy) "Saludable" else "En tratamiento")

                startActivity(intent)
            }

            // Agregar la fila completa a la tabla
            tableLayout.addView(row)
        }
    }
}