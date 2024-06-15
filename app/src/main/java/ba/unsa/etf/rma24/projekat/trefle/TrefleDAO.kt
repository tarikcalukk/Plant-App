package ba.unsa.etf.rma24.projekat.trefle

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import ba.unsa.etf.rma24.projekat.Biljka
import ba.unsa.etf.rma24.projekat.BuildConfig
import ba.unsa.etf.rma24.projekat.R
import ba.unsa.etf.rma24.projekat.RetrofitInstance
import ba.unsa.etf.rma24.projekat.pomocneKlase.KlimatskiTip
import ba.unsa.etf.rma24.projekat.pomocneKlase.Zemljiste
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class TrefleDAO (private val context : Context?) {
    constructor() : this(null)

    private val defaultBitmap: Bitmap by lazy { loadDefaultBitmap() }
    private fun loadDefaultBitmap(): Bitmap {
        return context?.let {
            BitmapFactory.decodeResource(it.resources, R.drawable.eucaliptus)
        } ?: throw IllegalStateException("Context must not be null to load default bitmap")
    }
    private val zemljisniTip = mapOf(
        "SLJUNKOVITO" to 9,
        "KRECNJACKO" to 10,
        "GLINENO" to 1..2,
        "PJESKOVITO" to 3..4,
        "ILOVACA" to 5..6,
        "CRNICA" to 7..8
    )
    private val klimatskiTip = mapOf(
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
                    RetrofitInstance.apiService.searchPlants(BuildConfig.TREFLE_API_KEY, latinskiNaziv)
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
        val searchResponse = RetrofitInstance.apiService.searchPlants(BuildConfig.TREFLE_API_KEY, latinskiNaziv)
        val trefleBiljka = searchResponse.body()?.plants?.firstOrNull()
        trefleBiljka?.let {
            val detailsResponse = RetrofitInstance.apiService.getPlantDetails(it.id,
                BuildConfig.TREFLE_API_KEY
            )
            if (detailsResponse.isSuccessful) {
                val plantDetails = detailsResponse.body()?.data
                plantDetails?.let { details ->
                    if (details.mainSpecies.family != null && plant.porodica != details.mainSpecies.family) {
                        plant.porodica = details.mainSpecies.family
                    }

                    if (!details.mainSpecies.edible) {
                        plant.jela = emptyList()
                        if (!plant.medicinskoUpozorenje.contains("NIJE JESTIVO")) {
                            plant.medicinskoUpozorenje += " NIJE JESTIVO"
                        }
                    }

                    details.mainSpecies.specifications?.toxicity?.let { toxicity ->
                        if (toxicity != "none" && !plant.medicinskoUpozorenje.contains("TOKSIČNO")) {
                            plant.medicinskoUpozorenje += " TOKSIČNO"
                        }
                    }
                    if (details.mainSpecies.growth?.soilTexture != null) {
                        val soilTextures = details.mainSpecies.growth.soilTexture.map { it.toInt() }
                        plant.zemljisniTipovi = zemljisniTip.entries.filter { (_, range) ->
                            when (range) {
                                is Int -> soilTextures.contains(range)
                                is IntRange -> soilTextures.any { range.contains(it) }
                                else -> false
                            }
                        }.map { (key, _) -> Zemljiste.valueOf(key) }
                    }

                    if (details.mainSpecies.growth?.light != null && details.mainSpecies.growth.atmosphericHumidity != null) {
                        val light = details.mainSpecies.growth.light.toInt()
                        val humidity = details.mainSpecies.growth.atmosphericHumidity.toInt()
                        plant.klimatskiTipovi = klimatskiTip.entries.filter { (_, ranges) ->
                            val (lightRange, humidityRange) = ranges
                            light in lightRange && humidity in humidityRange
                        }.map { (key, _) -> KlimatskiTip.valueOf(key) }
                    }
                }
            }
        }
        return plant
    }

    suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {
        val resultList = mutableListOf<Biljka>()
        val response = RetrofitInstance.apiService.getPlants(
            apiKey = BuildConfig.TREFLE_API_KEY,
            flowerColor = flowerColor,
            query = substr
        )
        val plants = response.body()?.plants ?: emptyList()

        for (plant in plants) {
            val plantDetailsResponse = RetrofitInstance.apiService.getPlantDetails(
                id = plant.id,
                apiKey = BuildConfig.TREFLE_API_KEY
            )
                val plantDetails = plantDetailsResponse.body()?.data
                val name = plantDetails?.commonName ?: ""
                val biljka = Biljka(
                    naziv = name + " (" + plantDetails?.scientificName + ")",
                    porodica = plantDetails?.mainSpecies?.family ?: "",
                    medicinskoUpozorenje = "",
                    medicinskeKoristi = emptyList(),
                    profilOkusa = null,
                    jela = emptyList(),
                    klimatskiTipovi = emptyList(),
                    zemljisniTipovi = emptyList()
                )
                val fixedBiljka = fixData(biljka)
                resultList.add(fixedBiljka)
            }
        return resultList
    }




    suspend fun loadImageIntoImageView(imageView: ImageView, biljka: Biljka) {
        val bitmap = getImage(biljka)
        val context = imageView.context

        if (context != null && context is Activity) {
            Glide.with(context)
                .load(bitmap)
                .placeholder(R.drawable.eucaliptus)
                .error(R.drawable.eucaliptus)
                .fallback(R.drawable.eucaliptus)
                .into(imageView)
        }
    }
}