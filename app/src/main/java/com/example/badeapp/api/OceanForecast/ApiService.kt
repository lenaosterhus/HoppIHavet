package com.example.badeapp.api.OceanForecast


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface ApiService {

    // https://in2000-apiproxy.ifi.uio.no/weatherapi/oceanforecast/0.9/.json?lat=59.9016&lon=10.665422
    // Suspend fun --> called in coroutines

    //@Headers("User-Agent: ") @TODO add user agent
    @GET(".json")
    suspend fun getData(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Response<ResponseFormat?>

}