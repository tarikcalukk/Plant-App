package ba.unsa.etf.rma24.projekat.trefle

import com.google.gson.annotations.SerializedName

data class MainSpecies(
    @SerializedName("specifications") val specifications: Specifications?,
    @SerializedName("growth") val growth: Growth?
)
