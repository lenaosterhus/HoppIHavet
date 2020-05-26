package com.example.badeapp.api.locationForecast

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

//    private const val TAG = "LocationReqMngr"

    private val retrofitBuilder: Retrofit.Builder by lazy {
//        Log.d(TAG, "building Retrofit...")
        Retrofit.Builder()
            .baseUrl(BASE_URL_LOCATION)
            .addConverterFactory(GsonConverterFactory.create())
    }

    private val apiService: ApiService by lazy {
//        Log.d(TAG, "building apiService")
        retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }


    @Headers("User-Agent: $USER_HEADER")
    suspend fun request( badested: Badested) : List<LocationForecast>? {

        if (!MIThrottler.hasStopped()) {
//            Log.d(TAG, "request: API REQUEST")
            val response = apiService.getLocationData(badested.lat, badested.lon)
            MIThrottler.submitCode(response.code())

            if (response.isSuccessful) return response.body()?.summarise(badested)
        }
        return null
    }
}