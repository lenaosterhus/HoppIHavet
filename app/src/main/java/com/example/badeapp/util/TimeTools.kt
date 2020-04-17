package com.example.badeapp.util

import java.text.SimpleDateFormat
import java.util.*

val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("no","NO")).also {
    it.timeZone = TimeZone.getTimeZone("GMT")
}

/**
 * When working with our data source from MI, we get time in ISO8601 GMT time, and we need
 * to do time calculations for these values. This function takes two strings formatted in the right
 * way and returns the time delta.
 */
fun minBetwene(from:String,to:String) : Long? {
    val toDate = DATE_FORMAT.parse(to) ?: return null
    val fromDate = DATE_FORMAT.parse(from) ?: return null
    return (toDate.time - fromDate.time)/(60 * 1000) //Convert from mill to min
}

