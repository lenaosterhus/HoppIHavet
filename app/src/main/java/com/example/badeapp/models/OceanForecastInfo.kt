package com.example.badeapp.models

import android.util.Log
import com.example.badeapp.util.DATE_FORMAT
import java.util.*

private val TAG = "DEBUG - OceanInfo"

data class OceanForecastInfo(val vannTempC: Double?, val nextIssue: String) {

    // Format timePosition: "2020-04-02T14:00:00Z"
    fun isOutdated(): Boolean {
        return minUntilOutdated() < 10L
    }

    fun minUntilOutdated(): Long {
//        Log.d(TAG, "nextIssue: $nextIssue")

        val updateTime: Date? = DATE_FORMAT.parse(nextIssue)
        val currentTime = Date()
//            Log.d(TAG, "updateTime: $updateTime")
//            Log.d(TAG, "currentTime: $currentTime")

        updateTime?.let {
            val diff = updateTime.time - currentTime.time // millisek
            val diffMin = diff / (1000 * 60) // min
//            Log.d(TAG, "diffMin: $diffMin")

            return diffMin
        }
        Log.d(TAG, "updateTime = null")
        return 0
    }

}