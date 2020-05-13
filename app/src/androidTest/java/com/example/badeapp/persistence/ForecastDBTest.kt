package com.example.badeapp.persistence

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.badeapp.models.Hovedoya
import com.example.badeapp.models.OceanForecast
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.inTheFutureFromNow
import com.example.badeapp.util.toGmtIsoString
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ForecastDBTest {

    lateinit var DB: ForecastDB

    val OF1 = OceanForecast(
        Hovedoya,
        currentTime().toGmtIsoString(),
        inTheFutureFromNow(2).toGmtIsoString(),
        inTheFutureFromNow(10).toGmtIsoString(),
        42.0
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
        DB.forecastDao().newOceanForecast(listOf(OF1));
        val forecast = DB.forecastDao().getAllRaw();
        assertTrue(forecast != null)
        assertTrue(forecast.size == 1)
        assertTrue(forecast[0].contains(OF1))
    }

    @Test
    fun badestedSummaryDao() {
    }
}