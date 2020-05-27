package com.example.badeapp.api.oceanForecast

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


    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_OCEAN)
            .addConverterFactory(GsonConverterFactory.create())
    }

    private val apiService: ApiService by lazy {
        retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }


    @Headers("User-Agent: $USER_HEADER")
    suspend fun request(badested: Badested): List<OceanForecast>? {

        if (!MIThrottler.hasStopped()) {
            val response = apiService.getOceanData(badested.lat, badested.lon)
            MIThrottler.submitCode(response.code())

            if (response.isSuccessful) return response.body()?.summarise(badested)
        }
        return null
    }
}