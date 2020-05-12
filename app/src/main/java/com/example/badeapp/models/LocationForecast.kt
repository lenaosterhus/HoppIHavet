package com.example.badeapp.models

data class LocationForecast( val badestedId : Int,
                             val from: String,
                             val to: String,
                             val nextIssueLocation: String,
                             val airTempC: Double?,
                             val symbol: Int?,
                             val precipitation: Double?,
                             val windDirection: String?,
                             val windSpeedMps: Double?,
                             val windSpeedName: String?
) {

}