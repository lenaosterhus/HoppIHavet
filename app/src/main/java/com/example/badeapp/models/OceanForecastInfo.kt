package com.example.badeapp.models

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

private val TAG = "DEBUG - OceanInfo"

data class OceanForecastInfo(val vannTempC: Double?, val nextIssue: String?) {

    // Format timePosition: "2020-04-02T14:00:00Z"
    fun isOutdated(): Boolean {
        return minUntilOutdated() < 0L
    }

    fun minUntilOutdated(): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        nextIssue?.let {
            val updateTime: Date = dateFormat.parse(nextIssue)
            val currentTime = Date()

            val diff = updateTime.time - currentTime.time // millisek
            val diffMin = diff / (1000 * 60) // min

            return diffMin
        }
        return 0
    }

}