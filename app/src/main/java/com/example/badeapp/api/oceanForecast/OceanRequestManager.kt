package com.example.badeapp.api.oceanForecast

import android.util.Log
import com.example.badeapp.api.ApiService
import com.example.badeapp.api.MIThrottler
import com.example.badeapp.models.Badested
import com.example.badeapp.models.OceanForecast
import com.example.badeapp.util.BASE_URL_OCEAN
import com.example.badeapp.util.USER_HEADER
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers

/**
 * This file contains all the code needed to do http request to Meteorology Institute
 * location forecast service. It happens to use Retrofit, but that is just a implementation
 * detail.
 */

object OceanRequestManager {

    private const val TAG = "OceanReqMngr"

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
            Log.d(TAG, "request: API REQUEST ocean")
            val response = apiService.getOceanData(badested.lat, badested.lon)
            MIThrottler.submitCode(response.code())
            if (response.isSuccessful)
                return response.body()?.summarise(badested)
        }
        return null
    }
}