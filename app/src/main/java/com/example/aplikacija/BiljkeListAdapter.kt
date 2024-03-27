package com.example.aplikacija

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BiljkeListAdapter(
    private var biljke: List<Biljka>,
    private val onItemClicked: (biljka:Biljka) -> Unit
) : RecyclerView.Adapter<BiljkeListAdapter.BiljkeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiljkeViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.medicinski, parent, false)
        return BiljkeViewHolder(view)
    }

    override fun getItemCount(): Int = biljke.size

    override fun onBindViewHolder(holder: BiljkeViewHolder, position: Int) {
        holder.nazivItem.text = biljke[position].naziv
        holder.porodicaItem.text = biljke[position].porodica
        val imajuLiMedicinskuKorist = imajuLiMedicinskuKorist(biljke[position].medicinskeKoristi, biljke[0].medicinskeKoristi)
        if (imajuLiMedicinskuKorist) {
            holder.itemView.setOnClickListener { onItemClicked(biljke[position]) }
        } else {
            holder.hide()
        }
        //Pronalazimo id drawable elementa na osnovu naziva zanra
        val context: Context = holder.slika.context
        var id: Int = context.resources
            .getIdentifier(biljke[position].naziv.lowercase(), "drawable", context.packageName)
        if (id==0) id=context.resources
            .getIdentifier("eucaliptus", "drawable", context.packageName)
        holder.slika.setImageResource(id)
    }

    fun updateBiljke(biljke: List<Biljka>): List<Biljka> {
        this.biljke = biljke
        notifyDataSetChanged()
        return biljke
    }

    inner class BiljkeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slika: ImageView = itemView.findViewById(R.id.slika)
        val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
        val porodicaItem: TextView = itemView.findViewById(R.id.porodicaItem)
        val korist1Item: TextView = itemView.findViewById(R.id.korist1Item)
        val korist2Item: TextView = itemView.findViewById(R.id.jelo2Item)
        val korist3Item: TextView = itemView.findViewById(R.id.jelo3Item)
        fun hide() {
            slika.visibility = View.GONE
            nazivItem.visibility = View.GONE
            porodicaItem.visibility = View.GONE
            korist1Item.visibility = View.GONE
            korist2Item.visibility = View.GONE
            korist3Item.visibility = View.GONE
        }
    }

    fun imajuLiMedicinskuKorist(koristi1: List<MedicinskaKorist>, koristi2: List<MedicinskaKorist>): Boolean {
        for (korist1 in koristi1) {
            for (korist2 in koristi2) {
                if (korist1 == korist2) {
                    return true
                }
            }
        }
        return false
    }
}