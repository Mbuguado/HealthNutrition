package com.example.healthnutrition.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.healthnutrition.MainActivity
import com.example.healthnutrition.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var emailContainer: TextInputLayout
    private lateinit var passwordContainer: TextInputLayout
    private lateinit var nameContainer: TextInputLayout
    private lateinit var countryContainer: TextInputLayout
    private lateinit var numberContainer: TextInputLayout
    private lateinit var signUpButton: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        nameEditText = findViewById(R.id.fullNameEditText)
        countryEditText = findViewById(R.id.countryEditText)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        emailContainer = findViewById(R.id.emailContainer)
        passwordContainer = findViewById(R.id.passwordContainer)
        nameContainer = findViewById(R.id.nameContainer)
        countryContainer = findViewById(R.id.countryContainer)
        numberContainer = findViewById(R.id.numberContainer)
        signUpButton = findViewById(R.id.signup)
        firebaseAuth = FirebaseAuth.getInstance()

        haveAnAccount()

        /**
         * VALIDATION FUNCTIONS
         */
        emailFocusListener()
        passwordFocusListener()
        nameFocusedListener()
        countryFocusedListener()
        phoneNumberFocusedListener()


        signUpButton.setOnClickListener {
            submitForm()
        }


    }

    private fun submitForm() {

        val email: String = emailEditText.text.toString()
        val password: String = passwordEditText.text.toString()
        val name: String = nameEditText.text.toString()
        val phoneNumber: String = phoneNumberEditText.text.toString()
        val country: String = countryEditText.text.toString()

        emailContainer.helperText = validEmail()
        passwordContainer.helperText = validPassword()
        nameContainer.helperText = validName()
        countryContainer.helperText = validCountry()
        numberContainer.helperText = validNumber()

        val validEmail = emailContainer.helperText == null
        val validPassword = passwordContainer.helperText == null
        val validName = nameContainer.helperText == null
        val validCountry = countryContainer.helperText == null
        val validNumber = numberContainer.helperText == null

        if (validEmail && validPassword && validName && validCountry && validNumber)
            sendData(email, password, name, country, phoneNumber)
        else
            invalidForm()

    }

    /**
     * POST DATA
     */

    private fun sendData(email: String, password: String, name: String, country: String, phoneNumber: String) {

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun invalidForm() {
        var message = ""
        if (emailContainer.helperText != null)
            message += "\n\nEmail: " + emailContainer.helperText
        if (passwordContainer.helperText != null)
            message += "\n\nPassword: " + passwordContainer.helperText
        if (nameContainer.helperText != null)
            message += "\n\nFull Name: " + nameContainer.helperText
        if (countryContainer.helperText != null)
            message += "\n\nCountry: " + countryContainer.helperText
        if (numberContainer.helperText != null)
            message += "\n\nPhone Number: " + numberContainer.helperText

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
     * PHONE NUMBER VALIDATIONS
     */
    private fun phoneNumberFocusedListener() {
        phoneNumberEditText.setOnFocusChangeListener {_, focused ->
            if (!focused) {
                numberContainer.helperText = validNumber()
            }
        }
    }

    private fun validNumber(): String? {
        val numberText = phoneNumberEditText.text.toString()
        if (numberText.isEmpty()) {
            return "Phone Number Can Not Be Empty"
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
        if (!passwordText.matches(".*[A-Z].*".toRegex())) {
            return "Must Contain 1 Upper-case Character"
        }
        if (!passwordText.matches(".*[a-z].*".toRegex())) {
            return "Must Contain 1 Lower-case Character"
        }

        return null
    }

    /**
     * FULL NAME VALIDATIONS
     */
    private fun nameFocusedListener() {
        nameEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                nameContainer.helperText = validName()
            }
        }
    }
    private fun validName(): String? {
        val nameText = nameEditText.text.toString()
        if (nameText.isEmpty()) {
            return "Full Name Can Not Be Empty"
        }
        return null
    }

    /**
     * COUNTRY VALIDATIONS
     */
    private fun countryFocusedListener() {
        countryEditText.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                countryContainer.helperText = validCountry()
            }
        }
    }
    private fun validCountry(): String? {
        val countryText = countryEditText.text.toString()
        if (countryText.isEmpty()) {
            return "Country Can Not Be Empty"
        }
        return null
    }

    private fun haveAnAccount() {
        val signin:Button = findViewById(R.id.go_to_sign_in)
        signin.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }
    }
}