package com.example.amg

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AnimalesActivity : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout

    private val listaAnimales = listOf(
        Animal(
            id = "001",
            arete = "A001",
            raza = "Vaca",
            sexo = "Hembra",
            peso = "650 kg",
            meses = "24",
            mesIngreso = "Enero",
            lote = "Lote 1",
            fechaNacimiento = "01/01/2022",
            estadoReproductivo = "Lactando",
            alimentacion = "Pastura",
            observaciones = "Sin observaciones",
            estadoSanitario = "Saludable"
        ),
        Animal(
            id = "002",
            arete = "A002",
            raza = "Toro",
            sexo = "Macho",
            peso = "900 kg",
            meses = "36",
            mesIngreso = "Febrero",
            lote = "Lote 2",
            fechaNacimiento = "01/01/2021",
            estadoReproductivo = "Engorde",
            alimentacion = "Grano",
            observaciones = "Sin observaciones",
            estadoSanitario = "Saludable"
        ),
        Animal(
            id = "003",
            arete = "A003",
            raza = "Ternera",
            sexo = "Hembra",
            peso = "300 kg",
            meses = "12",
            mesIngreso = "Marzo",
            lote = "Lote 1",
            fechaNacimiento = "01/01/2023",
            estadoReproductivo = "Crecimiento",
            alimentacion = "Pastura",
            observaciones = "Sin observaciones",
            estadoSanitario = "Saludable"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animales)

        tableLayout = findViewById(R.id.tableAnimales)
        val btnAgregar = findViewById<Button>(R.id.btnAgregarAnimal)

        mostrarAnimales()

        btnAgregar.setOnClickListener {
            val intent = Intent(this, AgregarAnimalActivity::class.java)
            startActivity(intent)
        }
    }

    private fun mostrarAnimales() {

        if (tableLayout.childCount > 1) {
            tableLayout.removeViews(1, tableLayout.childCount - 1)
        }

        for ((index, animal) in listaAnimales.withIndex()) {

            val row = TableRow(this)

            // Alternar color de filas
            if (index % 2 == 0) {
                row.setBackgroundColor(0xFFFFFFFF.toInt()) // blanco
            } else {
                row.setBackgroundColor(0xFFF2F2F2.toInt()) // gris claro
            }

            val tvId = TextView(this)
            tvId.text = animal.id
            tvId.setPadding(16, 16, 16, 16)

            val tvRaza = TextView(this)
            tvRaza.text = animal.raza
            tvRaza.setPadding(16, 16, 16, 16)

            val tvPeso = TextView(this)
            tvPeso.text = animal.peso
            tvPeso.setPadding(16, 16, 16, 16)

            row.addView(tvId)
            row.addView(tvRaza)
            row.addView(tvPeso)

            row.setOnClickListener {
                val intent = Intent(this, DetalleAnimalActivity::class.java)

                intent.putExtra("id", animal.id)
                intent.putExtra("arete", animal.arete)
                intent.putExtra("raza", animal.raza)
                intent.putExtra("sexo", animal.sexo)
                intent.putExtra("peso", animal.peso)
                intent.putExtra("meses", animal.meses)
                intent.putExtra("mesIngreso", animal.mesIngreso)
                intent.putExtra("lote", animal.lote)
                intent.putExtra("fechaNacimiento", animal.fechaNacimiento)
                intent.putExtra("estadoReproductivo", animal.estadoReproductivo)
                intent.putExtra("alimentacion", animal.alimentacion)
                intent.putExtra("observaciones", animal.observaciones)
                intent.putExtra("estadoSanitario", animal.estadoSanitario)

                startActivity(intent)
            }

            tableLayout.addView(row)
        }
    }
}