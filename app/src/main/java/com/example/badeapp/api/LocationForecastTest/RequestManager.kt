package com.example.badeapp.api.LocationForecastTest


/**
 * This file contains all the code needed to do http request to Meteorology Institute
 * location forecast service. It happens to use Retrofit, but that is just a implementation
 * detail.
 */


import android.util.Log
import com.example.badeapp.api.MIThrottler
import com.example.badeapp.models.LocationForecast
import com.example.badeapp.models.LocationForecastInfo
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RequestManager {

    private const val USER_HEADER = "GRUPPE-38"

    private val TAG = "DEBUG-LocReqMngrTest"
    private val BASE_URL_WEATHER =
        "http://139.162.240.144/"

    // lazy = only initialized once, use the same instance
    private val retrofitBuilder: Retrofit.Builder by lazy {
        Log.d(TAG, "building WEATHER...")
        Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER)
            .addConverterFactory(GsonConverterFactory.create())
    }


    private val retrofitBuilderRaw: Retrofit.Builder by lazy {
        Log.d(TAG, "building WEATHER raw...")
        Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER)
            .addConverterFactory(ScalarsConverterFactory.create())

    }


    private val apiService: ApiServiceTest by lazy {
        Log.d(TAG, "building WEATHER apiService")
        retrofitBuilder
            .build()
            .create(ApiServiceTest::class.java)
    }

    private val apiServiceRaw: ApiServiceTest by lazy {
        Log.d(TAG, "building WEATHER apiService raw")
        retrofitBuilderRaw
            .build()
            .create(ApiServiceTest::class.java)
    }


    /**
     * @param lat, lon -> lat and lon of area we are wanting the response for
     * @param session -> we get the same result for every session
     * @param timeShift This variable manipulates all the dates in the reponse time make it look like
     * the response was created at this time. The temperature and symbols etc.. are the same, but the
     * times are shifted.
     *
     * @param banMe -> makes the server give the response code 429, that signals we are about to be banned
     * @param throttleMe -> makes the server give the response 203, that signals we are approaching
     * a situation were we might get banned.
     * */

    suspend fun request(
        lat: String,
        lon: String,
        session: String,
        timeShift: String,
        throttleMe: Boolean,
        banMe: Boolean
    ): Pair<LocationForecastInfo, List<LocationForecast>>? {
        if (!MIThrottler.hasStopped()) {
            val response =
                apiService.getWeatherData(lat, lon, session, timeShift, throttleMe, banMe)
            MIThrottler.submitCode(response.code())
            if (response.isSuccessful)
                return response.body()?.summarise(lat, lon)
            else {
                Log.d(TAG, response.toString())
            }
        }
        Log.d(TAG, "Request halted because MIThrotteler says we are banned.")
        return null
    }


    suspend fun requestRaw(
        lat: String,
        lon: String,
        session: String,
        timeShift: String = "",
        throttleMe: Boolean = false,
        banMe: Boolean = false
    ): Response<String> {
        val res = apiServiceRaw.getWeatherDataRaw(lat, lon, session, timeShift, throttleMe, banMe)
        Log.d(TAG, "HERE")
        return res
    }



}