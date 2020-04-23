package com.example.badeapp.api.LocationForecastTest

import android.util.Log
import com.example.badeapp.repository.Badested
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
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
            val response = RequestManager.requestRaw(
                sted.lat,
                sted.lon,
                SESSION
            )
            Log.d(TAG, response.body().toString())

            if (!response.isSuccessful) {
                response.errorBody()?.string()?.let {
                    Log.d("$TAG error_body", it)
                }
                Log.d("$TAG error_code", "Error code: ${response.code()}")
                assertTrue(false)
            }

        }


    }

}