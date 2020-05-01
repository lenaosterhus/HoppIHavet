package com.example.badeapp.api.OceanForecast

import android.util.Log
import com.example.badeapp.api.MIThrottler
import com.example.badeapp.models.Badested
import com.example.badeapp.models.OceanForecast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers

object RequestManager {

    private const val TAG = "DEBUG - MyRetroBuilder"
    private const val BASE_URL_OCEAN =
        "https://in2000-apiproxy.ifi.uio.no/weatherapi/oceanforecast/0.9/"
    private const val USER_HEADER = "GRUPPE-38"

    // lazy = only initialize once, use the same instance
    private val retrofitBuilder: Retrofit.Builder by lazy {
        Log.d(TAG, "building OCEAN...")
        Retrofit.Builder()
            .baseUrl(BASE_URL_OCEAN)
            .addConverterFactory(GsonConverterFactory.create())
    }

    private val apiService: ApiService by lazy {
        Log.d(TAG, "building OCEAN apiService")
        retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }

    @Headers("User-Agent: $USER_HEADER")
    suspend fun request(badested: Badested): List<OceanForecast>? {
        if (!MIThrottler.hasStopped()) {
            val response = apiService.getData(badested.lat, badested.lon)
            MIThrottler.submitCode(response.code())
            if (response.isSuccessful)
                return response.body()?.summarize(badested)
        }
        return null
    }

}