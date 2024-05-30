package ba.unsa.etf.rma24.projekat.trefle

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("common_name") val commonName: String?,
    @SerializedName("scientific_name") val scientificName: String,
    @SerializedName("image_url") val slika: String?,
    @SerializedName("family") val family: String?
)