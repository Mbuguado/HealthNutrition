package com.example.healthnutrition.fragments

import android.app.Dialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidstudy.daraja.Daraja
import com.androidstudy.daraja.callback.DarajaResult
import com.androidstudy.daraja.util.Environment
import com.example.healthnutrition.Constants.Constants
import com.example.healthnutrition.R
import com.example.healthnutrition.adapters.HerbAdapter
import com.example.healthnutrition.model.DiabetesHerb
import com.example.healthnutrition.model.HerbX
import com.example.healthnutrition.mpesa.AppUtils
import com.example.healthnutrition.mpesa.Config
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var daraja: Daraja
    private lateinit var progressDialog: ProgressDialogFragment
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerView) // Fixed here
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        val adapter = HerbAdapter(emptyList()) {
            showPhoneNumberInputDialog()
        }
        daraja = getDaraja()
        accessToken()
        recyclerView.adapter = adapter

        fab = view.findViewById(R.id.chat)
        fab
            .setOnClickListener {
                showGreetingDialog()
            }

        showDiabetesTypeDialog()

        return view
    }

    private fun showDiabetesTypeDialog() {
        val types = arrayOf("Type1", "Type2", "Common")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Your Diabetes Type")
        builder.setItems(types) { _, which ->
            val selectedType = types[which]
            fetchHerbsForType(selectedType)
        }
        builder.show()
    }

    private fun fetchHerbsForType(type: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("DiabetesHerbs")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val diabetesHerbsList = snapshot.children.mapNotNull { it.getValue(DiabetesHerb::class.java) }
                val selectedTypeHerbs = diabetesHerbsList.find { it.Type.equals(type, ignoreCase = true) }?.Herbs
                selectedTypeHerbs?.let {
                    (recyclerView.adapter as? HerbAdapter)?.updateHerbs(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle potential errors.
                Log.w("HomeFragment", "loadPost:onCancelled", error.toException())
            }
        })
    }

    private fun getDaraja(): Daraja {
        return Daraja.builder(Config.CONSUMER_KEY, Config.CONSUMER_SECRET)
            .setBusinessShortCode(Config.BUSINESS_SHORTCODE)
            .setPassKey(AppUtils.passKey)
            .setTransactionType(Config.ACCOUNT_TYPE)
            .setCallbackUrl(Config.CALLBACK_URL)
            .setEnvironment(Environment.SANDBOX)
            .build()
    }


    private fun showPhoneNumberInputDialog() {
        val input = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_PHONE
            hint = "Enter phone number"
        }

        AlertDialog.Builder(requireContext()).apply {
            setTitle("Phone Number for Payment")
            setView(input)
            setPositiveButton("Pay") { _, _ ->
                val phoneNumber = input.text.toString()
                initiateMpesaPayment(phoneNumber)
            }
            setNegativeButton("Cancel", null)
        }.show()
    }

    private fun initiateMpesaPayment(phoneNumber: String) {
        val token = AppUtils.getAccessToken(requireContext())
        if (token == null) {
            accessToken()
            toast("Your access token was refreshed. Retry again")
        } else {
            showProgressDialog()
            daraja.initiatePayment(
                token,
                phoneNumber,
                Constants.MPESA_PAYMENT_AMOUNT.toString(),
                AppUtils.generateUUID(),
                "Payment"
            ) { darajaResult ->
                dissmissProgressDialog()
                when (darajaResult) {
                    is DarajaResult.Success -> {
                        val result = darajaResult.value
                        toast(result.ResponseDescription)
                    }
                    is DarajaResult.Failure -> {
                        val exception = darajaResult.darajaException
                        if (darajaResult.isNetworkError) {
                            toast(exception?.message ?: "Network Error!")
                        } else {
                            toast(exception?.message ?: "Payment Failed")
                        }
                    }
                }
            }
        }

    }

    private fun accessToken() {
        showProgressDialog()
        daraja.getAccessToken { darajaResult ->
            dissmissProgressDialog()
            when (darajaResult) {
                is DarajaResult.Success -> {
                    val accessToken = darajaResult.value
                    AppUtils.saveAccessToken(requireContext(), accessToken.access_token)
                }
                is DarajaResult.Failure -> {
                    val darajaException = darajaResult.darajaException
                    toast(darajaException?.message ?: "An error occurred!")
                }
            }
        }
    }

    private fun toast(text: String) =
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()

    private fun showProgressDialog(title: String = "This will only take a sec", message: String = "Loading") {
        progressDialog = ProgressDialogFragment.newInstance(title, message)
        progressDialog.isCancelable = false
        progressDialog.show(childFragmentManager, "Progress")

    }

    private fun dissmissProgressDialog() {
        progressDialog.dismiss()
    }

    private fun showGreetingDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_chat)
        dialog.setTitle("Chat")

        val sendButton: Button = dialog.findViewById(R.id.sendButton)
        val inputMessage: EditText = dialog.findViewById(R.id.inputMessage)

        sendButton.setOnClickListener {
            val message = inputMessage.text.toString()
            sendMessageToFirestore(message)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun sendMessageToFirestore(message: String) {
        val db = FirebaseFirestore.getInstance()
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "anonymous"
        val chatMessage = hashMapOf(
            "message" to message,
            "timestamp" to FieldValue.serverTimestamp(),
            "sender" to userEmail
        )

        db.collection("chats")
            .add(chatMessage)
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "We Will get back to you shortly",
                    Toast.LENGTH_LONG
                ).show()
            }
            .addOnFailureListener {
                Log.w(TAG, "Error adding document")
            }
    }



}