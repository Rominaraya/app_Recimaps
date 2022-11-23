package com.recimaps.recimaps

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.recimaps.recimaps.databinding.ActivityAddInterestPointBinding

class AddInterestPointActivity : AppCompatActivity() {

    private lateinit var recycl: RadioButton
    private lateinit var reut: RadioButton
    private var group1: RadioGroup? = null
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

    private var dataBase = FirebaseFirestore.getInstance()
    private lateinit var mapsCoord: String
    private lateinit var binding: ActivityAddInterestPointBinding
    private lateinit var email: String


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interest_point)
        binding = ActivityAddInterestPointBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        email = bundle?.getString("email").toString()



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
        group1 = findViewById(R.id.rGroup)
        recycl = findViewById(R.id.recycling)
        reut = findViewById(R.id.reutilization)
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

        val docRef = dataBase.collection("coordenadas").document(email)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    mapsCoord = document.get("coordenada") as String
                }
            }

        val latlong = "-34.8799074,174.7565664".split(",".toRegex()).toTypedArray()
        val latitude = latlong[0].toDouble()
        val longitude = latlong[1].toDouble()
        val location = LatLng(latitude, longitude)

        cancelPointButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        savePointButton.setOnClickListener {
            if (recycl.isChecked) {
                if (latas.isChecked || plasticos.isChecked || carton.isChecked || vidrio.isChecked) {
                    dataBase.collection("PuntoReci").document(email).set(
                        hashMapOf(
                            "nombre" to nombre.text.toString(),
                            "coordenadas" to location,
                            "latas" to latas.isChecked,
                            "plasticos" to plasticos.isChecked,
                            "carton" to carton.isChecked,
                            "vidrio" to vidrio.isChecked,
                            "descripcion" to descripcion.text.toString()
                        )
                    )
                    Toast.makeText(this, "Punto agregado", Toast.LENGTH_SHORT).show()
                    dataBase.collection("coordenadas").document(email).delete()
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "Debe marcar al menos un tipo de reciclaje",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else if (reut.isChecked) {
                if (libros.isChecked || ropa.isChecked || juguetes.isChecked || herramientas.isChecked ||
                    componentes.isChecked || otros.isChecked
                ) {
                    dataBase.collection("PuntoReu").document(email).set(
                        hashMapOf(
                            "nombre" to nombre.text.toString(),
                            "coordenadas" to location,
                            "libros" to libros.isChecked,
                            "ropa" to ropa.isChecked,
                            "juguetes" to juguetes.isChecked,
                            "herramientas" to herramientas.isChecked,
                            "componentes" to componentes.isChecked,
                            "otros" to otros.isChecked,
                            "descripcion" to descripcion.text.toString()
                        )
                    )
                    dataBase.collection("coordenadas").document(email).delete()
                    Toast.makeText(this, "Punto agregado", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "Debe marcar al menos un tipo de reutilización",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }
}





