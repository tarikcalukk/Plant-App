package ba.unsa.etf.rma24.projekat.adapteri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.rma24.projekat.Biljka
import ba.unsa.etf.rma24.projekat.R

class MedicinskiAdapter(
    private var biljke: List<Biljka>,
    private var referentnaBiljka: Biljka? = null
    ) : RecyclerView.Adapter<MedicinskiAdapter.medicinskiHolder>() {
    fun updateReferentnaBiljka(referentnaBiljka: Biljka?) {
        this.referentnaBiljka = referentnaBiljka
        filterBiljke()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): medicinskiHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.medicinski, parent, false)
        return medicinskiHolder(view)
    }

    override fun getItemCount(): Int = biljke.size

    override fun onBindViewHolder(holder: medicinskiHolder, position: Int) {
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
        holder.korist1Item.text = biljka.medicinskeKoristi.getOrNull(0)?.opis ?: ""
        holder.korist2Item.text = biljka.medicinskeKoristi.getOrNull(1)?.opis ?: ""
        holder.korist3Item.text = biljka.medicinskeKoristi.getOrNull(2)?.opis ?: ""

        val resourceId = holder.itemView.context.resources.getIdentifier(
            "eucaliptus", "drawable", holder.itemView.context.packageName
        )
        holder.slika.setImageResource(resourceId)
    }

    fun updateBiljke(biljke: List<Biljka>) {
        this.biljke = biljke
        notifyDataSetChanged()
    }

    inner class medicinskiHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slika: ImageView = itemView.findViewById(R.id.slikaItem)
        val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
        val upozorenjeItem: TextView = itemView.findViewById(R.id.upozorenjeItem)
        val korist1Item: TextView = itemView.findViewById(R.id.korist1Item)
        val korist2Item: TextView = itemView.findViewById(R.id.korist2Item)
        val korist3Item: TextView = itemView.findViewById(R.id.korist3Item)
    }

    private fun filterBiljke(): List<Biljka> {
        return if (referentnaBiljka != null) {
            biljke.filter { biljka ->
                biljka.medicinskeKoristi.any { korist ->
                    referentnaBiljka?.medicinskeKoristi?.contains(korist) ?: false
                }
            }
        } else {
            biljke
        }
    }
}