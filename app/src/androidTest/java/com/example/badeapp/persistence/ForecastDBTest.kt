package com.example.badeapp.persistence

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.badeapp.models.Hovedoya
import com.example.badeapp.models.LocationForecast
import com.example.badeapp.models.OceanForecast
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.inTheFutureFromNow
import com.example.badeapp.util.toGmtIsoString
import junit.framework.TestCase.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

private const val TAG = "ForecastDB-Test"

class ForecastDBTest {

    lateinit var DB: ForecastDB

    val OF1 = OceanForecast(
        Hovedoya.id,
        currentTime().toGmtIsoString(),
        inTheFutureFromNow(2).toGmtIsoString(),
        inTheFutureFromNow(10).toGmtIsoString(),
        42.0
    )

    val LF1 = LocationForecast(
        Hovedoya.id,
        OF1.from,
        OF1.to,
        inTheFutureFromNow(20).toGmtIsoString(),
        20.0,
        1,
        1123132123.0,
        windDirection = "N",
        windSpeedMps = 2123123.0,
        windSpeedName = "Noe"
    )

    val LF1_2 = LocationForecast(
        Hovedoya.id,
        OF1.from,
        OF1.to,
        inTheFutureFromNow(20).toGmtIsoString(),
        20.0,
        2,
        1123132123.0,
        windDirection = "N",
        windSpeedMps = 2123123.0,
        windSpeedName = "Noe"
    )




    @Before
    fun startDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        DB = Room.inMemoryDatabaseBuilder(context, ForecastDB::class.java).build()
    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        DB.close()
    }

    @Test
    fun insertOceanData() {
        DB.forecastDao().newOceanForecast(listOf(OF1))
        DB.forecastDao().putBadested(Hovedoya)
        val forecast = DB.forecastDao().getAllRaw()
        Log.d(TAG, forecast.size.toString())
        assertTrue(forecast.size == 1)
        assertNotNull(forecast[0].forecast)
        assertTrue(forecast[0].forecast!!.contains(OF1))
        assertTrue(forecast[0].forecast!!.hasOceanData())
    }

    @Test
    fun insertLocationData() {
        DB.forecastDao().newLocationForecast(listOf(LF1))
        DB.forecastDao().putBadested(Hovedoya)
        val forecast = DB.forecastDao().getAllRaw()
        assertTrue(forecast.size == 1)
        assertNotNull(forecast[0].forecast)
        assertTrue(forecast[0].forecast!!.contains(LF1))
        assertTrue(forecast[0].forecast!!.hasLocationData())
    }

    @Test
    fun insertLocationDataAndOceanData() {
        DB.forecastDao().newLocationForecast(listOf(LF1))
        DB.forecastDao().newOceanForecast(listOf(OF1))
        DB.forecastDao().putBadested(Hovedoya)
        val forecast = DB.forecastDao().getAllRaw()
        assertTrue(forecast.size == 1)
        assertNotNull(forecast[0].forecast)
        assertTrue(forecast[0].forecast!!.contains(LF1))
        assertTrue(forecast[0].forecast!!.contains(OF1))
        assertTrue(forecast[0].forecast!!.hasLocationData())
        assertTrue(forecast[0].forecast!!.hasOceanData())
    }

    @Test
    fun updateLocationData() {
        DB.forecastDao().newLocationForecast(listOf(LF1))
        DB.forecastDao().newLocationForecast(listOf(LF1_2))
        DB.forecastDao().putBadested(Hovedoya)
        val forecast = DB.forecastDao().getAllRaw()
        assertTrue(forecast.size == 1)
        assertNotNull(forecast[0].forecast)
        assertFalse(forecast[0].forecast!!.contains(LF1))
        assertTrue(forecast[0].forecast!!.contains(LF1_2))
        assertTrue(forecast[0].forecast!!.hasLocationData())
    }


}