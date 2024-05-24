package ba.unsa.etf.rma24.projekat.pomocneKlase

import ba.unsa.etf.rma24.projekat.BuildConfig
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface TrefleApi {
    @GET("plants/search")
    suspend fun searchPlants(
        @Query("token") apiKey: String = BuildConfig.TREFLE_API_KEY,
        @Query("q") query: String
    ): Response<PlantResponse>

    @GET
    suspend fun getPlantImage(@Url imageUrl: String): Response<ResponseBody>
}
