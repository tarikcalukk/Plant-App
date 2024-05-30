package ba.unsa.etf.rma24.projekat.trefle

import com.google.gson.annotations.SerializedName

data class Specifications(
    @SerializedName("toxicity") val toxicity: String?
)