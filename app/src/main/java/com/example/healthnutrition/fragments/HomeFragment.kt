package com.example.healthnutrition.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthnutrition.R
import com.example.healthnutrition.adapters.ItemAdapter
import com.example.healthnutrition.model.ItemModel


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)

        val items = listOf(
            ItemModel(R.drawable.gymnema, "Gymnema"),
            ItemModel(R.drawable.fenugreek, "Fenugreek"),
            ItemModel(R.drawable.hibiscus, "Hibiscus")
        )

        recyclerView.layoutManager = LinearLayoutManager(context)
        itemAdapter = ItemAdapter(items)
        recyclerView.adapter = itemAdapter
    }


}