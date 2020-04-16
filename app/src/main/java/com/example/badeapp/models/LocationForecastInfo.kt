package com.example.badeapp.models

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

private val TAG = "DEBUG - LocationInfo"

data class LocationForecastInfo(val luftTempC: Double?, val symbol: Int?, val nextIssue: String?) {

    // Format timePosition: "2020-04-02T14:00:00Z"
    fun isOutdated(): Boolean {
        return minUntilOutdated() < 0L
    }

    fun minUntilOutdated(): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.GERMANY)
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
//        Log.d(TAG, "nextIssue: $nextIssue")

        nextIssue?.let {
            val updateTime: Date? = dateFormat.parse(nextIssue)
            val currentTime = Date()
//            Log.d(TAG, "updateTime: $updateTime")
//            Log.d(TAG, "currentTime: $currentTime")

            updateTime?.let {
                val diff = updateTime.time - currentTime.time // millisek
                val diffMin = diff / (1000 * 60) // min
//            Log.d(TAG, "diffMin: $diffMin")

                return diffMin
            }
        }
        return -1
    }


}