package ba.unsa.etf.rma24.projekat.Trefle

import ba.unsa.etf.rma24.projekat.Trefle.TrefleBiljka
import com.google.gson.annotations.SerializedName

data class PlantResponse(
    @SerializedName("data") val plants: List<TrefleBiljka>
)