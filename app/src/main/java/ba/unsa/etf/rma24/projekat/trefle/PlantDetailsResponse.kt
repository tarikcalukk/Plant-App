package ba.unsa.etf.rma24.projekat.trefle

import com.google.gson.annotations.SerializedName

data class PlantDetailsResponse(
    @SerializedName("data") val data: IdResponse
)