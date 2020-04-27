package com.example.badeapp.models

import androidx.room.Entity
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.minBetween


private const val TAG = "DEBUG - OceanInfo"

@Entity(primaryKeys = ["lat", "lon"], tableName = "Ocean_Forecast_Info_Table")
data class OceanForecastInfo(
    val lat: String,
    val lon: String,
    val nextIssue: String
) {

    // Format timePosition: "2020-04-02T14:00:00Z"
    fun isOutdated(): Boolean {
        return minUntilOutdated() < 10L
    }

    fun minUntilOutdated(): Long {
//        Log.d(TAG, "nextIssue: $nextIssue")
        return minBetween(currentTime(), nextIssue)!!
    }

}