package ba.unsa.etf.rma24.projekat.adapteri

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.rma24.projekat.Biljka
import ba.unsa.etf.rma24.projekat.R
import ba.unsa.etf.rma24.projekat.TrefleDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KuharskiAdapter(
    var biljke: List<Biljka>,
    private val filterCallback: (Biljka?) -> Unit
) : RecyclerView.Adapter<KuharskiAdapter.KuharskiHolder>() {

    private var clickedBiljka: Biljka? = null

    fun updateBiljke(updatedList: List<Biljka>) {
        biljke = updatedList
        notifyDataSetChanged()
    }

    fun filter(reference: Biljka?) {
        val filteredList = mutableListOf<Biljka>()
        if (reference != null) {
            for (biljka in biljke) {
                if (biljka.jela.any { jelo ->
                        reference.jela.contains(jelo)
                    } || biljka.profilOkusa == reference.profilOkusa) {
                    filteredList.add(biljka)
                }
            }
        } else {
            filteredList.addAll(biljke)
        }
        updateBiljke(filteredList)
    }

    inner class KuharskiHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slika: ImageView = itemView.findViewById(R.id.slikaItem)
        val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
        val profilOkusaItem: TextView = itemView.findViewById(R.id.profilOkusaItem)
        val jelo1Item: TextView = itemView.findViewById(R.id.jelo1Item)
        val jelo2Item: TextView = itemView.findViewById(R.id.jelo2Item)
        val jelo3Item: TextView = itemView.findViewById(R.id.jelo3Item)
        init {
            itemView.setOnClickListener {
                clickedBiljka = biljke[adapterPosition]
                filterCallback(clickedBiljka)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KuharskiHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.kuharski, parent, false)
        return KuharskiHolder(view)
    }
    override fun getItemCount(): Int = biljke.size

    override fun onBindViewHolder(holder: KuharskiHolder, position: Int) {
        val currentBiljka = biljke[position]
        holder.itemView.setOnClickListener {
            val referenceBiljka = biljke[position]
            val filteredList = mutableListOf<Biljka>()
            if (referenceBiljka != null) {
                for (biljka in biljke) {
                    if (biljka.jela.any { jelo ->
                            referenceBiljka.jela.contains(jelo)
                        } || biljka.profilOkusa == referenceBiljka.profilOkusa) {
                        filteredList.add(biljka)
                    }
                }
            } else {
                filteredList.addAll(biljke)
            }
            updateBiljke(filteredList)
            filterCallback(referenceBiljka)
        }
        holder.nazivItem.text = currentBiljka.naziv
        holder.profilOkusaItem.text = currentBiljka.profilOkusa?.opis
        holder.jelo1Item.text = ""
        holder.jelo2Item.text = ""
        holder.jelo3Item.text = ""

        for (i in currentBiljka.jela.indices) {
            val jelo = currentBiljka.jela[i]
            when (i) {
                0 -> holder.jelo1Item.text = jelo
                1 -> holder.jelo2Item.text = jelo
                2 -> holder.jelo3Item.text = jelo
            }
        }
        val trefleDAO = TrefleDAO()

        CoroutineScope(Dispatchers.Main).launch {
            val bitmap = trefleDAO.getImage(currentBiljka)
            holder.slika.setImageBitmap(bitmap)
        }
    }
}