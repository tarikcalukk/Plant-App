package ba.unsa.etf.rma24.projekat.Trefle

import ba.unsa.etf.rma24.projekat.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrefleApi {
    @GET("plants/search")
    suspend fun searchPlants(
        @Query("token") apiKey: String = BuildConfig.TREFLE_API_KEY,
        @Query("q") query: String
    ): Response<PlantResponse>
    @GET("plants/{id}")
    suspend fun getPlantDetails(
        @Path("id") id: Int,
        @Query("token") apiKey: String
    ): Response<PlantDetailsResponse>
}
