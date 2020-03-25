package com.example.badeapp.models


data class LocationForecastInfo(val luftTempC: Double?, val symbol: Int?) {

    fun isOutdated(): Boolean {
        return minUnitlOutdated() == 0.0
    }

    fun minUnitlOutdated(): Double {
        //TODO implement this feature, for å gjøre dette må du jobbe i api mappen for å hente
        //ut informasjonen som trengs.
        return 0.0
    }


}