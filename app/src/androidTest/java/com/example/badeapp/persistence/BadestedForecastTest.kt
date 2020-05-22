package com.example.badeapp.persistence

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.badeapp.models.Hovedoya
import com.example.badeapp.models.LocationForecast
import com.example.badeapp.models.OceanForecast
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.inTheFutureFromNow
import com.example.badeapp.util.toGmtIsoString
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class BadestedForecastTest {

    lateinit var DB: ForecastDB

    val OF1 = OceanForecast(
        Hovedoya.id,
        inTheFutureFromNow(4).toGmtIsoString(),
        inTheFutureFromNow(50).toGmtIsoString(),
        inTheFutureFromNow(60).toGmtIsoString(),
        42.0,
        currentTime().toGmtIsoString()
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
        windSpeedName = "Noe",
        createdLocation = currentTime().toGmtIsoString()
    )




    @Before
    fun startDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        DB = Room.inMemoryDatabaseBuilder(context, ForecastDB::class.java).build()
        DB.forecastDao().newLocationForecast(listOf(LF1))
        DB.forecastDao().newOceanForecast(listOf(OF1))
        DB.forecastDao().putBadested(Hovedoya)
    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        //DB.close()
    }

    /**
     * We want our db, to get the current forecast, but we define current
     * to be 5 min in the future. We therefor must be able to get LF1 & OF1
     * from the DB.
     */
    @Test
    fun getForecast5minInTheFuture() {
        val forecast  = DB.forecastDao().getAllCurrentRaw()
        assertNotNull(forecast)
        assertTrue(forecast.isNotEmpty())
        assertNotNull(forecast[0].forecast)
        assertTrue(forecast[0].forecast!!.contains(LF1))
        assertTrue(forecast[0].forecast!!.contains(OF1))

    }


}