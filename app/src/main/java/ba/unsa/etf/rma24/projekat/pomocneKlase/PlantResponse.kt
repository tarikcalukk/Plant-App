package ba.unsa.etf.rma24.projekat.pomocneKlase

import com.google.gson.annotations.SerializedName

data class PlantResponse(
    @SerializedName("data") val plants: List<TrefleBiljka>
)