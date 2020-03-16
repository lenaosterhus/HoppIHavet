package com.example.badeapp.api

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRetrofitBuilder {

    private val TAG = "DEBUG - MyRetroBuilder"
    private val BASE_URL_WEATHER = "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/1.9/"

    // lazy = only initialize once, use the same instance
    val retrofitBuilder: Retrofit.Builder by lazy {
        Log.d(TAG, "building...")
        Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiService: ApiService by lazy {
        Log.d(TAG, "building apiService")
        retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }
}