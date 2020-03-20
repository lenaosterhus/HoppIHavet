package com.example.badeapp.api

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OceanRetrofitBuilder {

    private val TAG = "DEBUG - MyRetroBuilder"
    private val BASE_URL_OCEAN = "https://in2000-apiproxy.ifi.uio.no/weatherapi/oceanforecast/0.9/"

    // lazy = only initialize once, use the same instance
    val retrofitBuilder: Retrofit.Builder by lazy {
        Log.d(TAG, "building OCEAN...")
        Retrofit.Builder()
            .baseUrl(BASE_URL_OCEAN)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiService: ApiService by lazy {
        Log.d(TAG, "building OCEAN apiService")
        retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }
}