package com.example.aplikacija

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val spinner = findViewById<Spinner>(R.id.modSpinner)
        val options = arrayListOf("Medicinski", "Kuharski", "Botanički")
        val layoutID = android.R.layout.simple_spinner_item
        val myAdapterInstance: ArrayAdapter<String> =
            ArrayAdapter<String>(this, layoutID, options)
        spinner.setAdapter(myAdapterInstance)

    }
}