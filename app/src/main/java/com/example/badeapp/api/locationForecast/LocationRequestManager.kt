package com.example.badeapp.api.locationForecast

import android.util.Log
import com.example.badeapp.api.ApiService
import com.example.badeapp.api.MIThrottler
import com.example.badeapp.models.Badested
import com.example.badeapp.models.LocationForecast
import com.example.badeapp.util.BASE_URL_LOCATION
import com.example.badeapp.util.USER_HEADER
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers

/**
 * This file contains all the code needed to do http request to Meteorology Institute
 * location forecast service. It happens to use Retrofit, but that is just a implementation
 * detail.
 */

object LocationRequestManager {

    private const val TAG = "LocationReqMngr"

    // lazy = only initialized once, use the same instance
    private val retrofitBuilder: Retrofit.Builder by lazy {
        Log.d(TAG, "building LOCATION...")
        Retrofit.Builder()
            .baseUrl(BASE_URL_LOCATION)
            .addConverterFactory(GsonConverterFactory.create())
    }


    private val apiService: ApiService by lazy {
        Log.d(TAG, "building LOCATION apiService")
        retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }

    @Headers("User-Agent: $USER_HEADER")
    suspend fun request(
        badested: Badested
    ): List<LocationForecast>? {
        Log.d(TAG, "$badested")
        if (!MIThrottler.hasStopped()) {
            Log.d(TAG, "request: API REQUEST location")
            val response = apiService.getWeatherData(badested.lat, badested.lon)
            MIThrottler.submitCode(response.code())
            if (response.isSuccessful) {
                val res = response.body()?.summarise(badested)
                Log.d(TAG, "$res\n\n")
                return res
            }
        }
        return null
    }
}