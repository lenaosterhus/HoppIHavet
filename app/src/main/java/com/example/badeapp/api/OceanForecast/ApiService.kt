package com.example.badeapp.api.OceanForecast


import retrofit2.http.GET
import retrofit2.http.Query

internal interface ApiService {

    // https://in2000-apiproxy.ifi.uio.no/weatherapi/oceanforecast/0.9/.json?lat=59.9016&lon=10.665422
    // Suspend fun --> called in coroutines
    @GET(".json")
    suspend fun getOceanData(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): ResponseFormat?

}