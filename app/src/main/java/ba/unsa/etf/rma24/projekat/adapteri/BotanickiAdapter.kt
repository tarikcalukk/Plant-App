package ba.unsa.etf.rma24.projekat.adapteri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.rma24.projekat.Biljka
import ba.unsa.etf.rma24.projekat.R

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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BotanickiHolder {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.botanicki, parent, false)
            return BotanickiHolder(view)
        }
        override fun getItemCount(): Int = biljke.size

        override fun onBindViewHolder(holder: BotanickiHolder, position: Int) {
            val biljka = biljke[position]
            holder.itemView.setOnClickListener {
                referentnaBiljka = biljka
                filterBiljke()
            }
            holder.nazivItem.text = biljka.naziv
            holder.porodicaItem.text = biljka.porodica
            holder.klimatskiTipItem.text = biljka.klimatskiTipovi[0].opis
            holder.zemljisniTipItem.text = biljka.zemljisniTipovi[0].naziv

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
            val slika: ImageView = itemView.findViewById(R.id.slikaItem)
            val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
            val porodicaItem: TextView = itemView.findViewById(R.id.porodicaItem)
            val klimatskiTipItem: TextView = itemView.findViewById(R.id.klimatskiTipItem)
            val zemljisniTipItem: TextView = itemView.findViewById(R.id.zemljisniTipItem)
        }
    }