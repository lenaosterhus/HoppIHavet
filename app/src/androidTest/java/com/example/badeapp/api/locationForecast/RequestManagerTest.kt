package com.example.badeapp.api.locationForecast


import com.example.badeapp.models.Hovedoya
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
}