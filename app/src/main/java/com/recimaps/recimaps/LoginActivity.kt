package com.recimaps.recimaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.recimaps.recimaps.databinding.ActivityLoginBinding
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        screenSplash.setKeepOnScreenCondition { false }
        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnCreateAccount.setOnClickListener {
            val intent = Intent( this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.tvRestorepass.setOnClickListener {
            val intent = Intent( this, RecorvePassActivity::class.java)
            startActivity(intent)
        }

        binding.btnStar.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()){

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful){
                        val intent = Intent(this, MapsActivity::class.java)
                        reciveId(email)
                        startActivity(intent)
                    } else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText( this , "No se permiten campos vacíos" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun reciveId(id: String){
        val recivedId = id
        userId = recivedId

    }
    fun getUserId():String{
        return userId
    }

     override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null){
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

}