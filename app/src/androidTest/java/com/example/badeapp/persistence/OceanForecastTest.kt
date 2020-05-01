package com.example.badeapp.persistence


import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.badeapp.models.Hovedoya
import com.example.badeapp.models.OceanForecast
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.getOrAwaitValue
import com.example.badeapp.util.inTheFutureFromNow
import com.example.badeapp.util.toGmtIsoString
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

/**
 * In this class we test the DAO, aka the sql querys for the ocean forecast dao.
 */

class OceanForecastTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

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
    fun simpleInsertGet() {

        //Insert value
        DB.oceanForecastDao().insert(OF1)

        //Get value
        var res = DB.oceanForecastDao().getAllForecast(Hovedoya)
        assertTrue(res != null)
        assertTrue(res!!.size == 1)
        assertTrue(res[0] == OF1)

        //Delete all the values
        DB.oceanForecastDao().removeForecasts(Hovedoya)

        //Assert we cant get it
        res = DB.oceanForecastDao().getAllForecast(Hovedoya)
        assertTrue(res != null)
        assertTrue(res!!.size == 0)


    }

    @Test
    fun getBetween() {
        val from = OF1.from
        val to = inTheFutureFromNow(10).toGmtIsoString()

        DB.oceanForecastDao().insert(OF1)

        val res = DB.oceanForecastDao().getBetween(Hovedoya, from, to)
        assertNotNull(res)
        assertTrue(res!!.size == 1)
        assertEquals(OF1, res[0])
    }


    @Test
    fun getCurrent() {
        //Insert
        DB.oceanForecastDao().insert(OF1)
        val res = DB.oceanForecastDao().getCurrent(Hovedoya)
        assertEquals(OF1, res)
    }


    @Test
    fun getFromLiveData() {

        DB.oceanForecastDao().insert(OF1)
        val raw = DB.oceanForecastDao().getAllForecast(Hovedoya)
        assertEquals(OF1, raw!![0])


        val res = DB.oceanForecastDao().getAllForecastsLive(Hovedoya)
        val value = res.getOrAwaitValue(2)
        assertEquals(OF1, value!![0])


    }
}