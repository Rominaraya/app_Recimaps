package com.recimaps.recimaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.recimaps.recimaps.databinding.ActivityProfileBinding


class ProfileActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityProfileBinding
    private lateinit var bd: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        bd = FirebaseFirestore.getInstance()


        binding.bottomView.setOnItemSelectedListener {
            //val profileInte = Intent(this, ProfileActivity::class.java)
            val mapsInte = Intent(this, MapsActivity::class.java)
            val pointInte = Intent(this, AddInterestPointActivity::class.java)

            when (it.itemId) {

                //R.id.perfil -> startActivity(profileInte)
                R.id.mapa -> startActivity(mapsInte)
                R.id.publi -> startActivity(pointInte)
                else -> {
                }
            }
            true
        }


        val user = firebaseAuth.currentUser
        val uid = user!!.uid

        bd.collection("users").document(uid).get().addOnSuccessListener {
            binding.tvNom.text = (it.get("Nombre")as String?)
            binding.tvNombrePerfil.text = (it.get("Nombre Usuario") as String?)
            binding.tvNombreUsuario.text = (it.get("Nombre Usuario") as String?)
            binding.tvNombrePerfil2.text = (it.get("Nombre") as String?)
            binding.tvCorreoPerfil.text = (it.get("Email") as String?)
        }


        binding.closebtn.setOnClickListener {

            firebaseAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
    }

}



