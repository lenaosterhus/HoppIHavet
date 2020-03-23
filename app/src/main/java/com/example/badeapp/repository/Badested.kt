package com.example.badeapp.repository

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
import com.example.badeapp.api.LocationForecast.RequestManager as LocationForcastAPI


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
     * update the weather data. This makes it so we dont request a update for the same
     * Badested on different threads.
     */
    private val mutex = Mutex()

    object Hovedoya : Badested("59.898397", "10.738595", "Hovedøya")
    object Sorenga : Badested("59.894894", "10.724028", "Sørenga")
    object Tjuvholmen : Badested("59.906102", "10.720453", "Tjuvholmen")
    object Paradisbukta : Badested("59.901614", "10.665422", "Paradisbukta")


    /**
     * This function updates the weather data if there exists newer data.
     * This function does not consider things like how many other requests are
     * happening, or if the user is actually wanting the info.
     */
    suspend fun updateLocationForecast() {
        mutex.withLock {
            if (locationForecastInfo.value?.isOutdated() == true) {
                CoroutineScope(IO).launch {
                    val newData = LocationForcastAPI.request(lat, lon)
                    //@TODO("Handle potential errors with the new data"
                    // what if new data has less info then the old?
                    if (newData != null) {
                        withContext(Main) {
                            locationForecastInfo.value = newData
                        }
                    }
                }
            }

        }
    }

}



