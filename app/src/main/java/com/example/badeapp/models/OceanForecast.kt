package com.example.badeapp.models

data class OceanForecast( val badestedId : Int,
                             val from: String,
                             val to: String,
                             val nextIssueOcean: String,
                             val waterTempC: Double?
)