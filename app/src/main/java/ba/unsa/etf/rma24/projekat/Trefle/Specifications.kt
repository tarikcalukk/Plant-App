package ba.unsa.etf.rma24.projekat.Trefle

import com.google.gson.annotations.SerializedName

data class Specifications(
    @SerializedName("toxicity") val toxicity: String?,
    @SerializedName("growth") val growth: Growth?
)