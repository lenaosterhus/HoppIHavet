package com.example.badeapp.persistence

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.badeapp.models.BadestedSummary
import com.example.badeapp.models.Hovedoya
import com.example.badeapp.models.LocationForecast
import com.example.badeapp.models.OceanForecast
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.getOrAwaitValue
import com.example.badeapp.util.inTheFutureFromNow
import com.example.badeapp.util.toGmtIsoString
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class BadestedSummaryTest {

    lateinit var DB: ForecastDB

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val OF1 = OceanForecast(
        Hovedoya,
        currentTime().toGmtIsoString(),
        inTheFutureFromNow(5).toGmtIsoString(),
        inTheFutureFromNow(10).toGmtIsoString(),
        42.0
    )

    val LF1 = LocationForecast(
        Hovedoya,
        OF1.from,
        OF1.to,
        OF1.nextIssue,
        20.0,
        1
    )

    val BS1 = BadestedSummary(
        Hovedoya,
        OF1.from,
        OF1.to,
        OF1.waterTempC,
        LF1.airTempC,
        LF1.symbol
    )


    @Before
    fun startDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        DB = Room.inMemoryDatabaseBuilder(context, ForecastDB::class.java).build()
        DB.oceanForecastDao().insert(OF1)
        DB.locationForecastDao().insert(LF1)
    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        DB.close()
    }


    @Test
    fun simpleGet() {
        val res = DB.badestedSummaryDao().getAllRaw(Hovedoya)
        assertNotNull(res)
        assertEquals(1, res.size)
        assertEquals(1, res[0].symbol)
    }

    @Test
    fun getAllCurrentRaw() {
        val res = DB.badestedSummaryDao().getAllCurrentRaw()
        assertNotNull(res)
        assertEquals(1, res.size)
        assertEquals(BS1, res[0])


    }


    @Test
    fun liveDataGet() {
        val res = DB.badestedSummaryDao().getAllCurrent()

        DB.oceanForecastDao().insert(OF1)
        DB.locationForecastDao().insert(LF1)


        assertEquals(listOf(BS1), res.getOrAwaitValue(2))

    }

}