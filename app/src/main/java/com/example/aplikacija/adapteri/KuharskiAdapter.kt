package com.example.aplikacija.adapteri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikacija.Biljka
import com.example.aplikacija.R

class KuharskiAdapter (
    private var biljke: List<Biljka>
    ) : RecyclerView.Adapter<KuharskiAdapter.kuharskiHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KuharskiAdapter.kuharskiHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.kuharski, parent, false)
        return kuharskiHolder(view)
    }

    override fun getItemCount(): Int = biljke.size

    override fun onBindViewHolder(holder: KuharskiAdapter.kuharskiHolder, position: Int) {
        val biljka = biljke[position]
        holder.itemView.setOnClickListener {
            val referenceBiljka = biljke[position]

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
        holder.jelo1Item.text = ""
        holder.jelo2Item.text = ""
        holder.jelo3Item.text = ""
        for (i in biljka.medicinskeKoristi.indices) {
            val korist = biljka.medicinskeKoristi[i]
            when (i) {
                0 -> holder.jelo1Item.text = korist.toString()
                1 -> holder.jelo2Item.text = korist.toString()
                2 -> holder.jelo3Item.text = korist.toString()
            }
        }
        val resourceId = holder.itemView.context.resources.getIdentifier(
            "eucaliptus", "drawable", holder.itemView.context.packageName
        )
        holder.slika.setImageResource(resourceId)
    }

    fun updateBiljke(biljke: List<Biljka>) {
        this.biljke = biljke
        notifyDataSetChanged()
    }

    inner class kuharskiHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slika: ImageView = itemView.findViewById(R.id.slika)
        val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
        val upozorenjeItem: TextView = itemView.findViewById(R.id.profilOkusaItem)
        val jelo1Item: TextView = itemView.findViewById(R.id.jelo1Item)
        val jelo2Item: TextView = itemView.findViewById(R.id.jelo2Item)
        val jelo3Item: TextView = itemView.findViewById(R.id.jelo3Item)
    }
}