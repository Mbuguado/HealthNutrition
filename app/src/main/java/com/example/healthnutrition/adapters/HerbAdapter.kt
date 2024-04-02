// HerbAdapter.kt
package com.example.healthnutrition.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthnutrition.R
import com.example.healthnutrition.model.HerbX
import com.squareup.picasso.Picasso

class HerbAdapter(
    private var herbs: List<HerbX>,
    private val onPayClicked: (HerbX) ->Unit
) : RecyclerView.Adapter<HerbAdapter.HerbViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HerbViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return HerbViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HerbViewHolder, position: Int) {
        val herb = herbs[position]
        holder.nameTextView.text = herb.Name
        holder.propertiesTextView.text = herb.Properties
        Picasso.get().load(herb.Image).into(holder.imageView)

        holder.payButton.setOnClickListener {
            onPayClicked(herb)
        }
    }

    override fun getItemCount(): Int = herbs.size

    class HerbViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        var propertiesTextView: TextView = itemView.findViewById(R.id.textViewProperties)
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
        var payButton: Button = itemView.findViewById(R.id.payButton)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateHerbs(newHerbs: List<HerbX>) {
        herbs = newHerbs
        notifyDataSetChanged()
    }
}

