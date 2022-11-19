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
import com.google.firebase.firestore.FirebaseFirestore
import com.recimaps.recimaps.databinding.ActivityProfileBinding


class ProfileActivity : AppCompatActivity() {

    private lateinit var closePointButton: Button
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomView.setOnItemSelectedListener {
            val profileInte = Intent(this, ProfileActivity::class.java)
            val mapsInte = Intent(this, MapsActivity::class.java)
            val pointInte = Intent(this, AddInterestPointActivity::class.java)

            when(it.itemId){

                R.id.perfil -> startActivity(profileInte)
                R.id.mapa -> startActivity(mapsInte)
                R.id.publi -> startActivity(pointInte)
                else ->{
                }
            }
            true
        }

        closePointButton = findViewById(R.id.closebtn)
        closePointButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}


