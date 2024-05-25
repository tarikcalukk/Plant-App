package ba.unsa.etf.rma24.projekat

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ba.unsa.etf.rma24.projekat.adapteri.TrefleAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class TrefleDAO {
    private val defaultBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    suspend fun getImage(biljka: Biljka): Bitmap {
        return withContext(Dispatchers.IO) {
            try {
                val latinskiNaziv = biljka.naziv.substringAfter("(").substringBefore(")")
                val encodedString =
                    URLEncoder.encode(latinskiNaziv, StandardCharsets.UTF_8.toString())
                val response =
                    TrefleAdapter.retrofit.searchPlants(BuildConfig.TREFLE_API_KEY, encodedString)
                if (response.isSuccessful) {
                    val plants = response.body()?.plants
                    if (!plants.isNullOrEmpty()) {
                        val imageUrl = plants.first().slika
                        if (!imageUrl.isNullOrEmpty()) {
                            val imageResponse = URL(imageUrl).openStream()
                            return@withContext BitmapFactory.decodeStream(imageResponse)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            defaultBitmap
        }
    }

    suspend fun fixData(plant: Biljka): Biljka {
        val searchResponse = TrefleAdapter.retrofit.searchPlants(BuildConfig.TREFLE_API_KEY, plant.naziv)
        if (searchResponse.isSuccessful) {
            val trefleBiljka = searchResponse.body()?.plants?.firstOrNull()
            trefleBiljka?.let {
                val detailsResponse = TrefleAdapter.retrofit.getPlantDetails(it.id, BuildConfig.TREFLE_API_KEY)
                if (detailsResponse.isSuccessful) {
                    val plantDetails = detailsResponse.body()?.data
                    plantDetails?.let { details ->
                        if (details.family != null && plant.porodica != details.family) {
                            plant.porodica = details.family
                        }

                        if (!details.edible) {
                            plant.jela = emptyList()
                            if (!plant.medicinskoUpozorenje.contains("NIJE JESTIVO")) {
                                plant.medicinskoUpozorenje += " NIJE JESTIVO"
                            }
                        }

                        details.mainSpecies?.specifications?.toxicity?.let { toxicity ->
                            if (toxicity != "none" && !plant.medicinskoUpozorenje.contains("TOKSIČNO")) {
                                plant.medicinskoUpozorenje += " TOKSIČNO"
                            }
                        }

                        val validSoilTypes = mapOf(
                            "SLJUNOVITO" to 9,
                            "KKRECNJACKO" to 10,
                            "GLINENO" to 1..2,
                            "PJESKOVITO" to 3..4,
                            "ILOVACA" to 5..6,
                            "CRNICA" to 7..8
                        )
                        plant.zemljisniTipovi = plant.zemljisniTipovi.filter { soilType ->
                            validSoilTypes[soilType.naziv]?.let { range ->
                                when (range) {
                                    is Int -> {
                                        details.mainSpecies?.specifications?.growth?.soilTexture?.any { texture ->
                                            range == texture.toInt()
                                        } ?: false
                                    }
                                    is IntRange -> {
                                        details.mainSpecies?.specifications?.growth?.soilTexture?.any { texture ->
                                            range.contains(texture.toInt())
                                        } ?: false
                                    }
                                    else -> false // Handle other cases if needed
                                }
                            } ?: false
                        }


                        // Handle climate types
                        val validClimateTypes = mapOf(
                            "SREDOZEMNA" to (6..9 to 1..5),
                            "TROPSKA" to (8..10 to 7..10),
                            "SUBTROPSKA" to (6..9 to 5..8),
                            "UMJERENA" to (4..7 to 3..7),
                            "SUHA" to (7..9 to 1..2),
                            "PLANINSKA" to (0..5 to 3..7)
                        )
                        plant.klimatskiTipovi = plant.klimatskiTipovi.filter { climateType ->
                            validClimateTypes[climateType.opis]?.let { (lightRange, humidityRange) ->
                                details.mainSpecies?.specifications?.growth?.let { growth ->
                                    lightRange.contains(growth.light ?: 0) && humidityRange.contains(growth.atmosphericHumidity ?: 0)
                                } ?: false
                            } ?: false
                        }
                    }
                }
            }
        }
        return plant
    }
/*
    suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {
        val response = TrefleAdapter.retrofit.searchPlantsByColor(
            query = substr,
            flowerColor = flowerColor
        )

        if (response.isSuccessful) {
            val plants = response.body()?.data ?: emptyList()
            return plants.map { plant ->
                Biljka(
                    naziv = plant.common_name ?: "",
                    porodica = plant.family_name ?: "",
                    medicinskoUpozorenje = "",
                    medicinskeKoristi = emptyList(),
                    jela = emptyList(),
                    profilOkusa = null,
                    klimatskiTipovi = emptyList(),
                    zemljisniTipovi = emptyList()
                )
            }
        } else {
            // In case of unsuccessful response, return an empty list
            return emptyList()
        }
    }*/
}