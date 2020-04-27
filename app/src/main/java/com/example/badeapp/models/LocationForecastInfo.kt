package com.example.badeapp.models

import androidx.room.Entity
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.minBetween


private const val TAG = "DEBUG - LocationInfo"

@Entity(primaryKeys = ["lat", "lon"], tableName = "Location_Forecast_Info_Table")
data class LocationForecastInfo(
    val lat: String,
    val lon: String,
    val nextIssue: String
) {

    fun isOutdated(): Boolean {
        return minUntilOutdated() < 0L
    }

    fun minUntilOutdated(): Long {
        return minBetween(currentTime(), nextIssue)!!
    }
}