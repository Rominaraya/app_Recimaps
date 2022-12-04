package com.recimaps.recimaps

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.recimaps.recimaps.databinding.ActivityAddInterestPointBinding

class AddInterestPointActivity : AppCompatActivity() {

    private lateinit var savePointButton: Button
    private lateinit var cancelPointButton: Button
    private lateinit var latas: CheckBox
    private lateinit var plasticos: CheckBox
    private lateinit var carton: CheckBox
    private lateinit var vidrio: CheckBox
    private lateinit var libros: CheckBox
    private lateinit var ropa: CheckBox
    private lateinit var juguetes: CheckBox
    private lateinit var herramientas: CheckBox
    private lateinit var componentes: CheckBox
    private lateinit var otros: CheckBox
    private lateinit var descripcion: TextView
    private lateinit var nombre: TextView
    private lateinit var recGroup1: LinearLayout
    private lateinit var recGroup2: LinearLayout
    private lateinit var reuGroup1: LinearLayout
    private lateinit var reuGroup2: LinearLayout

    private var lat : Double = 0.0
    private var lon : Double = 0.0

    private var dataBase = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityAddInterestPointBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var email :String


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interest_point)
        binding = ActivityAddInterestPointBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        email = user!!.uid

        binding.bottomView.setOnItemSelectedListener {
            val profileInte = Intent(this, ProfileActivity::class.java)
            val mapsInte = Intent(this, MapsActivity::class.java)
            //val pointInte = Intent(this, AddInterestPointActivity::class.java)

            when (it.itemId) {

                R.id.perfil -> startActivity(profileInte)
                R.id.mapa -> startActivity(mapsInte)
                //R.id.publi -> startActivity(pointInte)
                else -> {
                }
            }
            true
        }

        recGroup1 = findViewById(R.id.recLay)
        recGroup2 = findViewById(R.id.recLay2)
        reuGroup1 = findViewById(R.id.reuLay)
        reuGroup2 = findViewById(R.id.reuLay2)
        latas = findViewById(R.id.recTLatas)
        plasticos = findViewById(R.id.recTPlast)
        carton = findViewById(R.id.recTCart)
        vidrio = findViewById(R.id.recTVidr)
        libros = findViewById(R.id.reuTLibros)
        ropa = findViewById(R.id.reuTRopa)
        juguetes = findViewById(R.id.reuTJug)
        herramientas = findViewById(R.id.reuTHerr)
        componentes = findViewById(R.id.reuTComp)
        otros = findViewById(R.id.reuTOtros)
        descripcion = findViewById(R.id.pointDescription)
        nombre = findViewById(R.id.pointName)

        savePointButton = findViewById(R.id.savePoint)
        cancelPointButton = findViewById(R.id.cancelAddPoint)

        val docRef = dataBase.collection("temp").document(email)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    lat = document.get("latitud").toString().toDouble()
                    lon = document.get("longitud").toString().toDouble()
                }
            }



        cancelPointButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        savePointButton.setOnClickListener {

            val location = LatLng(lat, lon)

            if (latas.isChecked || plasticos.isChecked || carton.isChecked || vidrio.isChecked ||
                libros.isChecked || ropa.isChecked || juguetes.isChecked || herramientas.isChecked ||
                componentes.isChecked || otros.isChecked) {
                dataBase.collection("PuntoReci").document("$lat,$lon").set(
                    hashMapOf(
                        "nombre" to nombre.text.toString(),
                        "coordenadas" to location,
                        "latas" to latas.isChecked,
                        "plasticos" to plasticos.isChecked,
                        "carton" to carton.isChecked,
                        "vidrio" to vidrio.isChecked,
                        "libros" to libros.isChecked,
                        "ropa" to ropa.isChecked,
                        "juguetes" to juguetes.isChecked,
                        "herramientas" to herramientas.isChecked,
                        "componentes" to componentes.isChecked,
                        "otros" to otros.isChecked,
                        "descripcion" to descripcion.text.toString()
                    )
                )
                Toast.makeText(this, "Punto agregado", Toast.LENGTH_SHORT).show()
                dataBase.collection("temp").document(email).delete()
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Debe marcar al menos un tipo de reciclaje",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }
}





