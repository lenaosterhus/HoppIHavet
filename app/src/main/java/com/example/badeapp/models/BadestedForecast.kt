package com.example.badeapp.models

import androidx.room.Entity

@Entity(primaryKeys = ["lat", "lon", "from", "to"])
data class BadestedForecast(
    val lat: String,
    val lon: String,
    val from: String,
    val to: String,
    val airTempC: Double?,
    val waterTempC: Double?,
    val symbol: Int?
)

