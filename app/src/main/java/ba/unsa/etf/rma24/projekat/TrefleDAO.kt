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
                val encodedString = URLEncoder.encode(latinskiNaziv, StandardCharsets.UTF_8.toString())
                val response = TrefleAdapter.retrofit.searchPlants(BuildConfig.TREFLE_API_KEY, encodedString)
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


    suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {
        return withContext(Dispatchers.IO) {
            try {
                val apiKey = BuildConfig.TREFLE_API_KEY
                val response = TrefleAdapter.retrofit.searchPlants(apiKey, substr)
                if (response.isSuccessful) {
                    val plants = response.body()?.plants
                    if (!plants.isNullOrEmpty()) {
                        val filteredPlants = plants.filter { plant ->
                            val matchesColor = plant.flowerColor?.contains(flowerColor, ignoreCase = true) ?: false
                            val matchesSubstr = plant.commonName?.contains(substr, ignoreCase = true) ?: false
                            matchesColor && matchesSubstr
                        }.map { plant ->
                            Biljka(
                                naziv = plant.commonName ?: "",
                                porodica = plant.family ?: "",
                                medicinskoUpozorenje = "",
                                medicinskeKoristi = emptyList(),
                                jela = emptyList(),
                                profilOkusa = null,
                                klimatskiTipovi = emptyList(),
                                zemljisniTipovi = emptyList()
                            )
                        }
                        return@withContext filteredPlants
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext emptyList()
        }
    }



}

