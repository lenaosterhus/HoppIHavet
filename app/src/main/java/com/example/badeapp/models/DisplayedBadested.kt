package com.example.badeapp.models

data class DisplayedBadested(
    val name: String,
    val info: String,
    val waterTempC: String,
    val airTempC: String,
    val precipitation: String,
    val wind: String,
    val icon: Int?,
    val to: String?,
    val image: Int
)