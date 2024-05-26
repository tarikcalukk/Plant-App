package ba.unsa.etf.rma24.projekat.Trefle

import com.google.gson.annotations.SerializedName

data class Growth(
    @SerializedName("soil_texture") val soilTexture: List<String>?,
    @SerializedName("light") val light: Int?,
    @SerializedName("atmospheric_humidity") val atmosphericHumidity: Int?
)