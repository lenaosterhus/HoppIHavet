package com.example.badeapp.persistence

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.badeapp.models.Hovedoya
import com.example.badeapp.models.OceanForecast
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.inTheFutureFromNow
import com.example.badeapp.util.toGmtIsoString
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
        DB.oceanForecastDao().insert(OF1)
    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        DB.close()
    }


    @Test
    fun locationForecastDao() {
    }

    @Test
    fun badestedSummaryDao() {
    }
}