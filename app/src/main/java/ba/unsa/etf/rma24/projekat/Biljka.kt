package ba.unsa.etf.rma24.projekat

import ba.unsa.etf.rma24.projekat.pomocneKlase.KlimatskiTip
import ba.unsa.etf.rma24.projekat.pomocneKlase.MedicinskaKorist
import ba.unsa.etf.rma24.projekat.pomocneKlase.ProfilOkusaBiljke
import ba.unsa.etf.rma24.projekat.pomocneKlase.Zemljiste

data class Biljka(
    var naziv: String,
    var porodica: String?,
    var medicinskoUpozorenje: String,
    var medicinskeKoristi: List<MedicinskaKorist>,
    var jela: List<String>,
    var profilOkusa: ProfilOkusaBiljke?,
    var klimatskiTipovi: List<KlimatskiTip>,
    var zemljisniTipovi: List<Zemljiste>
)