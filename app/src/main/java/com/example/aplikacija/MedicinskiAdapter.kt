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

        holder.itemView.setOnClickListener {
            val referenceBiljka = biljke[position]

            // Filter biljke based on similar medicinal usage
            val filteredBiljke = biljke.filter { otherBiljka ->
                otherBiljka.medicinskeKoristi.any { korist ->
                    referenceBiljka.medicinskeKoristi.contains(korist)
                }
            }

            biljke = filteredBiljke
            notifyDataSetChanged()
        }

        holder.nazivItem.text = biljka.naziv
        holder.upozorenjeItem.text = biljka.medicinskoUpozorenje
        val resourceId = holder.itemView.context.resources.getIdentifier(
            "eucaliptus", "drawable", holder.itemView.context.packageName
        )
        holder.slika.setImageResource(resourceId)



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
    fun getSelectedBiljka(position: Int): Biljka {
        return biljke[position]
    }
}