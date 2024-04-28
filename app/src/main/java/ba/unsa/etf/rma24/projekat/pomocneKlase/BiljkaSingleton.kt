package ba.unsa.etf.rma24.projekat.pomocneKlase

import ba.unsa.etf.rma24.projekat.Biljka

object BiljkaSingleton {
    var listaBiljaka: MutableList<Biljka> = biljke.toMutableList()
    var filtriraneBiljke: List<Biljka> = biljke
}