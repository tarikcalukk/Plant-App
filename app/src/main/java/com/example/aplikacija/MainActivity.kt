package com.example.aplikacija

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikacija.adapteri.MedicinskiAdapter
import com.example.aplikacija.pomocneKlase.biljke


class MainActivity : AppCompatActivity() {
    private lateinit var spinner: Spinner
    private lateinit var medicinski: RecyclerView
    private lateinit var medicinskiAdapter: MedicinskiAdapter
    private var listaBiljaka = biljke

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.modSpinner)
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
            }
        }
    }
    private fun showMedicinskiMod() {
        medicinski = findViewById(R.id.biljkeRV)
        medicinski.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        medicinskiAdapter = MedicinskiAdapter(listOf())
        medicinski.adapter = medicinskiAdapter
        medicinskiAdapter.updateBiljke(listaBiljaka)
    }



    private fun showKuharskiMod() {
    }

    private fun showBotanickiMod() {
    }
}