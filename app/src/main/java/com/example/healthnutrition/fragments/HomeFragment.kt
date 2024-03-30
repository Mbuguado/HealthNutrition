package com.example.healthnutrition.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthnutrition.R
import com.example.healthnutrition.adapters.HerbAdapter
import com.example.healthnutrition.model.DiabetesHerb
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerView) // Fixed here
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        val adapter = HerbAdapter(emptyList())
        recyclerView.adapter = adapter

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




}