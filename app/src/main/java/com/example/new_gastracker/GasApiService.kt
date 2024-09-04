package com.example.new_gastracker

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GasApiService {
    @GET("carlosrsabreu/devo-abastecer/main/gas_info.json")
    suspend fun getGasInfo(): GasResponse
}

object RetrofitInstance {
    private const val BASE_URL = "https://raw.githubusercontent.com/"

    val api: GasApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GasApiService::class.java)
    }
}



interface PlacesApiService {
    @GET("nearbysearch/json")
    fun getNearbyGasStations(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String = "gas_station",
        @Query("key") apiKey: String = "AIzaSyCsUKTs3FzbZdKc27_tHpCF5gcjNvMpE5U"
    ): Call<PlacesResponse>
}

object RetrofitClient {
    private const val URL = "https://maps.googleapis.com/maps/api/place/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: PlacesApiService by lazy {
        retrofit.create(PlacesApiService::class.java)
    }
}
