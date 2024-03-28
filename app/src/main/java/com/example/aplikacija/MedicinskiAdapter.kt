package com.example.aplikacija

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MedicinskiAdapter(
    private var biljke: List<Biljka>
) : RecyclerView.Adapter<MedicinskiAdapter.BiljkeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiljkeViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.medicinski, parent, false)
        return BiljkeViewHolder(view)
    }

    override fun getItemCount(): Int = biljke.size

    override fun onBindViewHolder(holder: BiljkeViewHolder, position: Int) {
        val biljka = biljke[position]

        // Bind data to views
        holder.nazivItem.text = biljka.naziv
        holder.upozorenjeItem.text = biljka.medicinskoUpozorenje
        holder.korist1Item.text = biljka.medicinskeKoristi
        holder.korist2Item.text = biljka.korist2
        holder.korist3Item.text = biljka.korist3
        holder.slika.setImageResource(biljka.eucaliptus)

    }

    fun updateBiljke(biljke: List<Biljka>) {
        this.biljke = biljke
        notifyDataSetChanged()
    }

    inner class BiljkeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slika: ImageView = itemView.findViewById(R.id.slika)
        val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
        val upozorenjeItem: TextView = itemView.findViewById(R.id.upozorenjeItem)
        val korist1Item: TextView = itemView.findViewById(R.id.korist1Item)
        val korist2Item: TextView = itemView.findViewById(R.id.korist2Item)
        val korist3Item: TextView = itemView.findViewById(R.id.korist3Item)
    }
}