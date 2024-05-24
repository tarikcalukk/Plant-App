package ba.unsa.etf.rma24.projekat.adapteri

import ba.unsa.etf.rma24.projekat.pomocneKlase.TrefleApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TrefleAdapter {
    val retrofit : TrefleApi = Retrofit.Builder()
        .baseUrl("https://trefle.io/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TrefleApi::class.java)
}