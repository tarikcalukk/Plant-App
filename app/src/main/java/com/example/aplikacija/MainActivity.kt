package com.example.aplikacija

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private lateinit var medicinski: RecyclerView
    private lateinit var biljkeAdapter: BiljkeListAdapter
    private var listaBiljaka = biljke

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.biljkeRV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        biljkeAdapter = BiljkeListAdapter(emptyList()) { clickedBiljka ->
            Toast.makeText(this, "Kliknuta biljka: ${clickedBiljka.naziv}", Toast.LENGTH_SHORT)
                .show()
        }

        recyclerView.adapter = biljkeAdapter
        val spinner = findViewById<Spinner>(R.id.modSpinner)
        val options = arrayListOf("Medicinski", "Kuharski", "Botanički")
        val layoutID = android.R.layout.simple_spinner_item
        val myAdapterInstance: ArrayAdapter<String> =
            ArrayAdapter<String>(this, layoutID, options)
        spinner.setAdapter(myAdapterInstance)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> showMedicinskiMod()
                    1 -> showKuharskiMod()
                    2 -> showBotanickiMod()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Implementirati šta se dešava kada nije odabrana ni jedna stavka
            }
        }
    }
    private fun showMedicinskiMod() {

    }


    private fun showKuharskiMod() {

    }

    private fun showBotanickiMod() {
    }
}