package com.example.healthnutrition.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.healthnutrition.Constants.Constants
import com.example.healthnutrition.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class SignIn : AppCompatActivity() {

    lateinit var googleSignInClient: GoogleSignInClient
    val requestCode = Constants.REQ_CODE
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signIn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        //INITIALIZE FIREBASEAUTH
        FirebaseApp.initializeApp(this)
        signIn = findViewById(R.id.login)

        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)
        firebaseAuth = FirebaseAuth.getInstance()

        signIn.setOnClickListener { view: View? ->
            Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
            signInGoogle()
        }

    }

    private fun signInGoogle() {

    }
}