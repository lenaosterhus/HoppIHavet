package com.example.badeapp.api.LocationForecast

/**
 * This file contains all the code needed to do http request to Meteorology Institute
 * location forecast service. It happens to use Retrofit, but that is just a implementation
 * detail.
 */


import android.util.Log
import com.example.badeapp.models.LocationForecastInfo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers

object RequestManager {

    private const val USER_HEADER = "GRUPPE-38"

    private val TAG = "DEBUG - MyRetroBuilder"
    private val BASE_URL_WEATHER =
        "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/1.9/"

    // lazy = only initialized once, use the same instance
    private val retrofitBuilder: Retrofit.Builder by lazy {
        Log.d(TAG, "building WEATHER...")
        Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER)
            .addConverterFactory(GsonConverterFactory.create())
    }

    internal val apiService: ApiService by lazy {
        Log.d(TAG, "building WEATHER apiService")
        retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }

    @Headers("user-key: $USER_HEADER")
    suspend fun request(lat: String, long: String): LocationForecastInfo? {

        Log.d("RESPONSE", "Running request for a badested.")
        val response = apiService.getWeatherData(lat, long)

        Log.d("RESPONSE", response.toString())

        if (response.isSuccessful) return response.body()?.summarise()

        if (response.code() == 203) {
            TODO("Throttle MI request til de mere essensielle, da denne slutter snart")
        }

        if (response.code() == 429) {
            TODO("Throttle all MI requests, ellers blir vi bannet!")
        }

        if (response.code() == 404) {
            TODO("Error Report error")
        }

        if (response.code() == 403) {
            Log.d("ERROR", "We are banned from MI!")
        }
        assert(false)
        return null
    }


}