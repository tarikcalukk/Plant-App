package ba.unsa.etf.rma24.projekat

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.unsa.etf.rma24.projekat.adapteri.BotanickiAdapter
import ba.unsa.etf.rma24.projekat.adapteri.KuharskiAdapter
import ba.unsa.etf.rma24.projekat.adapteri.MedicinskiAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var spinner: Spinner
    private lateinit var button: Button
    private lateinit var biljke: RecyclerView
    private lateinit var botanickiAdapter: BotanickiAdapter
    private lateinit var medicinskiAdapter: MedicinskiAdapter
    private lateinit var kuharskiAdapter: KuharskiAdapter
    private var trenutniMod: String = "Medicinski"
    private var listaBiljaka = ba.unsa.etf.rma24.projekat.pomocneKlase.biljke
    private var filtriraneBiljke = ba.unsa.etf.rma24.projekat.pomocneKlase.biljke

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        biljke = findViewById(R.id.biljkeRV)
        spinner = findViewById(R.id.modSpinner)
        button = findViewById(R.id.resetBtn)
        val arraySpinner = listOf(
            "Medicinski",
            "Kuharski",
            "Botanicki"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arraySpinner)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(adapter)

        medicinskiAdapter = MedicinskiAdapter(listOf()) { biljka -> medicinskiAdapter.filter(biljka) }
        kuharskiAdapter = KuharskiAdapter(listOf()) { biljka -> kuharskiAdapter.filter(biljka) }
        botanickiAdapter = BotanickiAdapter(listOf()) { biljka -> botanickiAdapter.filter(biljka) }

        biljke.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        biljke.adapter = medicinskiAdapter
        medicinskiAdapter.updateBiljke(listaBiljaka)

        button.setOnClickListener {
            filtriraneBiljke = ba.unsa.etf.rma24.projekat.pomocneKlase.biljke
            if (trenutniMod == "Medicinski") {
                biljke.adapter =
                    MedicinskiAdapter(filtriraneBiljke) { biljka -> medicinskiAdapter.filter(biljka) }
                medicinskiAdapter.updateBiljke(filtriraneBiljke)
                medicinskiAdapter.notifyDataSetChanged()
            } else if (trenutniMod == "Botanicki") {
                biljke.adapter =
                    BotanickiAdapter(filtriraneBiljke) { biljka -> botanickiAdapter.filter(biljka) }
                botanickiAdapter.updateBiljke(filtriraneBiljke)
                botanickiAdapter.notifyDataSetChanged()
            } else {
                biljke.adapter =
                    KuharskiAdapter(filtriraneBiljke) { biljka -> kuharskiAdapter.filter(biljka) }
                kuharskiAdapter.updateBiljke(filtriraneBiljke)
                kuharskiAdapter.notifyDataSetChanged()
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (spinner.selectedItem.toString() == "Medicinski") {
                    if (trenutniMod == "Botanicki") filtriraneBiljke = botanickiAdapter.biljke
                    if (trenutniMod == "Kuharski") filtriraneBiljke = kuharskiAdapter.biljke
                    biljke.adapter =
                        MedicinskiAdapter(filtriraneBiljke) { biljka -> medicinskiAdapter.filter(biljka) }
                    medicinskiAdapter.updateBiljke(filtriraneBiljke)
                    medicinskiAdapter.notifyDataSetChanged()
                    trenutniMod = "Medicinski"
                } else if (spinner.selectedItem.toString() == "Kuharski") {
                    if (trenutniMod == "Medicinski") filtriraneBiljke = medicinskiAdapter.biljke
                    if (trenutniMod == "Botanicki") filtriraneBiljke = botanickiAdapter.biljke
                    biljke.adapter =
                        KuharskiAdapter(filtriraneBiljke) { biljka -> kuharskiAdapter.filter(biljka) }
                    kuharskiAdapter.updateBiljke(filtriraneBiljke)
                    kuharskiAdapter.notifyDataSetChanged()
                    trenutniMod = "Kuharski"
                } else {
                    if (trenutniMod == "Medicinski") filtriraneBiljke = medicinskiAdapter.biljke
                    if (trenutniMod == "Kuharski") filtriraneBiljke = kuharskiAdapter.biljke
                    biljke.adapter =
                        BotanickiAdapter(filtriraneBiljke) { biljka -> botanickiAdapter.filter(biljka) }
                    botanickiAdapter.updateBiljke(filtriraneBiljke)
                    botanickiAdapter.notifyDataSetChanged()
                    trenutniMod = "Botanicki"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}