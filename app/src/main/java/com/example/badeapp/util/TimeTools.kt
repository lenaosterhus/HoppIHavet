package com.example.badeapp.util

import java.text.SimpleDateFormat
import java.util.*


/*
 * When working with our data source from MI, we get time in ISO8601 GMT time, and we need
 * to do time calculations for these values. This function takes two strings formatted in the right
 * way and returns the time delta.
 */

fun minBetween(from: String, toDate: Date): Long? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("no", "NO")).also {
        it.timeZone = TimeZone.getTimeZone("GMT")
    }
    val fromDate = dateFormat.parse(from) ?: return null
    return minBetween(fromDate, toDate)
}

fun minBetween(fromDate: Date, to: String): Long? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("no", "NO")).also {
        it.timeZone = TimeZone.getTimeZone("GMT")
    }
    val toDate = dateFormat.parse(to) ?: return null
    return minBetween(fromDate, toDate)
}

fun minBetween(from: String, to: String): Long? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("no", "NO")).also {
        it.timeZone = TimeZone.getTimeZone("GMT")
    }

    val toDate = dateFormat.parse(to) ?: return null
    val fromDate = dateFormat.parse(from) ?: return null
    return minBetween(fromDate, toDate)
}

fun minBetween(from: Date, to: Date): Long {
    return (to.time - from.time).div(60L * 1000L) //Convert from mill to min
}


// --------------------------------------------------------------------------------

/**
 * Returns a date that is min minutes in the future from now
 */
fun inTheFutureFromNow(min: Long): Date {
    return Date(System.currentTimeMillis() + min * 60 * 1000)
}

/**
 * Returns a  string representation of current time
 */
fun currentTime(): Date {
    return inTheFutureFromNow(0)
}

// -------------------------------------------------------------------

/**
 * Extends date objects to return string representations of gmt iso time.
 */
fun Date.toGmtIsoString(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("no", "NO")).also {
        it.timeZone = TimeZone.getTimeZone("GMT")
    }
    return dateFormat.format(this)
}


fun String.parseAsGmtIsoDate(): Date? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("no", "NO")).also {
        it.timeZone = TimeZone.getTimeZone("GMT")
    }
    try {
        return dateFormat.parse(this)
    } catch (e: Exception) {
        return null
    }
}

/**
 * Returns true if the given date lies betwene (inclusive) before and after.
 * @return before <= it <= after
 */
fun Date.liesBetweneInclusive(before: Date, after: Date): Boolean {
    val low = before.time == this.time || this.after(before)
    val high = after.time == this.time || this.before(after)
    return low && high
}


