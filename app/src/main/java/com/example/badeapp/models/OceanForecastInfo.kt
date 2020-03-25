package com.example.badeapp.models

data class OceanForecastInfo(val vannTempC: Double?) {

    fun isOutdated(): Boolean {
        return minUnitlOutdated() == 0.0
    }

    fun minUnitlOutdated(): Double {
        //@TODO make it return time until next potential update.
        return 0.0
    }

}