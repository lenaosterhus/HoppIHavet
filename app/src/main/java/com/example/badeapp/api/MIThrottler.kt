package com.example.badeapp.api


import android.os.SystemClock.uptimeMillis
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * This object makes sure we respect Meteorology Institute wishes not to spam their servers.
 * If they give certain response codes they want us to throttle or halt requests.
 */
object MIThrottler {
    val TAG = "MIThrottler"


    private const val stoppTimeMin = 10
    private var stoppStart: Long? = null

    private val _hasHalted = MutableLiveData<Boolean>()
    val hasHalted: LiveData<Boolean> = _hasHalted


    /**
     * This variable/function tells if the traffic has been requested to halt. This is used to
     * determine if requests COULD happen.
     */
    fun hasStopped(): Boolean {
        return _hasHalted.value ?: false
    }


    private fun halt() {
        stoppStart = uptimeMillis()
        _hasHalted.value = true
    }

    fun resume() {
        stoppStart?.let {
            _hasHalted.value = (it + stoppTimeMin * 60000) < uptimeMillis()
        }
    }


    /**
     * When doing request we can get two relevant codes. 203 = reduce traffic, and 429 = halt
     * traffic. This function handles these two response codes.
     */
    fun submitCode(code: Int) {
        require(code in 0..599)

        when (code) {
            200 -> return //All good
            203 -> halt() //Plz slow down
            429 -> {          //If we dont stop now, we are banned
                halt()
            }
            403 -> Log.e("MI-BAN-HAMMER!!", "We are banned from MI!")
            //@TODO log if there was a 404, or any unknown response.
        }


    }


}

