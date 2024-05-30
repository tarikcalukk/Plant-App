package ba.unsa.etf.rma24.projekat.trefle

import com.google.gson.annotations.SerializedName

data class IdResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("common_name") val commonName: String?,
    @SerializedName("scientific_name") val scientificName: String,
    @SerializedName("main_species") val mainSpecies: MainSpecies
)