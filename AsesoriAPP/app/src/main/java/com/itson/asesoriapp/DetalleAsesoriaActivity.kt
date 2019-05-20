package com.itson.asesoriapp

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.activity_detalle_asesoria.*

class DetalleAsesoriaActivity : AppCompatActivity() {
var correoAsesor = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar()?.hide(); // hide the title bar
        setContentView(R.layout.activity_detalle_asesoria)
        detalleAsesoria_materia.text = intent.getStringExtra("materia")
        detalleAsesoria_horario.text = intent.getStringExtra("dia")+"\n"+intent.getStringExtra("hora")+" hrs."
        correoAsesor = intent.getStringExtra("correoAsesor")
        detalleAsesoria_correo.text = correoAsesor
        detalleAsesoria_asesor.text = intent.getStringExtra("nombreAsesor")

        btnDetalleAsesoria_contacto.setOnClickListener(){
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:") // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(correoAsesor))
            intent.putExtra(Intent.EXTRA_SUBJECT,"Info Asesoria")
            if (intent.resolveActivity(this.packageManager) != null) {
                startActivity(intent)
            }
        }
        btnDetalleAsesoria_cerrar.setOnClickListener(){
            finish()
        }
    }
}
