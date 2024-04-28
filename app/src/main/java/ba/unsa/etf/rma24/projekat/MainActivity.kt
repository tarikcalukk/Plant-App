package ba.unsa.etf.rma24.projekat

import android.content.Intent
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
import ba.unsa.etf.rma24.projekat.pomocneKlase.BiljkaSingleton

class MainActivity : AppCompatActivity() {
    private lateinit var spinner: Spinner
    private lateinit var button: Button
    private lateinit var biljka: RecyclerView
    private lateinit var novaBiljkaBtn : Button
    private lateinit var botanickiAdapter: BotanickiAdapter
    private lateinit var medicinskiAdapter: MedicinskiAdapter
    private lateinit var kuharskiAdapter: KuharskiAdapter
    private var trenutniMod: String = "Medicinski"
    private var filtriraneBiljke = BiljkaSingleton.filtriraneBiljke
    private var listaBiljaka = BiljkaSingleton.listaBiljaka

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        biljka = findViewById(R.id.biljkeRV)
        spinner = findViewById(R.id.modSpinner)
        button = findViewById(R.id.resetBtn)
        novaBiljkaBtn = findViewById(R.id.novaBiljkaBtn)
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

        biljka.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        biljka.adapter = medicinskiAdapter
        medicinskiAdapter.updateBiljke(listaBiljaka)

        button.setOnClickListener {
            filtriraneBiljke = BiljkaSingleton.listaBiljaka
            when (trenutniMod) {
                "Medicinski" -> {
                    biljka.adapter =
                        MedicinskiAdapter(filtriraneBiljke) { biljka -> medicinskiAdapter.filter(biljka) }
                    medicinskiAdapter.updateBiljke(filtriraneBiljke)
                    medicinskiAdapter.notifyDataSetChanged()
                }
                "Botanicki" -> {
                    biljka.adapter =
                        BotanickiAdapter(filtriraneBiljke) { biljka -> botanickiAdapter.filter(biljka) }
                    botanickiAdapter.updateBiljke(filtriraneBiljke)
                    botanickiAdapter.notifyDataSetChanged()
                }
                else -> {
                    biljka.adapter =
                        KuharskiAdapter(filtriraneBiljke) { biljka -> kuharskiAdapter.filter(biljka) }
                    kuharskiAdapter.updateBiljke(filtriraneBiljke)
                    kuharskiAdapter.notifyDataSetChanged()
                }
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
                    biljka.adapter =
                        MedicinskiAdapter(filtriraneBiljke) { biljka -> medicinskiAdapter.filter(biljka) }
                    medicinskiAdapter.updateBiljke(filtriraneBiljke)
                    medicinskiAdapter.notifyDataSetChanged()
                    trenutniMod = "Medicinski"
                } else if (spinner.selectedItem.toString() == "Kuharski") {
                    if (trenutniMod == "Medicinski") filtriraneBiljke = medicinskiAdapter.biljke
                    if (trenutniMod == "Botanicki") filtriraneBiljke = botanickiAdapter.biljke
                    biljka.adapter =
                        KuharskiAdapter(filtriraneBiljke) { biljka -> kuharskiAdapter.filter(biljka) }
                    kuharskiAdapter.updateBiljke(filtriraneBiljke)
                    kuharskiAdapter.notifyDataSetChanged()
                    trenutniMod = "Kuharski"
                } else {
                    if (trenutniMod == "Medicinski") filtriraneBiljke = medicinskiAdapter.biljke
                    if (trenutniMod == "Kuharski") filtriraneBiljke = kuharskiAdapter.biljke
                    biljka.adapter =
                        BotanickiAdapter(filtriraneBiljke) { biljka -> botanickiAdapter.filter(biljka) }
                    botanickiAdapter.updateBiljke(filtriraneBiljke)
                    botanickiAdapter.notifyDataSetChanged()
                    trenutniMod = "Botanicki"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        novaBiljkaBtn.setOnClickListener {
            val intent = Intent(this, NovaBiljkaActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        spinner.setSelection(0)
        if (trenutniMod == "Medicinski") {
            updateMedicinskiRecyclerView()
        } else {
            listaBiljaka = BiljkaSingleton.listaBiljaka
            spinner.setSelection(0)
            updateMedicinskiRecyclerView()
        }
    }
    private fun updateMedicinskiRecyclerView() {
        biljka.adapter = MedicinskiAdapter(listaBiljaka) { biljka -> medicinskiAdapter.filter(biljka) }
        medicinskiAdapter.updateBiljke(listaBiljaka)
        medicinskiAdapter.notifyDataSetChanged()
    }
}