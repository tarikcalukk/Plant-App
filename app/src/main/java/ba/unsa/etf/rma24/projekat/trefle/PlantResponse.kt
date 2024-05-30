package ba.unsa.etf.rma24.projekat.trefle

import com.google.gson.annotations.SerializedName

data class PlantResponse(
    @SerializedName("data") val plants: List<TrefleBiljka>
)