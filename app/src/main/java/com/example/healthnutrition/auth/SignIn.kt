package com.example.healthnutrition.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.healthnutrition.Constants.Constants
import com.example.healthnutrition.MainActivity
import com.example.healthnutrition.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignIn : AppCompatActivity() {

    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var emailContainer: TextInputLayout
    lateinit var passwordContainer: TextInputLayout
    lateinit var signInButton: Button
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var forgotPassword: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        emailContainer = findViewById(R.id.emailContainer)
        passwordContainer = findViewById(R.id.passwordContainer)
        signInButton = findViewById(R.id.login)
        forgotPassword = findViewById(R.id.forgot_password)
        firebaseAuth = FirebaseAuth.getInstance()



        val signUp : Button = findViewById(R.id.go_to_sign_up)
        signUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }

        signInButton.setOnClickListener { submitForm() }
        emailFocusListener()
        passwordFocusListener()

    }

    private fun submitForm() {
        val email: String = emailEditText.text.toString()
        val password: String = passwordEditText.text.toString()

        emailContainer.helperText = validEmail()
        passwordContainer.helperText = validPassword()

        val validEmail = emailContainer.helperText == null
        val validPassword = passwordContainer.helperText == null

        if (validEmail && validPassword)
            login(email, password)
        else
            invalidForm()
    }

    private fun login(email: String, password: String) {

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun invalidForm() {
        var message = ""
        if (emailContainer.helperText != null)
            message += "\n\nEmail: " + emailContainer.helperText
        if (passwordContainer.helperText != null)
            message += "\n\nPassword: " + passwordContainer.helperText

        AlertDialog.Builder(this)
            .setTitle("Invalid Form")
            .setMessage(message)
            .setPositiveButton("Okay"){_,_ ->
                //do nothing
            }
            .show()
    }

    /**
     * EMAIL VALIDATIONS
     */
    private fun emailFocusListener() {
        emailEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                emailContainer.helperText = validEmail()
            }
        }
    }
    private fun validEmail(): String? {
        val emailText = emailEditText.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Invalid Email Address"
        }
        return null
    }

    /**
     * PASSWORD VALIDATIONS
     */
    private fun passwordFocusListener() {
        passwordEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                passwordContainer.helperText = validPassword()
            }
        }
    }
    private fun validPassword(): String? {
        val passwordText = passwordEditText.text.toString()
        if (passwordText.length < 8) {
            return "Minimum 8 Character Password"
        }

        return null
    }

}