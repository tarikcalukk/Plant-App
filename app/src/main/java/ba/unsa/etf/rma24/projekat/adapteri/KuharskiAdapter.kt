package ba.unsa.etf.rma24.projekat.adapteri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.rma24.projekat.Biljka
import ba.unsa.etf.rma24.projekat.R

class KuharskiAdapter (
    private var biljke: List<Biljka>,
    private var referentnaBiljka: Biljka? = null
    ) : RecyclerView.Adapter<KuharskiAdapter.KuharskiHolder>() {
    fun updateReferentnaBiljka(biljka: Biljka?) {
        referentnaBiljka = biljka
        filterBiljke()
    }
    private fun filterBiljke() {
        referentnaBiljka?.let { reference ->
            biljke = biljke.filter { biljka ->
                biljka.jela.any { korist ->
                    reference.jela.contains(korist)
                } || biljka.profilOkusa == reference.profilOkusa
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KuharskiHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.kuharski, parent, false)
        return KuharskiHolder(view)
    }

    override fun getItemCount(): Int = biljke.size

    override fun onBindViewHolder(holder: KuharskiHolder, position: Int) {
        val biljka = biljke[position]
        holder.itemView.setOnClickListener {
            referentnaBiljka = biljka
            filterBiljke()
        }
        holder.nazivItem.text = biljka.naziv
        holder.profilOkusaItem.text = biljka.profilOkusa.opis
        holder.jelo1Item.text = ""
        holder.jelo2Item.text = ""
        holder.jelo3Item.text = ""

        for (i in biljka.jela.indices) {
            val jelo = biljka.jela[i]
            when (i) {
                0 -> holder.jelo1Item.text = jelo
                1 -> holder.jelo2Item.text = jelo
                2 -> holder.jelo3Item.text = jelo
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

    inner class KuharskiHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slika: ImageView = itemView.findViewById(R.id.slikaItem)
        val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
        val profilOkusaItem: TextView = itemView.findViewById(R.id.profilOkusaItem)
        val jelo1Item: TextView = itemView.findViewById(R.id.jelo1Item)
        val jelo2Item: TextView = itemView.findViewById(R.id.jelo2Item)
        val jelo3Item: TextView = itemView.findViewById(R.id.jelo3Item)
    }
}