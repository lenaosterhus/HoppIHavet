package com.example.badeapp.models

/**
 * This class is roughly a subclass of Forecast. Although not a subtype, it does have a strict
 * subset of the values that the Forecast class has. This model represents the part of a forecast
 * that the LocationForecast API gets us.
 */
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
)