package com.example.aplikacija

import com.example.aplikacija.pomocneKlase.KlimatskiTip
import com.example.aplikacija.pomocneKlase.MedicinskaKorist
import com.example.aplikacija.pomocneKlase.ProfilOkusaBiljke
import com.example.aplikacija.pomocneKlase.Zemljiste

data class Biljka(
    val naziv: String,
    val porodica: String,
    val medicinskoUpozorenje: String,
    val medicinskeKoristi: List<MedicinskaKorist>,
    val profilOkusa: ProfilOkusaBiljke,
    val jela: List<String>,
    val klimatskiTipovi: List<KlimatskiTip>,
    val zemljisniTipovi: List<Zemljiste>
)