package com.example.badeapp.api.locationForecast


import com.example.badeapp.models.Hovedoya
import com.example.badeapp.models.alleBadesteder
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RequestManagerTest {

    @Test
    fun request() {

        val sted = Hovedoya

        runBlocking {
            val result = RequestManager.request(sted)
            assertNotNull(result)
            assertTrue(!result.isNullOrEmpty())
        }
    }

    /**
     * Test that we can get data for every badested
     */
    @Test
    fun testAlleBadesteder() {

        alleBadesteder.forEach { place ->
            runBlocking {
                val result = RequestManager.request(place)
                assertNotNull(result)
                assertTrue(!result.isNullOrEmpty())
            }
        }
    }
}