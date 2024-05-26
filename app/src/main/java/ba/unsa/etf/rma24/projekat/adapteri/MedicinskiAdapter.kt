package ba.unsa.etf.rma24.projekat.adapteri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.rma24.projekat.Biljka
import ba.unsa.etf.rma24.projekat.R
import ba.unsa.etf.rma24.projekat.Trefle.TrefleDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicinskiAdapter(
    var biljke: List<Biljka>,
    private val filterCallback: (Biljka?) -> Unit
) : RecyclerView.Adapter<MedicinskiAdapter.MedicinskiHolder>() {
    private var clickedBiljka: Biljka? = null
    fun updateBiljke(updatedList: List<Biljka>) {
        biljke = updatedList
        notifyDataSetChanged()
    }
    fun filter(reference: Biljka?) {
        val filteredList = mutableListOf<Biljka>()
        if (reference != null) {
            for (biljka in biljke) {
                if (biljka.medicinskeKoristi.any { korist ->
                        reference.medicinskeKoristi.contains(korist)
                    }) {
                    filteredList.add(biljka)
                }
            }
        } else {
            filteredList.addAll(biljke)
        }
        updateBiljke(filteredList)
    }
    inner class MedicinskiHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slika: ImageView = itemView.findViewById(R.id.slikaItem)
        val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
        val upozorenjeItem: TextView = itemView.findViewById(R.id.upozorenjeItem)
        val korist1Item: TextView = itemView.findViewById(R.id.korist1Item)
        val korist2Item: TextView = itemView.findViewById(R.id.korist2Item)
        val korist3Item: TextView = itemView.findViewById(R.id.korist3Item)
        init {
            itemView.setOnClickListener {
                clickedBiljka = biljke[adapterPosition]
                filterCallback(clickedBiljka)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicinskiHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medicinski, parent, false)
        return MedicinskiHolder(view)
    }
    override fun getItemCount(): Int = biljke.size
    override fun onBindViewHolder(holder: MedicinskiHolder, position: Int) {
        val currentBiljka = biljke[position]
        holder.itemView.setOnClickListener {
            val referenceBiljka = biljke[holder.adapterPosition]
            val filteredList = mutableListOf<Biljka>()
            if (referenceBiljka != null) {
                for (biljka in biljke) {
                    if (biljka.medicinskeKoristi.any { korist ->
                            referenceBiljka.medicinskeKoristi.contains(korist)
                        }) {
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
        holder.upozorenjeItem.text = currentBiljka.medicinskoUpozorenje
        holder.korist1Item.text = currentBiljka.medicinskeKoristi.getOrNull(0)?.opis ?: ""
        holder.korist2Item.text = currentBiljka.medicinskeKoristi.getOrNull(1)?.opis ?: ""
        holder.korist3Item.text = currentBiljka.medicinskeKoristi.getOrNull(2)?.opis ?: ""

        val trefleDAO = TrefleDAO()

        CoroutineScope(Dispatchers.Main).launch {
            val bitmap = trefleDAO.getImage(currentBiljka)
            holder.slika.setImageBitmap(bitmap)
        }
    }
}