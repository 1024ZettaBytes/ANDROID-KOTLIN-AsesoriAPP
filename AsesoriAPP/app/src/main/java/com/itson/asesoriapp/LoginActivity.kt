package com.itson.asesoriapp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_registro.*

import java.nio.file.Files.exists

import com.google.firebase.firestore.DocumentSnapshot

import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull


class LoginActivity : AppCompatActivity() {
    lateinit private var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        firebaseAuth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar()?.hide() // hide the title bar
        setContentView(R.layout.activity_login)
// Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.getCurrentUser()
        if (currentUser != null) {
            abrirAvtivityCorrespondiente()
        }
        btn_registro.setOnClickListener() {
            var intent = Intent(this, RegistroActivity::class.java)
            startActivityForResult(intent, 1)
        }
        btn_login.setOnClickListener() {
            iniciarSesion()
        }
    }

    private fun iniciarSesion() {

        pgsBarCargaLogin.visibility = View.VISIBLE

        val correo = txt_LoginCorreo.text.toString()
        val pass = txt_LoginPass.text.toString()
        if (!correo.isEmpty() && !pass.isEmpty()) {
            this.firebaseAuth.signInWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    pgsBarCargaLogin.visibility = View.GONE
                    if (task.isSuccessful) {
                        if (FirebaseAuth.getInstance().currentUser!!.isEmailVerified)
                            abrirAvtivityCorrespondiente()
                        else {
                            Toast.makeText(this, "Verifique su correo para poder iniciar sesión.", Toast.LENGTH_SHORT)
                                .show()
                            FirebaseAuth.getInstance().signOut()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Compruebe que la información proporcionada sea la correcta",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

        } else {
            Toast.makeText(this, "Please fill up the Credentials :|", Toast.LENGTH_SHORT).show()
        }

    }

    private fun abrirAvtivityCorrespondiente() {
        pgsBarCargaLogin.visibility = View.VISIBLE
        val usuario = FirebaseAuth.getInstance().currentUser
        val alumnoDoc = FirebaseFirestore.getInstance().collection("alumnos").document(usuario?.email.toString())
        lateinit var intent: Intent
        alumnoDoc.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    intent = Intent(this, HorariosActivity::class.java)
                    intent.putExtra("campus", document.get("campus").toString().toInt())
                    intent.putExtra("carrera", document.get("carrera").toString())
                    pgsBarCargaLogin.visibility = View.GONE
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    val asesorDoc = FirebaseFirestore.getInstance().collection("asesores").document(usuario?.email.toString())
                    asesorDoc.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document.exists()) {
                                intent = Intent(this, AsesorActivity::class.java)
                                intent.putExtra("campus", document.get("campus").toString().toInt())
                                intent.putExtra("carrera", document.get("carrera").toString())
                                intent.putExtra("nombreAsesor", document.get("nombre").toString())
                                intent.putExtra("correoAsesor", document.id)
                                pgsBarCargaLogin.visibility = View.GONE
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Error: Credenciales incorrectas.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "Error: Compruebe su conexión de red.", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            } else {
                Toast.makeText(this, "Error: Compruebe su conexión de red.", Toast.LENGTH_SHORT).show()
            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Registro exitoso. Verifique su correo para iniciar sesión.", Toast.LENGTH_SHORT)
                .show()

        }
    }
}
