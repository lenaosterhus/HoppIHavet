package com.example.badeapp.models

import com.example.badeapp.util.currentTime
import com.example.badeapp.util.minBetween


private const val TAG = "DEBUG - OceanInfo"

data class OceanForecastInfo(val vannTempC: Double?, val nextIssue: String) {

    // Format timePosition: "2020-04-02T14:00:00Z"
    fun isOutdated(): Boolean {
        return minUntilOutdated() < 10L
    }

    fun minUntilOutdated(): Long {
//        Log.d(TAG, "nextIssue: $nextIssue")
        return minBetween(currentTime(), nextIssue)!!
    }

}