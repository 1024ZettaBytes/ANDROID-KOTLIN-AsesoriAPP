package com.itson.asesoriapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_agregar_asesoria.*


class AgregarAsesoriaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar()?.hide(); // hide the title bar
        setContentView(R.layout.activity_agregar_asesoria)
        // Setea elementos de lista de materias
        val listaMaterias = arrayOf("Seleccione una materia...")
        val adaptadorMaterias = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaMaterias // Array
        )
        adaptadorMaterias.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstAgregar_materia.adapter = adaptadorMaterias

        // Setea elementos de lista de dias
        val listaDias= arrayOf("Seleccione un día...","LUNES","MARTES","MIÉRCOLES","JUEVES","VIERNES")
        val adaptadorDias = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaDias // Array
        )
        adaptadorMaterias.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstAgregar_dia.adapter = adaptadorDias
        // Setea elementos de lista de horas inicio
        var listaHoras= arrayOf("Hora inicio","07:00","08:00","09:00","10:00","11:00","12:00")
        var adaptadorHoras = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaHoras// Array
        )
        adaptadorHoras.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstAgregar_horaIni.adapter = adaptadorHoras


        btnAgregar_cancelar.setOnClickListener(){
            finish()
        }
    }
}
