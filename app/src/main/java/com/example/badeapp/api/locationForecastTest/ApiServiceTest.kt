package com.example.badeapp.api.locationForecastTest


import com.example.badeapp.api.locationForecast.LocationResponseFormat
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


internal interface ApiServiceTest {


    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "User-Agent: GRUPPE-38"
    )
    @GET("locationforecast")
    suspend fun getWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("session") session: String
    ): Response<LocationResponseFormat?>

    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "User-Agent: GRUPPE-38"
    )
    @GET("locationforecast")
    suspend fun getWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("session") session: String,
        @Query("timeshift") timeShift: String,
        @Query("throttleMe") throttleMe: Boolean,
        @Query("banMe") banMe: Boolean
    ): Response<LocationResponseFormat>

    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "User-Agent: GRUPPE-38"
    )
    @GET("locationforecast")
    suspend fun getWeatherDataRaw(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("session") session: String,
        @Query("timeshift") timeShift: String,
        @Query("throttleMe") throttleMe: Boolean,
        @Query("banMe") banMe: Boolean
    ): Response<String>



}