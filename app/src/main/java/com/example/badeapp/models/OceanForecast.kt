package com.example.badeapp.models

import androidx.room.Entity
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.minBetween
import com.example.badeapp.util.parseAsGmtIsoDate
import java.lang.Math.abs

private const val TAG = "OceanFrecast"

@Entity(primaryKeys = ["lat", "lon", "timeInstance"], tableName = "Ocean_Forecast_Table")
data class OceanForecast(
    val lat: String,
    val lon: String,
    val timeInstance: String,
    val nextIssue: String,
    val waterTempC: Double?
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


/**
 * Takes a list of LocationForecast and lets us find the current Air Temp (if present)
 */
fun List<OceanForecast>.getCurrentWaterTempC(): Double? {
    return this.getCurrentForecast()?.waterTempC
}


/**
 * Takes a list of LocationForecasts and returns the one representing now, if that would be applicable
 */
fun List<OceanForecast>.getCurrentForecast(): OceanForecast? {
    //@TODO sjekk at vi får riktig resultat
    return this.minBy {
        abs(currentTime().time - it.timeInstance.parseAsGmtIsoDate()!!.time)
    }

}



