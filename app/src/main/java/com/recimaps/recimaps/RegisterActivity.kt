package com.recimaps.recimaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.recimaps.recimaps.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.tvRegisterReady.setOnClickListener {
            val intent = Intent( this , LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnSend.setOnClickListener {
            val email = binding.etEmailAddress.text.toString()
            val pass = binding.etPassword.text.toString()
            val confirmPass = binding.etConfirmation.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()){
                if (pass == confirmPass){

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{
                        if (it.isSuccessful){
                            val intent = Intent( this , LoginActivity::class.java)
                            startActivity(intent)

                        }else{
                            Toast.makeText( this , it.exception.toString() , Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText( this , "La contraseña no coincide" , Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText( this , "No se permiten campos vacíos" , Toast.LENGTH_SHORT).show()
            }
        }
    }
}