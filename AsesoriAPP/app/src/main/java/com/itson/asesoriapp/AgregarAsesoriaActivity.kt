package com.itson.asesoriapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_agregar_asesoria.*
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.DocumentReference
import com.google.android.gms.tasks.OnSuccessListener
import android.R.attr.data
import android.app.Activity
import android.content.Intent


class AgregarAsesoriaActivity : AppCompatActivity() {
     var listaMaterias= ArrayList<String>()
    var campus = 0
    lateinit var carrera: String
    lateinit var correoAsesor: String
    lateinit var nombreAsesor: String
    var materiaSeleccionada = 0
    var diaSeleccionado = 0
    var horaSeleccionada = 0
    lateinit var listaDias:Array<String>
    lateinit var listaHoras: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar()?.hide(); // hide the title bar
        setContentView(R.layout.activity_agregar_asesoria)
       campus = intent.getIntExtra("campus", 0)
        carrera = intent.getStringExtra("carrera")
        nombreAsesor = intent.getStringExtra("nombreAsesor")
        correoAsesor = intent.getStringExtra("correoAsesor")
        // Setea elementos de lista de materias

        listaMaterias.add("Seleccione una materia...")
        val adaptadorMaterias = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaMaterias // Array
        )
        adaptadorMaterias.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstAgregar_materia.adapter = adaptadorMaterias

        // Setea elementos de lista de dias
        listaDias = arrayOf("Seleccione un día...", "LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES")
        val adaptadorDias = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaDias // Array
        )
        adaptadorMaterias.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstAgregar_dia.adapter = adaptadorDias
        // Setea elementos de lista de horas inicio
        listaHoras = arrayOf("Hora inicio", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00")
        var adaptadorHoras = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaHoras// Array
        )
        adaptadorHoras.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstAgregar_horaIni.adapter = adaptadorHoras
       habilitarCampos(false)
        llenarListaMaterias()
        btnAgregar_cancelar.setOnClickListener() {
            var intetnAsesor: Intent = Intent(this, AsesorActivity::class.java)
            intetnAsesor.putExtra("campus", campus)
            intetnAsesor.putExtra("carrera", carrera)
            intetnAsesor.putExtra("nombreAsesor", nombreAsesor)
            intetnAsesor.putExtra("correoAsesor", correoAsesor)
            intetnAsesor.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intetnAsesor)
        }
        btnAgregar_agregar.setOnClickListener() {
            if (verificarCampos()) {
                pgsBarAgregaAsesoria.visibility = View.VISIBLE
                var asesoria = HashMap<String, Any?>()
                asesoria.put("asesor", correoAsesor)
                asesoria.put("campus", campus)
                asesoria.put("carrera", carrera)
                asesoria.put("materia", listaMaterias[materiaSeleccionada])
                asesoria.put("dia", listaDias[diaSeleccionado])
                asesoria.put("hora", listaHoras[horaSeleccionada])

                var db = FirebaseFirestore.getInstance()
                db.collection("asesorias")
                    .add(asesoria)
                    .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                        pgsBarAgregaAsesoria.visibility = View.GONE
                        Toast.makeText(this, "Asesoría agregada exitosamente.", Toast.LENGTH_SHORT).show()
                        var intetnAsesor: Intent = Intent(this, AsesorActivity::class.java)
                        intetnAsesor.putExtra("campus", campus)
                        intetnAsesor.putExtra("carrera", carrera)
                        intetnAsesor.putExtra("nombreAsesor", nombreAsesor)
                        intetnAsesor.putExtra("correoAsesor", correoAsesor)
                        intetnAsesor.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intetnAsesor)
                    })
                    .addOnFailureListener(OnFailureListener { e ->
                        pgsBarAgregaAsesoria.visibility = View.GONE
                        Toast.makeText(this, "Ocurrió un error. Compruebe su conexión e intente nuevamente.", Toast.LENGTH_SHORT)
                            .show()
                    })
            }
        }
    }

    private fun llenarListaMaterias() {
        pgsBarCargaMaterias.visibility = View.VISIBLE
        var materias = FirebaseFirestore.getInstance().collection("materias").whereEqualTo("carrera", carrera)
        materias.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    listaMaterias.add(document.id)
                }
                pgsBarCargaMaterias.visibility = View.GONE
                if (listaMaterias.size > 1) {
                    val adaptadorMaterias = ArrayAdapter(
                        this, // Context
                        android.R.layout.simple_spinner_item, // Layout
                        listaMaterias // Array
                    )
                    adaptadorMaterias.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                    lstAgregar_materia.adapter = adaptadorMaterias
                    habilitarCampos(true)
                } else {
                    Toast.makeText(this, "No se encontraron materias registradas para su carrera.", Toast.LENGTH_SHORT)
                        .show()
                }

            } else {
                pgsBarCargaMaterias.visibility = View.GONE
                Toast.makeText(this, "ERROR: Verifique su conexión de red.", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun verificarCampos(): Boolean {
        if (materiaSeleccionada > 0) {
            if (diaSeleccionado > 0) {
                if (horaSeleccionada > 0) {
                    return true
                } else {
                    Toast.makeText(this, "Seleccione una hora.", Toast.LENGTH_SHORT).show()
                    return false
                }
            } else {
                Toast.makeText(this, "Seleccione un día.", Toast.LENGTH_SHORT).show()
                return false
            }
        } else {
            Toast.makeText(this, "Seleccione una materia.", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun habilitarCampos(valor: Boolean) {
        lstAgregar_materia.isEnabled = valor
        lstAgregar_dia.isEnabled = valor
        lstAgregar_horaIni.isEnabled = valor
        btnAgregar_agregar.isEnabled = valor
    }

    override fun onStart() {
        super.onStart()
        lstAgregar_materia.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                materiaSeleccionada = position
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }
        lstAgregar_dia.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                diaSeleccionado = position
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }
        lstAgregar_horaIni.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                horaSeleccionada = position
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }
    }
}
