package com.example.healthnutrition.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.healthnutrition.R
import com.example.healthnutrition.auth.SignIn
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        // Find the button by its ID and set an OnClickListener
        view.findViewById<Button>(R.id.logout).setOnClickListener {
            logOut()
        }

        return view
    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(context, SignIn::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        (context as Activity).finish()


    }

}