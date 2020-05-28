package com.example.badeapp.util

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

const val dateFormatStringIso = "yyyy-MM-dd'T'HH:mm:ss'Z'"

fun getHour(date: String) : String? {
    return date.parseAsGmtIsoDate()?.toHourString()
}

/*
 * When working with our data source from MI, we get time in ISO8601 GMT time, and we need
 * to do time calculations for these values. This function takes two strings formatted in the right
 * way and returns the time delta.
 */

fun minBetween(from: String, toDate: Date): Long? {
    val dateFormat = SimpleDateFormat(
        dateFormatStringIso,
        Locale("no", "NO")
    ).also {
        it.timeZone = TimeZone.getTimeZone("GMT")
    }
    val fromDate = dateFormat.parse(from) ?: return null
    return minBetween(fromDate, toDate)
}

fun minBetween(fromDate: Date, to: String): Long? {
    val dateFormat = SimpleDateFormat(
        dateFormatStringIso,
        Locale("no", "NO")
    ).also {
        it.timeZone = TimeZone.getTimeZone("GMT")
    }
    val toDate = dateFormat.parse(to) ?: return null
    return minBetween(fromDate, toDate)
}

fun minBetween(from: String, to: String): Long? {
    val dateFormat = SimpleDateFormat(
        dateFormatStringIso,
        Locale("no", "NO")
    ).also {
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
    val dateFormat = SimpleDateFormat(
        dateFormatStringIso,
        Locale("no", "NO")
    ).also {
        it.timeZone = TimeZone.getTimeZone("GMT")
    }
    return dateFormat.format(this)
}

fun Date.toHourString(): String {
    val dateFormat = SimpleDateFormat(
        "HH:mm",
        Locale("no", "NO")
    ).also {
        it.timeZone = TimeZone.getTimeZone("Europe/Oslo")
    }
    return dateFormat.format(this)
}


fun String.parseAsGmtIsoDate(): Date? {
    val dateFormat = SimpleDateFormat(
        dateFormatStringIso,
        Locale("no", "NO")
    ).also {
        it.timeZone = TimeZone.getTimeZone("GMT")
    }
    return try {
        dateFormat.parse(this)
    } catch (e: Exception) {
        null
    }
}

/**
 * Returns true if the given date lies between (inclusive) before and after.
 * @return before <= it <= after
 */
fun Date.liesBetweenInclusive(before: Date, after: Date): Boolean {
    val low = before.time == this.time || this.after(before)
    val high = after.time == this.time || this.before(after)
    return low && high
}


fun Date.addOneHour(): Date {
    return Calendar.getInstance().also {
        it.time = this
        it.add(Calendar.HOUR, 1)
    }.time
}


fun Date.subOneHour(): Date {
    return Calendar.getInstance().also {
        it.time = this
        it.add(Calendar.HOUR, -1)
    }.time
}

/**
 * This function takes a Date, and says if the time we would expect daylight in Oslo.
 */
fun Date.daylightInOslo(): Boolean {

    val c = Calendar.getInstance()
    c.time = this
    val month = c.get(Calendar.MONTH)
    val min = c.get(Calendar.MINUTE) + ((c.get(Calendar.HOUR_OF_DAY) + 2) % 24) * 60

    when (month) {
        1 -> return 9 * 60 + 1 < min && min < 15 * 60 + 50
        2 -> return 7 * 60 + 53 < min && min < 17 * 60 + 8
        3 -> return 6 * 60 + 29 < min && min < 18 * 60 + 21
        4 -> return 5 * 60 + 56 < min && min < 20 * 60 + 37
        5 -> return 4 * 60 + 35 < min && min < 21 * 60 + 59
        6 -> return 3 * 60 + 50 < min && min < 22 * 60 + 44
        7 -> return 4 * 60 + 19 < min && min < 22 * 60 + 26
        8 -> return 5 * 60 + 30 < min && min < 21 * 60 + 11
        9 -> return 6 * 60 + 44 < min && min < 19 * 60 + 39
        10 -> return 7 * 60 + 55 < min && min < 18 * 60 + 10
        11 -> return 8 * 60 + 13 < min && min < 15 * 60 + 50
        12 -> return 9 * 60 + 10 < min && min < 15 * 60 + 14
    }
    return true
}


/**
 * ----------------------------------------------------------------------------------
 * Converter used by Room, to convert Date -> Iso String
 * and also back into Date.
 */

object IsoGmtConverter {

    @TypeConverter
    @JvmStatic
    fun toDate(value: String?): Date? {
        return value?.parseAsGmtIsoDate()
    }

    @TypeConverter
    @JvmStatic
    fun toString(value: Date?): String? {
        return value?.toGmtIsoString()
    }

}

