package com.example.badeapp.api.OceanForecast

import android.util.Log
import com.example.badeapp.models.OceanForecastInfo
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
    suspend fun request(lat: String, long: String): OceanForecastInfo? {

        Log.d("RESPONSE", "Running request for a badested.")
        val response = RequestManager.apiService.getData(lat, long)

        Log.d("RESPONSE", response.toString())

        if (response.isSuccessful) return response.body()?.summarize()

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