package com.example.badeapp.api.OceanForecast

import android.util.Log
import com.example.badeapp.api.MIThrottler
import com.example.badeapp.models.OceanForecastInfo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestManager {

    private val TAG = "DEBUG - MyRetroBuilder"
    private val BASE_URL_OCEAN = "https://in2000-apiproxy.ifi.uio.no/weatherapi/oceanforecast/0.9/"

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

    suspend fun request(lat: String, long: String): OceanForecastInfo? {
        if (!MIThrottler.hasStopped()) {
            val response = apiService.getData(lat, long)
            MIThrottler.submitCode(response.code())
            if (response.isSuccessful)
                return response.body()?.summarize()
        }
        return null
    }

}