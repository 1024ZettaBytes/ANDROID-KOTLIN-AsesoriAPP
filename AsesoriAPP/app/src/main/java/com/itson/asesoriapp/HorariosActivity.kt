package com.itson.asesoriapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_horarios.*

class HorariosActivity : AppCompatActivity() {
    var campus = 0
    var carreraAlumno = ""
    var listaMaterias = ArrayList<String>()
    var listaAsesorias = ArrayList<HashMap<String, Any?>>()
    var asesoriasLunes = ArrayList<HashMap<String, Any?>>()
    var asesoriasMartes = ArrayList<HashMap<String, Any?>>()
    var asesoriasMiercoles = ArrayList<HashMap<String, Any?>>()
    var asesoriasJueves = ArrayList<HashMap<String, Any?>>()
    var asesoriasViernes = ArrayList<HashMap<String, Any?>>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar()?.hide(); // hide the title bar
        campus = intent.getIntExtra("campus", 0)
        carreraAlumno = intent.getStringExtra("carrera")
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
        val listaLunes = arrayOf("LUNES")
        val adaptadorLunes = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaLunes // Array
        )
        adaptadorLunes.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstHorarios_lunes.adapter = adaptadorLunes

        // Setea elementos de lista de cada dia
        val listaMartes = arrayOf("MARTES")
        val adaptadorMartes = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaMartes // Array
        )
        adaptadorMartes.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstHorarios_martes.adapter = adaptadorMartes
        // Setea elementos de lista de cada dia
        val listaMiercoles = arrayOf("MIÉRCOLES")
        val adaptadorMiercoles = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaMiercoles // Array
        )
        adaptadorMiercoles.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstHorarios_miercoles.adapter = adaptadorMiercoles
        // Setea elementos de lista de cada dia
        val listaJueves = arrayOf("JUEVES")
        val adaptadorJueves = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaJueves // Array
        )
        adaptadorJueves.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstHorarios_jueves.adapter = adaptadorJueves
        // Setea elementos de lista de cada dia
        val listaViernes = arrayOf("VIERNES")
        val adaptadorViernes = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaViernes // Array
        )

        adaptadorViernes.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstHorarios_viernes.adapter = adaptadorViernes
        habilitarCampos(false)




        btnAlumno_cerrarSesion.setOnClickListener() {
            finish()
        }
        cargarDatos()

    }

    private fun cargarDatos() {
        pgsBarCargaHorarios.visibility = View.VISIBLE
        listaMaterias.add("Seleccione una materia...")
        var asesorias = FirebaseFirestore.getInstance().collection("asesorias").whereEqualTo("campus", 1)
        asesorias.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var i = ""
                for (document in task.result) {
                    i = document.get("carrera").toString()
                    if (i == carreraAlumno) {
                        val materia = document.get("materia").toString()
                        if (!listaMaterias.contains(materia))
                            listaMaterias.add(materia)
                        val dia = document.get("dia").toString()
                        var asesoria = HashMap<String, Any?>()
                        asesoria.put("correoAsesor", document.get("asesor").toString())
                        asesoria.put("dia", document.get("dia").toString())
                        asesoria.put("hora", document.get("hora").toString())
                        asesoria.put("materia", document.get("materia").toString())
                        var asesor = FirebaseFirestore.getInstance().collection("asesores")
                            .document(document.get("asesor").toString())
                        asesor.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                            if (task.isSuccessful) {
                                val document = task.result
                                asesoria.put("nombreAsesor", document.get("nombre"))
                            } else {
                                Toast.makeText(this, "Error: Compruebe su conexión de red.", Toast.LENGTH_SHORT).show()
                            }
                        })
                        listaAsesorias.add(asesoria)

                    }
                }
                pgsBarCargaHorarios.visibility = View.GONE

                // Si hay más de una asesoria para la carrera
                if (listaMaterias.size > 1) {
                    val adaptadorMaterias = ArrayAdapter(
                        this, // Context
                        android.R.layout.simple_spinner_item, // Layout
                        listaMaterias // Array
                    )
                    adaptadorMaterias.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                    lstHorarios_materia.adapter = adaptadorMaterias
                    lstHorarios_materia.isEnabled = true

                } else {
    habilitarCampos(false)
                    Toast.makeText(this, "No se encontraron asesorías para tu carrera.", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                pgsBarCargaHorarios.visibility = View.GONE
                Toast.makeText(this, "ERROR: Verifique su conexión de red.", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun habilitarCampos(valor: Boolean) {
        lstHorarios_materia.isEnabled = valor
        lstHorarios_lunes.isEnabled = valor
        lstHorarios_martes.isEnabled = valor
        lstHorarios_miercoles.isEnabled = valor
        lstHorarios_jueves.isEnabled = valor
        lstHorarios_viernes.isEnabled = valor
    }

    override fun onStart() {
        super.onStart()
        lstHorarios_materia.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if(position>0){
                    habilitarCampos(false)
                    lstHorarios_materia.isEnabled = true
                    asesoriasLunes = ArrayList<HashMap<String, Any?>>()
                    asesoriasMartes = ArrayList<HashMap<String, Any?>>()
                   asesoriasMiercoles = ArrayList<HashMap<String, Any?>>()
                     asesoriasJueves = ArrayList<HashMap<String, Any?>>()
                     asesoriasViernes = ArrayList<HashMap<String, Any?>>()
                    var materia = parentView.selectedItem.toString()
                    for (l in listaAsesorias){
                        if(l["materia"] == materia){
                            val dia = l["dia"].toString()
                            when(dia){
                                "LUNES" -> asesoriasLunes.add(l)
                                "MARTES" -> asesoriasMartes.add(l)
                                "MIÉRCOLES" -> asesoriasMiercoles.add(l)
                                "JUEVES" -> asesoriasJueves.add(l)
                                "VIERNES" -> asesoriasViernes.add(l)
                            }
                        }
                    }
                    pgsBarCargaDias.visibility = View.VISIBLE
                    establecerListas()
                    pgsBarCargaDias.visibility = View.GONE
                }
                else{
                    habilitarCampos(false)
                    lstHorarios_materia.isEnabled= true
                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }
        lstHorarios_lunes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if(position>0){
                    lstHorarios_lunes.setSelection(0)
                    abrirDetalle(1, position-1)
                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }
        lstHorarios_martes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if(position>0){
                    lstHorarios_martes.setSelection(0)
                    abrirDetalle(2, position-1)
                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }
        lstHorarios_miercoles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if(position>0){
                    lstHorarios_miercoles.setSelection(0)
                    abrirDetalle(3, position-1)
                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }
        lstHorarios_jueves.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if(position>0){
                    lstHorarios_jueves.setSelection(0)
                    abrirDetalle(4, position-1)
                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }
        lstHorarios_viernes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if(position>0){
                    lstHorarios_viernes.setSelection(0)
                    abrirDetalle(5, position-1)
                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }
    }

    private fun abrirDetalle(dia: Int, valor: Int) {
        var materia: String
        var diaSemana: String
        var hora: String
        var nombreAsesor: String
        var correoAsesor: String
        var datos = HashMap<String, Any?>()
        when(dia){
            1 -> datos = asesoriasLunes[valor]
            2 -> datos = asesoriasMartes[valor]
            3 -> datos = asesoriasMiercoles[valor]
            4 -> datos = asesoriasJueves[valor]
            5 -> datos = asesoriasViernes[valor]
        }
        materia = datos["materia"].toString()
        diaSemana = datos["dia"].toString()
        hora = datos["hora"].toString()
        nombreAsesor = datos["nombreAsesor"].toString()
        correoAsesor = datos["correoAsesor"].toString()
        val intent = Intent(this, DetalleAsesoriaActivity::class.java)
        intent.putExtra("materia", materia)
        intent.putExtra("dia", diaSemana)
        intent.putExtra("hora", hora)
        intent.putExtra("nombreAsesor", nombreAsesor)
        intent.putExtra("correoAsesor", correoAsesor)
        startActivity(intent)
    }

    fun establecerListas(){
        if(asesoriasLunes.size>0){
            val listaLunes = ArrayList<String>()
            listaLunes.add("LUNES")
            for(a in asesoriasLunes){
                listaLunes.add(a.get("hora").toString()+" - "+a.get("nombreAsesor").toString())
            }
            val adaptadorLunes = ArrayAdapter(
                this, // Context
                android.R.layout.simple_spinner_item, // Layout
                listaLunes // Array
            )
            adaptadorLunes.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            lstHorarios_lunes.adapter = adaptadorLunes
            lstHorarios_lunes.isEnabled = true
        }
        if(asesoriasMartes.size>0){
            val listaMartes = ArrayList<String>()
            listaMartes.add("MARTES")
            for(a in asesoriasMartes){
                listaMartes.add(a.get("hora").toString()+" - "+a.get("nombreAsesor").toString())
            }
            val adaptadorMartes = ArrayAdapter(
                this, // Context
                android.R.layout.simple_spinner_item, // Layout
                listaMartes// Array
            )
            adaptadorMartes.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            lstHorarios_martes.adapter = adaptadorMartes
            lstHorarios_martes.isEnabled = true
        }
        if(asesoriasMiercoles.size>0){
            val listaMiercoles = ArrayList<String>()
            listaMiercoles.add("MIÉRCOLES")
            for(a in asesoriasMiercoles){
                listaMiercoles.add(a.get("hora").toString()+" - "+a.get("nombreAsesor").toString())
            }
            val adaptadorMiercoles = ArrayAdapter(
                this, // Context
                android.R.layout.simple_spinner_item, // Layout
                listaMiercoles // Array
            )
            adaptadorMiercoles.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            lstHorarios_miercoles.adapter = adaptadorMiercoles
            lstHorarios_miercoles.isEnabled = true
        }
        if(asesoriasJueves.size>0){
            val listaJueves = ArrayList<String>()
            listaJueves.add("JUEVES")
            for(a in asesoriasJueves){
                listaJueves.add(a.get("hora").toString()+" - "+a.get("nombreAsesor").toString())
            }
            val adaptadorJueves = ArrayAdapter(
                this, // Context
                android.R.layout.simple_spinner_item, // Layout
                listaJueves // Array
            )
            adaptadorJueves.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            lstHorarios_jueves.adapter = adaptadorJueves
            lstHorarios_jueves.isEnabled = true
        }
        if(asesoriasViernes.size>0){
            val listaViernes = ArrayList<String>()
            listaViernes.add("VIERNES")
            for(a in asesoriasViernes){
                listaViernes.add(a.get("hora").toString()+" - "+a.get("nombreAsesor").toString())
            }
            val adaptadorViernes = ArrayAdapter(
                this, // Context
                android.R.layout.simple_spinner_item, // Layout
                listaViernes // Array
            )
            adaptadorViernes.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            lstHorarios_viernes.adapter = adaptadorViernes
            lstHorarios_viernes.isEnabled = true
        }
    }
}
