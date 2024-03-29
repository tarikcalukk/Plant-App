package com.example.aplikacija.adapteri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikacija.Biljka
import com.example.aplikacija.R

class BotanickiAdapter (
    private var biljke: List<Biljka>,
    private var referentnaBiljka: Biljka? = null
    ) : RecyclerView.Adapter<BotanickiAdapter.BotanickiHolder>()
    {
        fun updateReferentnaBiljka(biljka: Biljka?) {
            referentnaBiljka = biljka
            filterBiljke()
        }

        private fun filterBiljke() {
            val filteredList = mutableListOf<Biljka>()

            referentnaBiljka?.let { reference ->
                val zajednickiKlimatskiTipovi = reference.klimatskiTipovi.toSet()
                val zajednickiZemljisniTipovi = reference.zemljisniTipovi.toSet()

                for (biljka in biljke) {
                    if (biljka.porodica == reference.porodica &&
                        biljka.klimatskiTipovi.intersect(zajednickiKlimatskiTipovi).isNotEmpty() &&
                        biljka.zemljisniTipovi.intersect(zajednickiZemljisniTipovi).isNotEmpty()) {
                        filteredList.add(biljka)
                    }
                }
            }

            biljke = filteredList
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BotanickiAdapter.BotanickiHolder {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.botanicki, parent, false)
            return BotanickiHolder(view)
        }
        override fun getItemCount(): Int = biljke.size

        override fun onBindViewHolder(holder: BotanickiAdapter.BotanickiHolder, position: Int) {
            val biljka = biljke[position]
            holder.itemView.setOnClickListener {
                referentnaBiljka = biljka
                filterBiljke()
            }
            holder.nazivItem.text = biljka.naziv
            holder.porodicaItem.text = biljka.porodica
            holder.klimatskiTipItem.text = biljka.klimatskiTipovi[0].toString()
            holder.zemljisniTipItem.text = biljka.zemljisniTipovi[0].toString()

            val resourceId = holder.itemView.context.resources.getIdentifier(
                "eucaliptus", "drawable", holder.itemView.context.packageName
            )
            holder.slika.setImageResource(resourceId)
        }


        fun updateBiljke(biljke: List<Biljka>) {
            this.biljke = biljke
            notifyDataSetChanged()
        }

        inner class BotanickiHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val slika: ImageView = itemView.findViewById(R.id.slika)
            val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
            val porodicaItem: TextView = itemView.findViewById(R.id.porodicaItem)
            val klimatskiTipItem: TextView = itemView.findViewById(R.id.klimatskiTipItem)
            val zemljisniTipItem: TextView = itemView.findViewById(R.id.zemljisniTipItem)
        }
    }