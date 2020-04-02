package com.example.badeapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.badeapp.models.LocationForecastInfo
import com.example.badeapp.models.OceanForecastInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import com.example.badeapp.api.LocationForecast.RequestManager as LocationForecastAPI
import com.example.badeapp.api.OceanForecast.RequestManager as OceanForecastAPI

sealed class Badested(
    val lat: String,
    val lon: String,
    val name: String
) {
    /**
     * Other parts of the code use these values to observe changes. This is part of the mvvm
     * philosophy.
     */
    var locationForecastInfo = MutableLiveData<LocationForecastInfo?>()
    var oceanForecastInfo = MutableLiveData<OceanForecastInfo?>()

    /**
     * We cant have multiple threads trying to update the weather data at once, we
     * therefor put the updating behind a mutex. Then only one thread can try to
     * update the weather data. This makes it so we don't request a update for the same
     * Badested on different threads.
     */
    private val locationMutex = Mutex()
    private val oceanMutex = Mutex()

    object Hovedoya : Badested("59.898397", "10.738595", "Hovedøya")
    object Sorenga : Badested("59.894894", "10.724028", "Sørenga")
    object Tjuvholmen : Badested("59.906102", "10.720453", "Tjuvholmen")
    object Paradisbukta : Badested("59.901614", "10.665422", "Paradisbukta")

    //TODO utvid med flere badesteder.

    val isFinishedUpdatingLocationForecast = MutableLiveData<Boolean>()
    val isFinishedUpdatingOceanForecast = MutableLiveData<Boolean>()

    /**
     * This function updates the weather data if there exists newer data.
     * This function does not consider things like how many other requests are
     * happening, or if the user is actually wanting the info.
     */
    fun updateLocationForecast() {
        if (locationForecastInfo.value?.isOutdated() != false) {
            isFinishedUpdatingLocationForecast.value = false
            CoroutineScope(IO).launch {
                locationMutex.withLock {
                    if (locationForecastInfo.value?.isOutdated() != false) {
                        val newData = LocationForecastAPI.request(lat, lon)
                        //@TODO("Handle potential errors with the new data"
                        // what if new data has less info then the old?
                        if (newData != null) {
                            withContext(Main) {
                                locationForecastInfo.value = newData
                                isFinishedUpdatingLocationForecast.value = true
                            }
                        }
                    }
                }
            }
        }
    }

    fun updateOceanForecast() {
        if (oceanForecastInfo.value == null || oceanForecastInfo.value!!.isOutdated()) {
            isFinishedUpdatingOceanForecast.value = false
            CoroutineScope(IO).launch {
                oceanMutex.withLock {
                    if (oceanForecastInfo.value?.isOutdated() != false) { // Hvorfor sjekker vi dette to ganger? - Lena
                        val newData = OceanForecastAPI.request(lat, lon)
                        //@TODO("Handle potential errors with the new data"
                        // what if new data has less info then the old?
                        if (newData != null) {
                            withContext(Main) {
                                oceanForecastInfo.value = newData
                                isFinishedUpdatingOceanForecast.value = true
                            }
                        }
                    }
                }
            }
        }
    }

    override fun toString(): String {
        return "Badested: ${name}"
    }
}



