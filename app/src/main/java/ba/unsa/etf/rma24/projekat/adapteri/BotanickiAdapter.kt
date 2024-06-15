package ba.unsa.etf.rma24.projekat.adapteri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.rma24.projekat.Biljka
import ba.unsa.etf.rma24.projekat.BiljkaDao
import ba.unsa.etf.rma24.projekat.R
import ba.unsa.etf.rma24.projekat.pomocneKlase.BiljkaDatabase
import ba.unsa.etf.rma24.projekat.trefle.TrefleDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BotanickiAdapter(
    var biljke: List<Biljka>,
    private val filterCallback: (Biljka?) -> Unit
) : RecyclerView.Adapter<BotanickiAdapter.BotanickiHolder>() {
    private var clickedBiljka: Biljka? = null
    private lateinit var biljkaDao: BiljkaDao
fun updateBiljke(updatedList: List<Biljka>) {
        biljke = updatedList
        notifyDataSetChanged()
    }
    fun filter(reference: Biljka?) {
        val filteredList = mutableListOf<Biljka>()
        if (reference != null) {
            val zajednickiKlimatskiTipovi = reference.klimatskiTipovi.toSet()
            val zajednickiZemljisniTipovi = reference.zemljisniTipovi.toSet()
            for (biljka in biljke) {
                if (biljka.porodica == reference.porodica &&
                    biljka.klimatskiTipovi.intersect(zajednickiKlimatskiTipovi).isNotEmpty() &&
                    biljka.zemljisniTipovi.intersect(zajednickiZemljisniTipovi).isNotEmpty()) {
                    filteredList.add(biljka)
                }
            }
        } else {
            filteredList.addAll(biljke)
        }
        updateBiljke(filteredList)
    }
    inner class BotanickiHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slika: ImageView = itemView.findViewById(R.id.slikaItem)
        val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
        val porodicaItem: TextView = itemView.findViewById(R.id.porodicaItem)
        val klimatskiTipItem: TextView = itemView.findViewById(R.id.klimatskiTipItem)
        val zemljisniTipItem: TextView = itemView.findViewById(R.id.zemljisniTipItem)
        init {
            itemView.setOnClickListener {
                clickedBiljka = biljke[adapterPosition]
                filterCallback(clickedBiljka)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BotanickiHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.botanicki, parent, false)
        return BotanickiHolder(view)
    }
    override fun getItemCount(): Int = biljke.size
    override fun onBindViewHolder(holder: BotanickiHolder, position: Int) {
        val currentBiljka = biljke[position]
        holder.itemView.setOnClickListener {
            val referenceBiljka = biljke[position]
            val filteredList = mutableListOf<Biljka>()
            if (referenceBiljka != null) {
                val zajednickiKlimatskiTipovi = referenceBiljka.klimatskiTipovi.toSet()
                val zajednickiZemljisniTipovi = referenceBiljka.zemljisniTipovi.toSet()
                for (biljka in biljke) {
                    if (biljka.porodica == referenceBiljka.porodica &&
                        biljka.klimatskiTipovi.intersect(zajednickiKlimatskiTipovi).isNotEmpty() &&
                        biljka.zemljisniTipovi.intersect(zajednickiZemljisniTipovi).isNotEmpty()) {
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
        holder.porodicaItem.text = currentBiljka.porodica
        if (currentBiljka.klimatskiTipovi.isNotEmpty()) {
            holder.klimatskiTipItem.text = currentBiljka.klimatskiTipovi[0].opis
        } else {
            holder.klimatskiTipItem.text = ""
        }
        if(currentBiljka.zemljisniTipovi.isNotEmpty()) {
            holder.zemljisniTipItem.text = currentBiljka.zemljisniTipovi[0].naziv
        } else {
            holder.zemljisniTipItem.text = ""
        }

        val context = holder.itemView.context
        val trefleDAO = TrefleDAO(context)
        if (!::biljkaDao.isInitialized) {
            biljkaDao = BiljkaDatabase.getInstance(context).biljkaDao()
        }

        CoroutineScope(Dispatchers.Main).launch {
            val image = biljkaDao.getImageByIdBiljke(currentBiljka.id ?: 0L)
            if (image != null) {
                holder.slika.setImageBitmap(image.bitmap)
            } else {
                val bitmap = trefleDAO.getImage(currentBiljka)
                biljkaDao.addImage(currentBiljka.id ?: 0L, bitmap)
                holder.slika.setImageBitmap(bitmap)
            }
        }
    }
}