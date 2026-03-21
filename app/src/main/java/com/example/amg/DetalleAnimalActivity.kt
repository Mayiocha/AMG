package com.example.amg

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.amg.R

class DetalleAnimalActivity : AppCompatActivity() {

    private lateinit var tvId: TextView
    private lateinit var tvArete: TextView
    private lateinit var tvRaza: TextView
    private lateinit var tvSexo: TextView
    private lateinit var tvPeso: TextView
    private lateinit var tvMeses: TextView
    private lateinit var tvMesIngreso: TextView
    private lateinit var tvLote: TextView
    private lateinit var tvFechaNacimiento: TextView
    private lateinit var tvEstadoReproductivo: TextView
    private lateinit var tvAlimentacion: TextView
    private lateinit var tvObservaciones: TextView
    private lateinit var tvEstadoSanitario: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_animal)


        tvId = findViewById(R.id.tvId)
        tvArete = findViewById(R.id.tvArete)
        tvRaza = findViewById(R.id.tvRaza)
        tvSexo = findViewById(R.id.tvSexo)
        tvPeso = findViewById(R.id.tvPeso)
        tvMeses = findViewById(R.id.tvMeses)
        tvMesIngreso = findViewById(R.id.tvMesIngreso)
        tvLote = findViewById(R.id.tvLote)
        tvFechaNacimiento = findViewById(R.id.tvFechaNacimiento)
        tvEstadoReproductivo = findViewById(R.id.tvEstadoReproductivo)
        tvAlimentacion = findViewById(R.id.tvAlimentacion)
        tvObservaciones = findViewById(R.id.tvObservaciones)
        tvEstadoSanitario = findViewById(R.id.tvEstadoSanitario)


        tvId.text = "123"
        tvArete.text = "A-45"
        tvRaza.text = "Holstein"
        tvSexo.text = "Hembra"
        tvPeso.text = "450 kg"
        tvMeses.text = "12"
        tvMesIngreso.text = "Enero"
        tvLote.text = "Lote 5"
        tvFechaNacimiento.text = "01/02/2022"
        tvEstadoReproductivo.text = "Activo"
        tvAlimentacion.text = "Pastura + Concentrado"
        tvObservaciones.text = "Sin incidencias"
        tvEstadoSanitario.text = "Saludable"
    }
}