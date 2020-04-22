package com.example.badeapp.models

/**
 * This class represent the forecast for a badested at a certain time (as of now
 * only the present weather).
 */
class BadestedForecast(
    var airTempC: Double? = null,
    var waterTempC: Double? = null,
    var symbol: Int? = null
)

