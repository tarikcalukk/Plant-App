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

        jelaLV.setOnItemClickListener { parent, _, position, _ ->
            jeloET.setText(parent.getItemAtPosition(position).toString())
        }

        dodajJeloBtn.setOnClickListener {
            val novoJelo = jeloET.text.toString().trim()
            if (novoJelo.isNotEmpty()) {
                val adapter = jelaLV.adapter as ArrayAdapter<String>
                val indexPostojecegJela = adapter.getPosition(novoJelo)
                if (indexPostojecegJela != -1) {
                    jeloET.setText(novoJelo)
                    dodajJeloBtn.text = "Izmijeni jelo"
                    jelaLV.setItemChecked(indexPostojecegJela, true)
                } else {
                    adapter.add(novoJelo)
                    jeloET.text.clear()
                }
            } else {
                val adapter = jelaLV.adapter as ArrayAdapter<String>
                val selektovanoJelo = jelaLV.checkedItemPosition
                if (selektovanoJelo != ListView.INVALID_POSITION) {
                    adapter.remove(adapter.getItem(selektovanoJelo))
                }
                jeloET.text.clear()
                dodajJeloBtn.text = "Dodaj jelo"
            }
        }

        dodajBiljkuBtn.setOnClickListener {
            val naziv = nazivET.text.toString().trim()
            val porodica = porodicaET.text.toString().trim()
            val medicinskoUpozorenje = medicinskoUpozorenjeET.text.toString().trim()
            val odabranaJela = mutableListOf<String>()
            val adapter = jelaLV.adapter as ArrayAdapter<String>
            for (i in 0 until adapter.count) {
                if (jelaLV.isItemChecked(i)) {
                    odabranaJela.add(adapter.getItem(i)!!)
                }
            }

            if (naziv.isNotEmpty() && porodica.isNotEmpty() && medicinskoUpozorenje.isNotEmpty() && odabranaJela.isNotEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Molimo popunite sva obavezna polja.", Toast.LENGTH_SHORT).show()
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
}
