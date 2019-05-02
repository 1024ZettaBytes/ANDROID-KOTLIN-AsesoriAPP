package com.itson.asesoriapp

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_horarios.*
import kotlinx.android.synthetic.main.activity_registro.*


class RegistroActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar()?.hide(); // hide the title bar
        setContentView(R.layout.activity_registro)
        // Setea elementos de lista de campus
        val listaCampus = arrayOf("Seleccione campus...")
        val adaptadorCampus = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaCampus // Array
        )
        adaptadorCampus.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstRegistro_campus.adapter = adaptadorCampus
        // Setea elementos de lista de carreras
        val listaCarreras= arrayOf("Seleccione carrera...")
        val adaptadorCarreras = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaCarreras // Array
        )
        adaptadorCarreras.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstRegistro_carrera.adapter = adaptadorCarreras


        btn_alumno.setOnClickListener(){
            btn_alumno.setBackgroundColor(resources.getColor(R.color.colorTexto))
            btn_alumno.setTextColor(Color.WHITE)
            btn_asesor.setBackgroundColor(resources.getColor(R.color.colorGris))
            btn_asesor.setTextColor(resources.getColor(R.color.colorTexto))
        }
        btn_asesor.setOnClickListener(){
            btn_asesor.setBackgroundColor(resources.getColor(R.color.colorTexto))
            btn_asesor.setTextColor(Color.WHITE)
            btn_alumno.setBackgroundColor(resources.getColor(R.color.colorGris))
            btn_alumno.setTextColor(resources.getColor(R.color.colorTexto))
        }
        btnRegistro_cancelar.setOnClickListener(){
            finish()
        }
    }

}
