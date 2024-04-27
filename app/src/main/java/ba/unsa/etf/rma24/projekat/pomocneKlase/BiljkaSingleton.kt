package ba.unsa.etf.rma24.projekat.pomocneKlase

import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import ba.unsa.etf.rma24.projekat.Biljka

object BiljkaSingleton {
    var listaBiljaka: MutableList<Biljka> = biljke.toMutableList()
    var filtriraneBiljke: List<Biljka> = biljke
}
/*
private val novaBiljkaLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
        trenutniMod = "Medicinski" // Postavljanje trenutnog moda na "Medicinski"
        medicinskiAdapter.updateBiljke(BiljkaSingleton.listaBiljaka)
        medicinskiAdapter.notifyDataSetChanged()
        botanickiAdapter.updateBiljke(BiljkaSingleton.listaBiljaka)
        botanickiAdapter.notifyDataSetChanged()
        kuharskiAdapter.updateBiljke(BiljkaSingleton.listaBiljaka)
        kuharskiAdapter.notifyDataSetChanged()
    }
}*/