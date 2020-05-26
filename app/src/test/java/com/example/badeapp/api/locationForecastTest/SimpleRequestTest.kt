package com.example.badeapp.api.locationForecastTest

import android.util.Log
import com.example.badeapp.models.Gressholmen
import com.example.badeapp.models.Hovedoya
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test


class SimpleRequestTest {

    val TAG = "SR-TEST"
    val SESSION = "SimpleRequestTest"



    @Test
    fun simpleRequest() {

        val sted = Hovedoya

        runBlocking {
            val responseRaw = RequestManager.requestRaw(
                sted,
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
        val sted = Gressholmen

        runBlocking {
            try {
                //We get a reponse containing '{{}'. This is not valid json and should not be
                //possible to parse.
                val responseRaw = RequestManager.requestRaw(
                    sted,
                    SESSION
                )

                Log.d(TAG, responseRaw.body() ?: responseRaw.errorBody().toString())

                //Tries to parse the response "{{}", that should not result in anything
                val response = RequestManager.request(
                    sted,
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