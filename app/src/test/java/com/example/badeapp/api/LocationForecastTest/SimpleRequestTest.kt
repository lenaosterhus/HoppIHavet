package com.example.badeapp.api.LocationForecastTest

import com.example.badeapp.repository.Badested
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test


class SimpleRequestTest {

    val TAG = "SR-TEST"
    val SESSION = "SimpleRequestTest"

    val badesteder = Badested::class.nestedClasses.map {
        it.objectInstance as Badested
    }

    @Test
    fun simpleRequest() {

        val sted = badesteder.find { badested -> badested.name.toLowerCase() == "hovedøya" }!!

        runBlocking {
            val response = RequestManager.request(SESSION, sted.lat, sted.lon)
            assertNotNull(response)
            if (response == null) return@runBlocking
            assertEquals("2020-04-22T14:11:08Z", response.nextIssue)
        }


    }

}