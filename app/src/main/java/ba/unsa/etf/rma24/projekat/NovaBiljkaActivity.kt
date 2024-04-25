package ba.unsa.etf.rma24.projekat

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ba.unsa.etf.rma24.projekat.pomocneKlase.KlimatskiTip
import ba.unsa.etf.rma24.projekat.pomocneKlase.MedicinskaKorist
import ba.unsa.etf.rma24.projekat.pomocneKlase.ProfilOkusaBiljke
import ba.unsa.etf.rma24.projekat.pomocneKlase.Zemljiste
import java.util.*

class NovaBiljkaActivity : AppCompatActivity() {
    private lateinit var nazivET: EditText
    private lateinit var porodicaET: EditText
    private lateinit var medicinskoUpozorenjeET: EditText
    private lateinit var jeloET: EditText
    private lateinit var medicinskaKoristLV: ListView
    private lateinit var klimatskiTipLV: ListView
    private lateinit var zemljisniTipLV: ListView
    private lateinit var profilOkusaLV: ListView
    private lateinit var jelaLV: ListView
    private lateinit var slikaIV: ImageView
    private lateinit var dodajJeloBtn: Button
    private lateinit var dodajBiljkuBtn: Button
    private lateinit var uslikajBiljkuBtn: Button
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nova_biljka)

        nazivET = findViewById(R.id.nazivET)
        porodicaET = findViewById(R.id.porodicaET)
        medicinskoUpozorenjeET = findViewById(R.id.medicinskoUpozorenjeET)
        jeloET = findViewById(R.id.jeloET)
        medicinskaKoristLV = findViewById(R.id.medicinskaKoristLV)
        klimatskiTipLV = findViewById(R.id.klimatskiTipLV)
        zemljisniTipLV = findViewById(R.id.zemljisniTipLV)
        profilOkusaLV = findViewById(R.id.profilOkusaLV)
        jelaLV = findViewById(R.id.jelaLV)
        slikaIV = findViewById(R.id.slikaIV)
        dodajJeloBtn = findViewById(R.id.dodajJeloBtn)
        dodajBiljkuBtn = findViewById(R.id.dodajBiljkuBtn)
        uslikajBiljkuBtn = findViewById(R.id.uslikajBiljkuBtn)

        medicinskaKoristLV.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            EnumSet.allOf(MedicinskaKorist::class.java).map { it.toString() }
        )
        klimatskiTipLV.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            EnumSet.allOf(KlimatskiTip::class.java).map { it.toString() }
        )
        zemljisniTipLV.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            EnumSet.allOf(Zemljiste::class.java).map { it.toString() }
        )
        profilOkusaLV.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_single_choice,
            EnumSet.allOf(ProfilOkusaBiljke::class.java).map { it.toString() }
        )
        jelaLV.adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1
        )

        val adapter = jelaLV.adapter as ArrayAdapter<String>

        jelaLV.setOnItemClickListener { _, _, position, _ ->
            val selektovanoJelo = adapter.getItem(position)
            jeloET.setText(selektovanoJelo)
            dodajJeloBtn.text = "Izmijeni jelo"
            dodajJeloBtn.tag = position
        }

        dodajJeloBtn.setOnClickListener {
            val novoJelo = jeloET.text.toString().trim().lowercase()
            if (novoJelo.isNotEmpty()) {
                val indexPostojecegJela = dodajJeloBtn.tag as? Int
                if (indexPostojecegJela != null) {
                    val postojeceJelo = adapter.getItem(indexPostojecegJela)
                    if (postojeceJelo?.toLowerCase() == novoJelo) {
                        // Ako je novo ime isto kao i staro, samo ažuriraj UI
                        adapter.remove(postojeceJelo)
                        adapter.insert(novoJelo, indexPostojecegJela)
                    } else {
                        // Provjeravamo da li već postoji jelo s istim imenom
                        if (!adapterItemsContain(adapter, novoJelo)) {
                            adapter.remove(postojeceJelo)
                            adapter.insert(novoJelo, indexPostojecegJela)
                        } else {
                            // Obavijestite korisnika da je jelo već dodano
                            Toast.makeText(this, "Jelo već postoji u listi.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Provjeravamo da li već postoji jelo s istim imenom
                    if (!adapterItemsContain(adapter, novoJelo)) {
                        adapter.add(novoJelo)
                    } else {
                        // Obavijestite korisnika da je jelo već dodano
                        Toast.makeText(this, "Jelo već postoji u listi.", Toast.LENGTH_SHORT).show()
                    }
                }
                jeloET.text.clear()
                dodajJeloBtn.text = "Dodaj jelo"
                dodajJeloBtn.tag = null
            } else {
                val selectedItemPosition = jelaLV.selectedItemPosition
                if (selectedItemPosition != ListView.INVALID_POSITION) {
                    adapter.remove(adapter.getItem(selectedItemPosition))
                }
                jeloET.text.clear()
                dodajJeloBtn.text = "Dodaj jelo"
                dodajJeloBtn.tag = null
            }
        }

        dodajBiljkuBtn.setOnClickListener {
            if (validacijaPolja()) {
                val odabranaJela = mutableListOf<String>()
                val adapter = jelaLV.adapter as ArrayAdapter<String>
                for (i in 0 until adapter.count) {
                    if (jelaLV.isItemChecked(i)) {
                        odabranaJela.add(adapter.getItem(i)!!)
                    }
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        uslikajBiljkuBtn.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Provjeravamo da li postoji aplikacija za kameru na uređaju
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } else {
                Toast.makeText(this, "Aplikacija za kameru nije dostupna.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            slikaIV.setImageBitmap(imageBitmap)
        }
    }
    private fun adapterItemsContain(adapter: ArrayAdapter<String>, novoJelo: String): Boolean {
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i)?.lowercase() == novoJelo) {
                return true
            }
        }
        return false
    }

    private fun validacijaPolja(): Boolean {
        val naziv = nazivET.text.toString().trim()
        val porodica = porodicaET.text.toString().trim()
        val medicinskoUpozorenje = medicinskoUpozorenjeET.text.toString().trim()
        val jelo = jeloET.text.toString().trim()
        val odabranaJela = mutableListOf<String>()
        val adapter = jelaLV.adapter as ArrayAdapter<String>
        for (i in 0 until adapter.count) {
            odabranaJela.add(adapter.getItem(i)!!)
        }

        if (naziv.length !in 3..19) {
            nazivET.error = "Naziv mora biti između 3 i 19 znakova"
            return false
        }
        if (porodica.length !in 3..19) {
            porodicaET.error = "Porodica mora biti između 3 i 19 znakova"
            return false
        }
        if (medicinskoUpozorenje.length !in 3..19) {
            medicinskoUpozorenjeET.error = "Medicinsko upozorenje mora biti između 3 i 19 znakova"
            return false
        }
        if (jelo.length !in 3..19) {
            jeloET.error = "Jelo mora biti izmedju 3 i 19 znakova"
            return false
        }

        val lowercaseJela = odabranaJela.map { it.lowercase() }
        val uniqueLowercaseJela = lowercaseJela.toSet()
        if (lowercaseJela.size != uniqueLowercaseJela.size) {
            Toast.makeText(this, "Postoji duplikat", Toast.LENGTH_SHORT).show()
            return false
        }



        if (medicinskaKoristLV.checkedItemCount == 0) {
            Toast.makeText(this, "Nije odabrana nijedna medicinski korisna vrijednost", Toast.LENGTH_SHORT).show()
            return false
        }

        if (klimatskiTipLV.checkedItemCount == 0) {
            Toast.makeText(this, "Nije odabran nijedan klimatski tip", Toast.LENGTH_SHORT).show()
            return false
        }

        if (zemljisniTipLV.checkedItemCount == 0) {
            Toast.makeText(this, "Nije odabran nijedan tip zemljišta", Toast.LENGTH_SHORT).show()
            return false
        }

        val selectedProfilePosition = profilOkusaLV.checkedItemPosition
        if (selectedProfilePosition == ListView.INVALID_POSITION) {
            Toast.makeText(this, "Nije odabran nijedan profil okusa", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
