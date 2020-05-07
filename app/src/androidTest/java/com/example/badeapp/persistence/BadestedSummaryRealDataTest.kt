package com.example.badeapp.persistence


import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.badeapp.models.Hovedoya
import com.example.badeapp.persistence.ForecastDB
import com.example.badeapp.util.getOrAwaitValue
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.badeapp.api.LocationForecast.RequestManager as LFRM
import com.example.badeapp.api.OceanForecast.RequestManager as OFRM

class BadestedSummaryRealDataTest {

    var DB: ForecastDB? = null
    val TAG = "BadestedSummaryTest"

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        if (DB == null) {
            val context = ApplicationProvider.getApplicationContext<Context>()
            DB = Room.inMemoryDatabaseBuilder(context, ForecastDB::class.java).build()

            //Download and insert data for location forecast
            runBlocking {
                LFRM.request(Hovedoya)?.let { DB!!.locationForecastDao().replaceAll(it) }
            }

            //Download and insert data from ocean forecast
            runBlocking {
                OFRM.request(Hovedoya)?.let { DB!!.oceanForecastDao().replaceAll(it) }
            }
        }
    }

    @After
    fun tearDown() {
    }

    @Test
    fun checkGettingDataRaw() {
        val ofl = DB!!.oceanForecastDao().getAllForecast(Hovedoya)
        assertNotNull(ofl)
        assertTrue(ofl!!.isNotEmpty())

        val lfl = DB!!.locationForecastDao().getAllForecast(Hovedoya)
        assertNotNull(lfl)
        assertTrue(lfl!!.isNotEmpty())


        val ocur = DB!!.oceanForecastDao().getCurrent(Hovedoya)
        assertNotNull(ocur)

        val lcur = DB!!.locationForecastDao().getCurrent(Hovedoya)
        assertNotNull(lcur)


        assertNotNull(ocur!!.badested)
        assertNotNull(lcur!!.badested)
        assertEquals(lcur.badested, ocur.badested)


        assertNotNull(ocur.from)
        assertNotNull(lcur.from)
        assertEquals(lcur.from, ocur.from)

        assertNotNull(ocur.to)
        assertNotNull(lcur.to)
        assertEquals(lcur.to, ocur.to)


        val scurs = DB!!.badestedSummaryDao().getAllCurrentRaw()
        assertEquals(1, scurs.size)
        val scur = scurs.find { it -> it.badested == Hovedoya }
        assertNotNull(scur)

    }

    @Test
    fun checkGettingLiveData() {
        val ofl = DB!!.oceanForecastDao().getAllForecastsLive(Hovedoya)
        assertNotNull(ofl)

        val lfl = DB!!.locationForecastDao().getAllForecastsLive(Hovedoya)
        assertNotNull(lfl)


        val ofv = ofl.getOrAwaitValue(3) //Must run on main
        val lfv = lfl.getOrAwaitValue(3) //

        assertNotNull(ofv)
        assertNotNull(lfv)

    }
}