package com.example.badeapp.api


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.inTheFutureFromNow
import java.util.*

/**
 * This object makes sure we respect Meteorology Institute wishes not to spam their servers.
 * If they give certain response codes they want us to throttle or halt requests.
 *
 */
object MIThrottler {

    private const val TAG = "MIThrottler"

    private const val stopTimeMin = 10L
    private var stopUntil: Date? = null

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
        stopUntil = inTheFutureFromNow(stopTimeMin)
        _hasHalted.value = true
    }

    fun tryToResume() {
        stopUntil?.let {
            _hasHalted.value = it.before(currentTime())
        }
    }


    /**
     * When doing request we can get two relevant codes. 203 = reduce traffic, and 429 = halt
     * traffic. This function handles these two response codes.
     */
    fun submitCode(code: Int) {
        require(code in 0..599)

        when (code) {
            200 -> return     //All good
            203 -> halt()     //Plz slow down
            429 -> {          //If we dont stop now, we are banned
                halt()
            }
            403 -> Log.e("MI-BAN-HAMMER!!", "We are banned from MI!")
            else -> Log.i(TAG,"Got response code $code, that MIThrotteler.kt does not understand.")
        }
    }
}

