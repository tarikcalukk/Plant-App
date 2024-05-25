package ba.unsa.etf.rma24.projekat.pomocneKlase

import com.google.gson.annotations.SerializedName

data class TrefleBiljka(
    @SerializedName("id") val id: Int,
    @SerializedName("common_name") val commonName: String?,
    @SerializedName("scientific_name") val scientificName: String,
    @SerializedName("image_url") val slika: String?,
    @SerializedName("flower_color") val flowerColor: String?,
    @SerializedName("family_name") val family: String,
    @SerializedName("edible") val edible: Boolean,
    @SerializedName("main_species") val mainSpecies: MainSpecies?
)