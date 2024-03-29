// HerbAdapter.kt
package com.example.healthnutrition.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthnutrition.R
import com.example.healthnutrition.model.HerbX

class HerbAdapter(private var herbs: List<HerbX>) : RecyclerView.Adapter<HerbAdapter.HerbViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HerbViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return HerbViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HerbViewHolder, position: Int) {
        val herb = herbs[position]
        holder.nameTextView.text = herb.Name
        holder.propertiesTextView.text = herb.Properties
        holder.useTextView.text = "Use: ${herb.Use}"
    }

    override fun getItemCount(): Int = herbs.size

    class HerbViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        var propertiesTextView: TextView = itemView.findViewById(R.id.textViewProperties)
        var useTextView: TextView = itemView.findViewById(R.id.textViewUse)
    }

    fun updateHerbs(newHerbs: List<HerbX>) {
        herbs = newHerbs
        notifyDataSetChanged()
    }
}

