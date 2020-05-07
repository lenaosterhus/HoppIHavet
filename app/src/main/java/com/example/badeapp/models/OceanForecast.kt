package com.example.badeapp.models

import android.util.Log
import androidx.room.Entity
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.liesBetweneInclusive
import com.example.badeapp.util.parseAsGmtIsoDate

private const val TAG = "OceanFrecast"


@Entity(primaryKeys = ["badested", "from", "to"], tableName = "Ocean_Forecast_Table")
data class OceanForecast(
    val badested: Badested,
    val from: String,
    val to: String,
    val nextIssue: String,
    val waterTempC: Double?
) {


    fun isOutdated(): Boolean {
        return nextIssue.parseAsGmtIsoDate()!!.before(currentTime())
    }

}

/**
 * Takes a list of LocationForecast and lets us find the current Air Temp (if present)
 */
fun List<OceanForecast>.getCurrentWaterTempC(): Double? {
    return this.getCurrentForecast()?.waterTempC
}


/**
 * Takes a list o OceanForecasts and returns the one representing now, if that would be applicable
 */
fun List<OceanForecast>.getCurrentForecast(): OceanForecast? {
    val now = currentTime()
    val res = this.find { hour ->
        now.liesBetweneInclusive(
            hour.from.parseAsGmtIsoDate()!!,
            hour.to.parseAsGmtIsoDate()!!
        )
    }

    Log.d(TAG, res.toString())
    return res
}


