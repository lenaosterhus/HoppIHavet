package com.example.badeapp.api.LocationForecast

/**
 * This file contains all the code needed to do http request to Meteorology Institute
 * location forecast service. It happens to use Retrofit, but that is just a implementation
 * detail.
 */


import android.util.Log
import com.example.badeapp.api.MIThrottler
import com.example.badeapp.models.LocationForecastInfo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestManager {


    private val TAG = "DEBUG-LocationReqMngr"
    private val BASE_URL_WEATHER =
        "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/1.9/"

    // lazy = only initialized once, use the same instance
    private val retrofitBuilder: Retrofit.Builder by lazy {
        Log.d(TAG, "building...")
        Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER)
            .addConverterFactory(GsonConverterFactory.create())
    }

    private val apiService: ApiService by lazy {
        Log.d(TAG, "building apiService")
        retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }


    suspend fun request(lat: String, long: String): LocationForecastInfo? {

        if (!MIThrottler.hasStopped()) {
            val response = apiService.getWeatherData(lat, long)
            MIThrottler.submitCode(response.code())
            if (response.isSuccessful)
                return response.body()?.summarise()
        }
        return null
    }


}