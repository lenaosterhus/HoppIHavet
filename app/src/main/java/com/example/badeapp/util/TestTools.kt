package com.example.badeapp.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.badeapp.models.Forecast
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


fun <T> LiveData<T>.getOrAwaitValue(time: Long = 2): T? {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, TimeUnit.SECONDS)) {
        if (this.value != null)
            return this.value
        else
            throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data
}

// Takes a list of Forecasts and returns the one representing now, if that would be applicable
fun List<Forecast>.getCurrentForecast(): Forecast? {
    val now = currentTime()
    return this.find { hour ->
        now.liesBetweenInclusive(
            hour.from.parseAsGmtIsoDate()!!,
            hour.to.parseAsGmtIsoDate()!!
        )
    }
}

// Takes a list of Forecast and lets us find the current Air Temp (if present)
fun List<Forecast>.getCurrentAirTempC(): Double? {
    return this.getCurrentForecast()?.airTempC
}

// Takes a list of Forecast and lets us find the current Symbol (if present)
fun List<Forecast>.getCurrentIcon(): Int? {
    return this.getCurrentForecast()?.getIcon()
}

// Takes a list of Forecast and lets us find the current Water Temp (if present)
fun List<Forecast>.getCurrentWaterTempC(): Double? {
    return this.getCurrentForecast()?.waterTempC
}


