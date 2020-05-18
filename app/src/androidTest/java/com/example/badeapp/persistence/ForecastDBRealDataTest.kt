package com.example.badeapp.persistence


import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.badeapp.models.Badested
import com.example.badeapp.models.LocationForecast
import com.example.badeapp.models.OceanForecast
import com.example.badeapp.models.alleBadesteder
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.badeapp.api.locationForecast.RequestManager as LFRM
import com.example.badeapp.api.oceanForecast.RequestManager as OFRM

class ForecastDBRealDataTest {

    var DB: ForecastDB? = null
    val TAG = "BadestedSummaryTest"

    val lfl: MutableList<LocationForecast> = mutableListOf()
    val ofl: MutableList<OceanForecast> = mutableListOf()


    //We can observe livedata indefinably
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        if (DB == null) {
            val context = ApplicationProvider.getApplicationContext<Context>()
            DB = Room.inMemoryDatabaseBuilder(context, ForecastDB::class.java).build()

            runBlocking {
                alleBadesteder.forEach { badested ->
                    //Put badested in the DB
                    DB!!.forecastDao().putBadested(badested)

                    //Download and insert data for location forecast
                    LFRM.request(badested)?.let {
                        lfl.addAll(it)
                        DB!!.forecastDao().newLocationForecast(it)
                        println("adding location forecast for $badested")
                    }

                    //Download and insert data from ocean forecast
                    OFRM.request(badested)?.let {
                        ofl.addAll(it)
                        DB!!.forecastDao().newOceanForecast(it)
                        println("adding ocean forecast for $badested")
                    }
                }
            }
        }
    }

    @After
    fun tearDown() {
        //DB?.close()
    }

    @Test
    fun testRequestValidity() {
        val missingLocal = alleBadesteder.filter { badested ->
            lfl.find { it.badestedId == badested.badestedId } == null
        }
        assertEquals(emptyList<Badested>(), missingLocal)

        val missingOcean = alleBadesteder.filter { badested ->
            ofl.find { it.badestedId == badested.badestedId } == null
        }
        assertEquals(emptyList<Badested>(), missingOcean)
    }

    @Test
    fun checkGettingDataRaw() {
        val forecasts = DB!!.forecastDao().getAllCurrentRaw()
        assertNotNull(forecasts)
        assertTrue(forecasts.isNotEmpty())

        //Check that every badested has a entry
        assertEquals(alleBadesteder.size, forecasts.size)
        alleBadesteder.forEach { badested ->
            assertTrue(forecasts.any {
                it.badested.badestedId == badested.badestedId
            })
        }

        //Check that every badested forecast has forecast data
        forecasts.forEach {
            println("BadestedForecast-Test-Real-Data $it")
        }

        forecasts.forEach { forecast ->
            assertNotNull(forecast.forecast)
            if (!forecast.forecast[0].hasLocationData()) {
                println("FAILED: ${forecast}")

                val locResponse = lfl.find {
                    it.badestedId == forecast.badested.badestedId
                }
                val oceanResponse = ofl.find {
                    it.badestedId == forecast.badested.badestedId
                }

                if (locResponse == null) {
                    println("Location reponse is missing!")
                } else {
                    println("$locResponse")
                }

                if (oceanResponse == null) {
                    println("Ocean reponse is missing!")
                } else {
                    println("$oceanResponse")
                }
                println("-----------------------------------------\n\n")
            } else {
                println("Succsess!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            }
            //assertTrue(it.forecast!!.hasOceanData())
            //assertTrue(it.forecast!!.hasLocationData())

        }

    }


}

