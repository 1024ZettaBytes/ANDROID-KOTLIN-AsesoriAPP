package com.itson.asesoriapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.activity_detalle_asesoria.*

class DetalleAsesoriaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar()?.hide(); // hide the title bar
        setContentView(R.layout.activity_detalle_asesoria)
        btnDetalleAsesoria_cerrar.setOnClickListener(){
            finish()
        }
    }
}
