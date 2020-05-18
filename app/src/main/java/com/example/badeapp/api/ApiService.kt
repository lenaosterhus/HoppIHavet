package com.example.badeapp.api

import com.example.badeapp.api.locationForecast.LocationResponseFormat
import com.example.badeapp.api.oceanForecast.OceanResponseFormat
import com.example.badeapp.util.CONTENT_TYPE
import com.example.badeapp.util.USER_HEADER
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

internal interface ApiService {

    // https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/1.9/.json?lat=60.10&lon=9.58

    @Headers(
        "Content-Type: $CONTENT_TYPE",
        "User-Agent: $USER_HEADER"
    )
    @GET(".json")
    suspend fun getWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Response<LocationResponseFormat?>


    // https://in2000-apiproxy.ifi.uio.no/weatherapi/oceanforecast/0.9/.json?lat=59.9016&lon=10.665422
    // Suspend fun --> called in coroutines

    @Headers(
        "Content-Type: $CONTENT_TYPE",
        "User-Agent: $USER_HEADER"
    )
    @GET(".json")
    suspend fun getOceanData(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Response<OceanResponseFormat?>


}