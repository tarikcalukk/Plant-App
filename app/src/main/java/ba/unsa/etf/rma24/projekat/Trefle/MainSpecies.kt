package ba.unsa.etf.rma24.projekat.Trefle

import com.google.gson.annotations.SerializedName

data class MainSpecies(
    @SerializedName("specifications") val specifications: Specifications?,
    @SerializedName("flower") val flower: Flower?
)
