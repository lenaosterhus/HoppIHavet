package com.example.badeapp.api.LocationForecastTest


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
import retrofit2.http.Headers

object RequestManager {

    private const val USER_HEADER = "GRUPPE-38"

    private val TAG = "DEBUG-LocationReqMngr"
    private val BASE_URL_WEATHER =
        "http://139.162.240.144/"

    // lazy = only initialized once, use the same instance
    private val retrofitBuilder: Retrofit.Builder by lazy {
        Log.d(TAG, "building WEATHER...")
        Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER)
            .addConverterFactory(GsonConverterFactory.create())
    }


    private val apiService: ApiServiceTest by lazy {
        Log.d(TAG, "building WEATHER apiService")
        retrofitBuilder
            .build()
            .create(ApiServiceTest::class.java)
    }


    @Headers("User-Agent: $USER_HEADER")
    suspend fun request(session: String, lat: String, long: String): LocationForecastInfo? {

        if (!MIThrottler.hasStopped()) {
            val response = apiService.getWeatherData(lat, long, session)
            MIThrottler.submitCode(response.code())
            if (response.isSuccessful)
                return response.body()?.summarise()
        }
        return null
    }


}