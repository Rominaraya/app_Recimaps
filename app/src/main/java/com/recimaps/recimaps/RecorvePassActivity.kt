package com.recimaps.recimaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.recimaps.recimaps.databinding.ActivityRecorvePassBinding


class RecorvePassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecorvePassBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecorvePassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.tvReturnInit.setOnClickListener {
            val intent = Intent( this , LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnSend.setOnClickListener {
            val email = binding.etEmailAddress.text.toString()

            if (email.isNotEmpty()) {

                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Contraseña enviada", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText( this , "No se permiten campos vacíos" , Toast.LENGTH_SHORT).show()
            }
        }
    }
}