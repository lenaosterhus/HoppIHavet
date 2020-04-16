package com.example.badeapp.util

import java.text.SimpleDateFormat
import java.util.*

val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.GERMANY).also {
    it.timeZone = TimeZone.getTimeZone("GMT")
}