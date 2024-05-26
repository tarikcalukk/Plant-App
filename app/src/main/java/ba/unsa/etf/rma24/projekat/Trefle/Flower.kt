package ba.unsa.etf.rma24.projekat.Trefle

import com.google.gson.annotations.SerializedName

data class Flower (
    @SerializedName("color") val color: List<String>?
)