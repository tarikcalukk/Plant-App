package ba.unsa.etf.rma24.projekat.trefle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import ba.unsa.etf.rma24.projekat.Biljka
import ba.unsa.etf.rma24.projekat.BuildConfig
import ba.unsa.etf.rma24.projekat.R
import ba.unsa.etf.rma24.projekat.pomocneKlase.KlimatskiTip
import ba.unsa.etf.rma24.projekat.pomocneKlase.Zemljiste
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class TrefleDAO (private val context : Context) {
    private val defaultBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.eucaliptus)
    private val validSoilTypes = mapOf(
        "SLJUNOVITO" to 9,
        "KKRECNJACKO" to 10,
        "GLINENO" to 1..2,
        "PJESKOVITO" to 3..4,
        "ILOVACA" to 5..6,
        "CRNICA" to 7..8
    )
    private val validClimateTypes = mapOf(
        "SREDOZEMNA" to (6..9 to 1..5),
        "TROPSKA" to (8..10 to 7..10),
        "SUBTROPSKA" to (6..9 to 5..8),
        "UMJERENA" to (4..7 to 3..7),
        "SUHA" to (7..9 to 1..2),
        "PLANINSKA" to (0..5 to 3..7)
    )
    suspend fun getImage(biljka: Biljka): Bitmap {
        return withContext(Dispatchers.IO) {
            try {
                val latinskiNaziv = biljka.naziv.substringAfter("(").substringBefore(")")
                val response =
                    TrefleAdapter.retrofit.searchPlants(BuildConfig.TREFLE_API_KEY, latinskiNaziv)
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
        val latinskiNaziv = plant.naziv.substringAfter("(").substringBefore(")")
        val searchResponse = TrefleAdapter.retrofit.searchPlants(BuildConfig.TREFLE_API_KEY, latinskiNaziv)
        val trefleBiljka = searchResponse.body()?.plants?.firstOrNull()
        trefleBiljka?.let {
            val detailsResponse = TrefleAdapter.retrofit.getPlantDetails(it.id,
                BuildConfig.TREFLE_API_KEY
            )
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
                    plant.zemljisniTipovi = plant.zemljisniTipovi.filter { soilType ->
                        validSoilTypes[soilType.naziv]?.let { range ->
                            when (range) {
                                is Int -> {
                                    details.mainSpecies?.growth?.soilTexture?.any { texture ->
                                        range == texture.toInt()
                                    } ?: false
                                }
                                is IntRange -> {
                                    details.mainSpecies?.growth?.soilTexture?.any { texture ->
                                        range.contains(texture.toInt())
                                    } ?: false
                                }
                                else -> false
                            }
                        } ?: false
                    }
                    plant.klimatskiTipovi = plant.klimatskiTipovi.filter { climateType ->
                        validClimateTypes[climateType.opis]?.let { (lightRange, humidityRange) ->
                            details.mainSpecies?.growth?.let { growth ->
                                lightRange.contains(growth.light ?: 0) && humidityRange.contains(growth.atmosphericHumidity ?: 0)
                            } ?: false
                        } ?: false
                    }
                }
            }
        }
        return plant
    }

    suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {
        val resultList = mutableListOf<Biljka>()
        val response = TrefleAdapter.retrofit.getPlants(flowerColor = flowerColor, query = substr)
        val plants = response.body()?.plants ?: emptyList()

        for (plant in plants) {
            val plantDetailsResponse = TrefleAdapter.retrofit.getPlantDetails(
                id = plant.id,
                apiKey = BuildConfig.TREFLE_API_KEY
            )
            val plantDetails = plantDetailsResponse.body()?.data
            val name = plantDetails?.scientificName ?: ""

            if (name.contains(substr, ignoreCase = true)) {
                val zemljisniTip= Zemljiste.entries.filter { soilType ->
                    validSoilTypes[soilType.name]?.let { range ->
                        when (range) {
                            is Int -> {
                                plantDetails?.mainSpecies?.growth?.soilTexture?.any { texture ->
                                    range == texture.toInt()
                                } ?: false
                            }
                            is IntRange -> {
                                plantDetails?.mainSpecies?.growth?.soilTexture?.any { texture ->
                                    range.contains(texture.toInt())
                                } ?: false
                            }
                            else -> false
                        }
                    } ?: false
                }

                val klimatskiTip = KlimatskiTip.entries.filter { climateType ->
                    validClimateTypes[climateType.name]?.let { (lightRange, humidityRange) ->
                        plantDetails?.mainSpecies?.growth?.let { growth ->
                            lightRange.contains(growth.light ?: 0) && humidityRange.contains(growth.atmosphericHumidity ?: 0)
                        } ?: false
                    } ?: false
                }

                val biljka = Biljka(
                    naziv = name,
                    porodica = plantDetails?.family,
                    medicinskoUpozorenje = "",
                    medicinskeKoristi = emptyList(),
                    jela = emptyList(),
                    profilOkusa = null,
                    klimatskiTipovi = klimatskiTip,
                    zemljisniTipovi = zemljisniTip
                )
                resultList.add(biljka)
            }
        }
        return resultList
    }


    suspend fun loadImageIntoImageView(imageView: ImageView, biljka: Biljka) {
        val bitmap = getImage(biljka)
        Glide.with(context)
            .load(bitmap)
            .placeholder(R.drawable.eucaliptus)
            .error(R.drawable.eucaliptus)
            .fallback(R.drawable.eucaliptus)
            .into(imageView)
    }
}