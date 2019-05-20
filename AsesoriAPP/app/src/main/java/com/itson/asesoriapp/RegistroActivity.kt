package com.itson.asesoriapp

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_horarios.*
import kotlinx.android.synthetic.main.activity_registro.*
import android.widget.AdapterView.OnItemSelectedListener
import com.google.firebase.auth.FirebaseAuth
import android.support.annotation.NonNull
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener




class RegistroActivity : AppCompatActivity(){

var listaCampus = ArrayList<String>()
var listaCarreras = ArrayList<String>()
    var listaUsuarios = ArrayList<String>()
    var indiceCampus = 0
    var indiceCarrera = 0
    var conexionCorrecta = false
    var tipo = "alumnos"
 lateinit private var campus:String
    lateinit private var carrera:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar()?.hide(); // hide the title bar
        setContentView(R.layout.activity_registro)
        pgsBarCargaDatosregistro.visibility =View.VISIBLE
        camposHabilita(false)
        btnRegistro_registrar.isEnabled = false
        // Setea elementos de lista de campus
       obtenerListaCampus()

        listaCampus.add("Seleccione campus...")
        val adaptadorCampus = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaCampus // Array
        )
        adaptadorCampus.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstRegistro_campus.adapter = adaptadorCampus
        // Setea elementos de lista de carreras
    obtenerListaCarreras()
        listaCarreras.add("Seleccione carrera...")
        val adaptadorCarreras = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item, // Layout
            listaCarreras // Array
        )
        adaptadorCarreras.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        lstRegistro_carrera.adapter = adaptadorCarreras
        obtenerListaUsuarios()

        btn_alumno.setOnClickListener(){
            btn_alumno.setBackgroundColor(resources.getColor(R.color.colorTexto))
            btn_alumno.setTextColor(Color.WHITE)
            btn_asesor.setBackgroundColor(resources.getColor(R.color.colorGris))
            btn_asesor.setTextColor(resources.getColor(R.color.colorTexto))
            tipo = "alumnos"
        }
        btn_asesor.setOnClickListener(){
            btn_asesor.setBackgroundColor(resources.getColor(R.color.colorTexto))
            btn_asesor.setTextColor(Color.WHITE)
            btn_alumno.setBackgroundColor(resources.getColor(R.color.colorGris))
            btn_alumno.setTextColor(resources.getColor(R.color.colorTexto))
            tipo = "asesores"
        }
        btnRegistro_cancelar.setOnClickListener(){
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        btnRegistro_registrar.setOnClickListener(){


            if(verificarCampos()){
                pgsBarRegistrar.visibility = View.VISIBLE
                registrarUsuario()
            }

        }
    }

    private fun registrarUsuario() {
        val id = txtRegistro_id.text.toString()
        val nombre = txtRegistro_nombre.text.toString()
        val correo = txtRegistro_correo.text.toString()
        val pass = txtRegistro_pass.text.toString()
        val mAuth = FirebaseAuth.getInstance()
        mAuth
            .createUserWithEmailAndPassword(correo,pass)
            .addOnCompleteListener(this) { task ->
                pgsBarRegistrar.visibility = View.GONE
                if (task.isSuccessful) {
                    //Verify Email
                    verifyEmail()
                    var usuario = HashMap<String, Any>()
                    var db = FirebaseFirestore.getInstance()
                    usuario.put("campus", indiceCampus)
                    usuario.put("carrera", carrera)
                    usuario.put("contraseña", pass)
                    usuario.put("idItson", id)
                    usuario.put("nombre", nombre)
                    db.collection(tipo).document(correo)
                        .set(usuario)
                        .addOnSuccessListener {
                                setResult(Activity.RESULT_OK)
                            FirebaseAuth.getInstance().signOut()
                            finish()
                        }

                } else {

                    Toast.makeText(this, "Ocurrió un error. Compruebe su conexión de red.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun verifyEmail() {
        val mUser =FirebaseAuth.getInstance().currentUser
        mUser!!.sendEmailVerification()
    }

    fun verificarCampos(): Boolean{
        val id = txtRegistro_id.text.toString()
        val nombre = txtRegistro_nombre.text.toString()
        val correo = txtRegistro_correo.text.toString()
        val pass = txtRegistro_pass.text.toString()
        val passConfirm = txtRegistro_passConfirm.text.toString()
        if(id.length==11){
           if(!nombre.isEmpty()){
               if(indiceCampus>0 && indiceCarrera>0){
                    if (!correo.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                        if(!pass.isEmpty() && pass.length>8){
                            return if(passConfirm==pass){
                                if(!listaUsuarios.contains(correo)){
                                    true
                                } else{
                                    Toast.makeText(this,"El correo ya se encuentra registrado.",Toast.LENGTH_SHORT).show()
                                    false
                                }
                            }else{
                                Toast.makeText(this,"Las contraseñas no coinciden.",Toast.LENGTH_SHORT).show()
                                false
                            }
                        }
                        else{
                            Toast.makeText(this,"Introduzca una contraseña válida.",Toast.LENGTH_SHORT).show()
                            return false
                        }
                    }
                   else{
                        Toast.makeText(this,"Introduzca un correo válido.",Toast.LENGTH_SHORT).show()
                        return false
                    }
               }
               else{
                   Toast.makeText(this,"Seleccione su campus y carrera.",Toast.LENGTH_SHORT).show()
                   return false
               }
           }
            else{
               Toast.makeText(this,"Indique su nombre completo.",Toast.LENGTH_SHORT).show()
               return false
           }
        }
        else{
            Toast.makeText(this,"El ID indicado no es correcto.",Toast.LENGTH_SHORT).show()
            return false
        }
    }
    private fun obtenerListaCampus(){
        val aber = FirebaseFirestore.getInstance().collection("campus").get()
        aber.addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    listaCampus.add(document.get("nombre").toString());
                }
            conexionCorrecta = conexionCorrecta && true
            }
            else{
                conexionCorrecta = conexionCorrecta && false
            }
        })

    }
