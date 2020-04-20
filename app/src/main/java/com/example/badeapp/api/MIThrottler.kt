package com.example.badeapp.api


import android.os.SystemClock.uptimeMillis
import android.util.Log

/**
 * This object makes sure we respect Meteorology Institute wishes not to spam their servers.
 * If they give certain response codes they want us to throttle or halt requests.
 */
object MIThrottler {

    private const val throttleTimeMin = 10
    private var throttledStart: Long? = null

    private const val stoppTimeMin = 10
    private var stoppStart: Long? = null

    /**
     * This variable/function tells if the traffic has been requested to be reduced. Not stopped,
     * just reduced. This should be used as part of a triage to determine if data SHOULD (not could)
     * update.
     */
    fun isThrottled(): Boolean {
        throttledStart?.let {
            return (it + throttleTimeMin * 60000) < uptimeMillis()
        }
        return false
    }

    /**
     * This variable/function tells if the traffic has been requested to halt. This is used to
     * determine if requests COULD happen.
     */
    fun hasStopped(): Boolean {
        stoppStart?.let {
            return (it + stoppTimeMin * 60000) < uptimeMillis()
        }
        return false
    }


    private fun halt() {
        stoppStart = uptimeMillis()
    }

    private fun throttle() {
        throttledStart = uptimeMillis()
    }


    /**
     * When doing request we can get two relevant codes. 203 = reduce traffic, and 429 = halt
     * traffic. This function handles these two response codes.
     */
    fun submitCode(code: Int) {
        require(code in 0..599)

        when (code) {
            200 -> return //All good
            203 -> throttle() //Plz slow down
            429 -> {          //If we dont stop now, we are banned
                throttle()
                halt()
            }
            403 -> Log.d("MI-BAN-HAMMER!!", "We are banned from MI!")
            //@TODO log if there was a 404, or any unknown response.
        }



    }


}

