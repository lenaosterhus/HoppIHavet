package com.example.badeapp.api

import com.example.badeapp.models.OceanForecast
import com.example.badeapp.models.WeatherForecast
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    // https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/1.9/.json?lat=60.10&lon=9.58
    // Suspend fun --> called in coroutine

    //@Headers("User-Agent: ") @TODO add user agent
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET(".json")
    suspend fun getData(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ) : WeatherForecast

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET(".json")
    suspend fun getDataWithMsl(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("msl") msl: String
    ) : WeatherForecast

    // https://in2000-apiproxy.ifi.uio.no/weatherapi/oceanforecast/0.9/.json?lat=59.9016&lon=10.665422

    @GET(".json")
    suspend fun getOceanData(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ) : OceanForecast
}