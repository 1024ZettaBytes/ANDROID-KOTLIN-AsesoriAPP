package com.itson.asesoriapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import kotlinx.android.synthetic.main.activity_horarios.*

class HorariosActivity : AppCompatActivity() {
var listaMaterias = ArrayList<String>()
    var asesoriasLunes = HashMap<String, String>()
    var asesoriasMartes = HashMap<String, String>()
    var asesoriasMiercoles = HashMap<String, String>()
    var asesoriasJueves = HashMap<String, String>()
    var asesoriasViernes = HashMap<String, String>()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar()?.hide(); // hide the title bar

        setContentView(R.layout.activity_horarios)
        // Setea elementos de lista de materias
        val listaMaterias = arrayOf("Seleccione una materia...")
        val adaptadorMaterias = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaMaterias // Array
        )
        adaptadorMaterias.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstHorarios_materia.adapter = adaptadorMaterias

        // Listas de días
// Setea elementos de lista de cada dia
        val listaLunes= arrayOf("LUNES")
        val adaptadorLunes = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaLunes // Array
        )
        adaptadorLunes.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstHorarios_lunes.adapter = adaptadorLunes

        // Setea elementos de lista de cada dia
        val listaMartes= arrayOf("MARTES")
        val adaptadorMartes = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaMartes // Array
        )
        adaptadorMartes.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstHorarios_martes.adapter = adaptadorMartes
        // Setea elementos de lista de cada dia
        val listaMiercoles= arrayOf("MIÉRCOLES")
        val adaptadorMiercoles = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaMiercoles // Array
        )
        adaptadorMiercoles.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstHorarios_miercoles.adapter = adaptadorMiercoles
        // Setea elementos de lista de cada dia
        val listaJueves= arrayOf("JUEVES")
        val adaptadorJueves = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaJueves // Array
        )
        adaptadorJueves.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstHorarios_jueves.adapter = adaptadorJueves
        // Setea elementos de lista de cada dia
        val listaViernes= arrayOf("VIERNES")
        val adaptadorViernes = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaViernes // Array
        )

        adaptadorViernes.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstHorarios_viernes.adapter = adaptadorViernes
        habilitarCampos(false)
        pgsBarCargaHorarios.visibility = View.VISIBLE
        cargarDatos()
        btnAlumno_cerrarSesion.setOnClickListener(){
            finish()
        }
    }

    private fun cargarDatos() {
        // cargar datos de materias

    }

    private fun habilitarCampos(valor: Boolean) {
           lstHorarios_materia.isEnabled = valor
           lstHorarios_lunes.isEnabled = valor
        lstHorarios_martes.isEnabled = valor
        lstHorarios_miercoles.isEnabled = valor
        lstHorarios_jueves.isEnabled = valor
        lstHorarios_viernes.isEnabled = valor
    }
}
