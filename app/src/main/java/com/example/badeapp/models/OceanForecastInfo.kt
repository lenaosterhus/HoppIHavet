package com.example.badeapp.models

data class OceanForecastInfo(val luftTempC: Double?, val symbol: Int?) {

    fun isOutdated(): Boolean {
        return minUnitlOutdated() == 0.0
    }

    fun minUnitlOutdated(): Double {
        return 0.1
    }

}