private fun obtenerListaCarreras(){
    val aber = FirebaseFirestore.getInstance().collection("carreras").get()
    aber.addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
        if (task.isSuccessful) {
            for (document in task.result) {
                listaCarreras.add(document.id);
            }
            conexionCorrecta = conexionCorrecta && true
        }
        else
            conexionCorrecta = conexionCorrecta && false

    })
}
    private fun obtenerListaUsuarios(){
        val aber = FirebaseFirestore.getInstance().collection("alumnos").get()
        aber.addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    listaUsuarios.add(document.id)
                }
            }
        })
        val aber2 = FirebaseFirestore.getInstance().collection("asesores").get()
        aber.addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    listaUsuarios.add(document.id)
                }
                conexionCorrecta = conexionCorrecta && true
                camposHabilita(true)

            }
            else{
                conexionCorrecta = conexionCorrecta && false
                Toast.makeText(this, "ERROR: Verfifique su conexión a internet.", Toast.LENGTH_SHORT).show()
            }
            pgsBarCargaDatosregistro.visibility =View.GONE

        })
    }
    fun camposHabilita(valor: Boolean){
        btn_alumno.isEnabled = valor
        btn_asesor.isEnabled = valor
        txtRegistro_id.isEnabled = valor
        txtRegistro_nombre.isEnabled = valor
        lstRegistro_campus.isEnabled = valor
        lstRegistro_carrera.isEnabled = valor
        txtRegistro_correo.isEnabled = valor
        txtRegistro_pass.isEnabled = valor
        txtRegistro_passConfirm.isEnabled = valor
        btnRegistro_registrar.isEnabled = valor
    }
    override fun onStart() {
        super.onStart()
        lstRegistro_campus.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
             if(position>0){
                 indiceCampus = position
                 campus = listaCampus[indiceCampus]
             }

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }
        lstRegistro_carrera.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if(position>0){
                    indiceCarrera = position
                    carrera = listaCarreras[indiceCarrera]
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }
    }

}
