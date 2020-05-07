package com.example.badeapp.models

data class DisplayedBadested(
    val name: String,
    val info: String,
    val waterTempC: Double,
    val airTempC: Double,
    val icon: Int?,
    val to: String?
)