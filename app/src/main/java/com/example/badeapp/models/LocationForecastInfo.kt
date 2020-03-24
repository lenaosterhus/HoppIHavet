package com.example.badeapp.models


data class LocationForecastInfo(val luftTempC: Double?, val symbol: Int?) {

    fun isOutdated(): Boolean {
        return minUnitlOutdated() == 0.0
    }

    fun minUnitlOutdated(): Double {
        //TODO implement this feature, for å gjøre dette må du
        return 0.0
    }


}