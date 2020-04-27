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
            val responseRaw = RequestManager.requestRaw(
                sted.lat,
                sted.lon,
                SESSION
            )
            if (!responseRaw.isSuccessful) {
                assertTrue(false)
            }

            //If request had no error, then we know the headers are properly set
        }
    }

    /**
     * This test checks what happens if we try to request malformed json.
     */
    @Test
    fun badResponse() {
        val sted = badesteder.find { badested -> badested.name.toLowerCase() == "gressholmen" }!!

        runBlocking {
            try {
                //We get a reponse containing '{{}'. This is not valid json and should not be
                //possible to parse.
                val responseRaw = RequestManager.requestRaw(
                    sted.lat,
                    sted.lon,
                    SESSION
                )

                Log.d(TAG, responseRaw.body() ?: responseRaw.errorBody().toString())

                //Tries to parse the response "{{}", that should not result in anything
                val response = RequestManager.request(
                    sted.lat,
                    sted.lon,
                    SESSION,
                    "",
                    false,
                    false
                )

                assertTrue(response == null)
            } catch (exs: Exception) {
                Log.d(TAG, exs.toString())
                assertTrue(false) //We don't want any exception to be thrown.
            }
        }
    }

}