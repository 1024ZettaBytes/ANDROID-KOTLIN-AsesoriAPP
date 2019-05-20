package com.itson.asesoriapp

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_asesor.*
import kotlinx.android.synthetic.main.activity_horarios.*
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth


class AsesorActivity : AppCompatActivity() {
    var campus: Int = 0
    lateinit var carrera: String
    lateinit var nombreAsesor: String
    lateinit var correoAsesor: String
    var listaNumero = ArrayList<Int>()
    var listaAsesorias = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar()?.hide();
        // hide the title bar
        setContentView(R.layout.activity_asesor)
        campus = intent.getIntExtra("campus", 0)
        carrera = intent.getStringExtra("carrera")
        nombreAsesor = intent.getStringExtra("nombreAsesor")
        correoAsesor = intent.getStringExtra("correoAsesor")

        asesor_nombre.text = nombreAsesor
        asesor_siglasCarrera.text = carrera
        asesor_correo.text = correoAsesor
        llenarTabla()

        btnAgregarAsesoria.setOnClickListener() {
            var intentAgregar = Intent(this, AgregarAsesoriaActivity::class.java)
            intentAgregar.putExtra("campus", campus)
            intentAgregar.putExtra("carrera", carrera)
            intentAgregar.putExtra("correoAsesor", correoAsesor)
            intentAgregar.putExtra("nombreAsesor", nombreAsesor)
                intentAgregar.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intentAgregar)
        }

        btnAsesor_cerrarSesion.setOnClickListener() {
            FirebaseAuth.getInstance().signOut()
            var intentLogin = Intent(this, LoginActivity::class.java)
            intentLogin.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intentLogin)
        }

    }

    private fun llenarTabla() {

        pgsBarCargaAsesorias.visibility = View.VISIBLE

        var asesorias = FirebaseFirestore.getInstance().collection("asesorias").whereEqualTo("asesor", correoAsesor)
        asesorias.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {

                var i = 0
                for (document in task.result) {
                    listaAsesorias.add(document.id)
                    listaNumero.add(i)
                    val materia = document.get("materia").toString()
                    val dia = document.get("dia").toString()
                    val hora = document.get("hora").toString()
                    var tr = TableRow(this)
                    var lp = Cabecera.layoutParams
                    var lpTexto = asesor_ColumnaMateria.layoutParams

                    var tv1 = TextView(this)
                    tv1.text = materia
                    tv1.layoutParams = lpTexto
                    tv1.gravity = Gravity.CENTER
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17f)
                    tr.addView(tv1, 0)
                    var tv2 = TextView(this)
                    tv2.text = dia
                    tv2.layoutParams = lpTexto
                    tv2.gravity = Gravity.CENTER
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17f)

                    tr.addView(tv2, 1)
                    var tv3 = TextView(this)
                    tv3.text = hora
                    tv3.layoutParams = lpTexto
                    tv3.gravity = Gravity.CENTER
                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17f)
                    tr.addView(tv3, 2)
                    tr.layoutParams = lp
                    asesor_tablaAsesorias.addView(tr)
                    i++
                }

                for (n in listaNumero) {

                    asesor_tablaAsesorias.getChildAt(n+1).setOnClickListener() {
                        borrarAsesoria(n)
                    }

                }
                pgsBarCargaAsesorias.visibility = View.GONE
            } else {
                pgsBarCargaHorarios.visibility = View.GONE
                Toast.makeText(this, "ERROR: Verifique su conexión de red.", Toast.LENGTH_SHORT).show()
            }

        })
    }



    private fun borrarAsesoria(i: Int) {
        // Initialize a new instance of
        val builder = AlertDialog.Builder(this)
        // Set the alert dialog title
        builder.setTitle("ELIMINAR ASESORÍA")

        // Display a message on alert dialog
        builder.setMessage("¿Desea eliminar la asesoría seleccionada?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("BORRAR"){dialog, which ->
            var fb = FirebaseFirestore.getInstance().collection("asesorias").document(listaAsesorias[i])
                .delete()
                .addOnSuccessListener(OnSuccessListener<Void> {
                    Toast.makeText(this, "Se eliminó la asesoría.", Toast.LENGTH_SHORT).show()
                    var intetnAsesor = Intent(this, AsesorActivity::class.java)
                    intetnAsesor = Intent(this, AsesorActivity::class.java)
                    intetnAsesor.putExtra("campus", campus)
                    intetnAsesor.putExtra("carrera", carrera)
                    intetnAsesor.putExtra("nombreAsesor", nombreAsesor)
                    intetnAsesor.putExtra("correoAsesor", correoAsesor)
                    intetnAsesor.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intetnAsesor)
                })
                .addOnFailureListener(OnFailureListener { e ->
                    Toast.makeText(this, "ERROR: Verifique su conexión de red.", Toast.LENGTH_SHORT).show()
                })

        }

        // Display a neutral button on alert dialog
        builder.setNeutralButton("Cancelar"){_,_ ->
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }


}
