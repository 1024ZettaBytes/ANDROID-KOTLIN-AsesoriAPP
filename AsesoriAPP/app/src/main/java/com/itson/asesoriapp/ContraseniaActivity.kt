package com.itson.asesoriapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_contrasenia.*

class ContraseniaActivity : AppCompatActivity() {
    lateinit var correo: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contrasenia)
        btn_enviarContra.setOnClickListener() {
            if (verificaDatos()) {

                enviarCorreo()
            }
        }
        btn_contraCancelar.setOnClickListener() {
            finish()
        }
    }

    private fun verificaDatos(): Boolean {
        correo = txt_enviarCorreo.text.toString()
        return if (!correo.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            true
        } else {
            Toast.makeText(this, "Introduzca un correo válido.", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun enviarCorreo() {
pgsBarCargaEnviaCorreo.visibility = View.VISIBLE
        val alumnoDoc = FirebaseFirestore.getInstance().collection("alumnos").document(correo)
        alumnoDoc.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {

                    envia()

                } else {
                    val asesorDoc = FirebaseFirestore.getInstance().collection("asesores").document(correo)
                    asesorDoc.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document.exists()) {

                                envia()
                            } else {
                                pgsBarCargaEnviaCorreo.visibility = View.GONE
                                Toast.makeText(
                                    this,
                                    "El correo proporcionado no se encuentra registrado.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            pgsBarCargaEnviaCorreo.visibility = View.GONE
                            Toast.makeText(this, "Error: Compruebe su conexión de red.", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            } else {
                pgsBarCargaEnviaCorreo.visibility = View.GONE
                Toast.makeText(this, "Error: Compruebe su conexión de red.", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun envia() {
        var fb = FirebaseAuth.getInstance()
        fb.sendPasswordResetEmail(correo).addOnSuccessListener {
            pgsBarCargaEnviaCorreo.visibility = View.GONE
            finish()
            Toast.makeText(this, "El correo de restablecimiento ha sido enviado.", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            pgsBarCargaEnviaCorreo.visibility = View.GONE
            Toast.makeText(this, "Error: Compruebe su conexión de red e intente nuevamente.", Toast.LENGTH_SHORT).show()
        }
    }
}
