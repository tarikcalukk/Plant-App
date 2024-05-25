package ba.unsa.etf.rma24.projekat.pomocneKlase

import com.google.gson.annotations.SerializedName

data class MainSpecies(
    @SerializedName("specifications") val specifications: Specifications?
)
