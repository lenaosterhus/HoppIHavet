package com.example.badeapp.models

import androidx.room.Entity

@Entity(primaryKeys = ["lat", "lon", "from", "to"], tableName = "Ocean_Forecast_Table")
data class OceanForecast(
    val lat: String,
    val lon: String,
    val from: String,
    val to: String,
    val waterTempC: Double?
)