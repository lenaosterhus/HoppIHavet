package com.example.badeapp.api.LocationForecast

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

internal interface ApiService {

    // https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/1.9/.json?lat=60.10&lon=9.58

    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "User-Agent: GRUPPE-38"
    )
    @GET(".json")
    suspend fun getWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Response<ResponseFormat?>


}