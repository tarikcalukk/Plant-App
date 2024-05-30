package ba.unsa.etf.rma24.projekat.trefle

import com.google.gson.annotations.SerializedName

data class MainSpecies(
    @SerializedName("family") val family: String?,
    @SerializedName("edible") val edible: Boolean,
    @SerializedName("specifications") val specifications: Specifications?,
    @SerializedName("growth") val growth: Growth?
)
