package com.example.badeapp.models

import androidx.room.Entity
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.liesBetweneInclusive
import com.example.badeapp.util.parseAsGmtIsoDate

@Entity(primaryKeys = ["lat", "lon", "from", "to"], tableName = "Ocean_Forecast_Table")
data class OceanForecast(
    val lat: String,
    val lon: String,
    val from: String,
    val to: String,
    val waterTempC: Double?
)


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
    val now = currentTime()
    return this.find { hour ->
        now.liesBetweneInclusive(
            hour.from.parseAsGmtIsoDate()!!,
            hour.to.parseAsGmtIsoDate()!!
        )
    }
}



