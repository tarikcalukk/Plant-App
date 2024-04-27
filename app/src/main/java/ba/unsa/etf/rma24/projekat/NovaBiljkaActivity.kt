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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ba.unsa.etf.rma24.projekat.pomocneKlase.BiljkaSingleton
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
    private val request = 1

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
                    if (postojeceJelo?.lowercase() == novoJelo) {
                        adapter.remove(postojeceJelo)
                        adapter.insert(novoJelo, indexPostojecegJela)
                    } else {
                        if (!adapterItemsContain(adapter, novoJelo)) {
                            adapter.remove(postojeceJelo)
                            adapter.insert(novoJelo, indexPostojecegJela)
                        } else {
                            Toast.makeText(this, "Jelo već postoji u listi.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    if (!adapterItemsContain(adapter, novoJelo)) {
                        adapter.add(novoJelo)
                    } else {
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
                val naziv = nazivET.text.toString().trim()
                val porodica = porodicaET.text.toString().trim()
                val medicinskoUpozorenje = medicinskoUpozorenjeET.text.toString().trim()
                val odabranaJela = mutableListOf<String>()
                val adapter = jelaLV.adapter as ArrayAdapter<String>
                for (i in 0 until adapter.count) {
                    odabranaJela.add(adapter.getItem(i)!!)
                }
                val odabraniProfilOkusa = ProfilOkusaBiljke.valueOf(profilOkusaLV.getItemAtPosition(profilOkusaLV.checkedItemPosition).toString())
                val odabraniKlimatskiTipovi = mutableListOf<KlimatskiTip>()
                for (i in 0 until klimatskiTipLV.count) {
                    if (klimatskiTipLV.isItemChecked(i)) {
                        val klimatskiTip = KlimatskiTip.entries[i]
                        odabraniKlimatskiTipovi.add(klimatskiTip)
                    }
                }

                val odabraniZemljisniTipovi = mutableListOf<Zemljiste>()
                for (i in 0 until zemljisniTipLV.count) {
                    if (zemljisniTipLV.isItemChecked(i)) {
                        odabraniZemljisniTipovi.add(Zemljiste.valueOf(zemljisniTipLV.getItemAtPosition(i).toString()))
                    }
                }

                val odabraneMedicinskeKoristi = mutableListOf<MedicinskaKorist>()
                val checkedPositions = medicinskaKoristLV.checkedItemPositions
                for (i in 0 until medicinskaKoristLV.count) {
                    if (checkedPositions.get(i)) {
                        odabraneMedicinskeKoristi.add(MedicinskaKorist.valueOf(medicinskaKoristLV.getItemAtPosition(i).toString()))
                    }
                }
                val novaBiljka = Biljka(
                    naziv,
                    porodica,
                    medicinskoUpozorenje,
                    odabraneMedicinskeKoristi,
                    odabranaJela,
                    odabraniProfilOkusa,
                    odabraniKlimatskiTipovi,
                    odabraniZemljisniTipovi
                )
                BiljkaSingleton.listaBiljaka.add(novaBiljka)

                val intent = Intent(this, MainActivity::class.java)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        uslikajBiljkuBtn.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                takePictureLauncher.launch(takePictureIntent) // Korištenje ActivityResultLauncher-a za pokretanje aktivnosti
            } else {
                Toast.makeText(this, "Aplikacija za kameru nije dostupna.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == request && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            slikaIV.setImageBitmap(imageBitmap)
        }
    }
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
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

        if (naziv.length !in 2..20) {
            nazivET.error = "Naziv mora biti između 2 i 20 znakova"
            return false
        }
        if (porodica.length !in 2..20) {
            porodicaET.error = "Porodica mora biti između 2 i 20 znakova"
            return false
        }
        if (medicinskoUpozorenje.length !in 2..20) {
            medicinskoUpozorenjeET.error = "Medicinsko upozorenje mora biti između 2 i 20 znakova"
            return false
        }
        if (jelo.length !in 2..20) {
            jeloET.error = "Jelo mora biti izmedju 2 i 20 znakova"
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