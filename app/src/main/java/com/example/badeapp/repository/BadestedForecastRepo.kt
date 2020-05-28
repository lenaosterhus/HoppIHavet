package com.example.badeapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.badeapp.api.locationForecast.LocationRequestManager
import com.example.badeapp.api.oceanForecast.OceanRequestManager
import com.example.badeapp.models.Badested
import com.example.badeapp.models.BadestedForecast
import com.example.badeapp.models.alleBadesteder
import com.example.badeapp.persistence.ForecastDao
import kotlinx.coroutines.*

/**
 * This is the repository that handles the interactions to get the
 * summarised data of every badested. This would be the information
 * visible when first opening the app .
 */

private const val TAG = "BadestedForecastRepo"

class BadestedForecastRepo(private val forecastDao: ForecastDao) {

    val forecasts: LiveData<List<BadestedForecast>> = forecastDao.getAllCurrent()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var job : CompletableJob? = null


    fun updateForecasts() {
        _isLoading.postValue(true)

        // Only one thread can update at a time
        synchronized(this) {
            if (job?.isActive == true) {
                return
            }
        }

        job = Job()

        CoroutineScope(Dispatchers.IO + job!!).launch {

            // Check what ocean forecast and location forecasts needs to be updated
            val rawForecasts = forecastDao.getAllCurrentRaw()

            if (rawForecasts.isNullOrEmpty()) {
                // Then no data is stored at all --> We need to update all badesteder
                alleBadesteder.forEach {
                    updateOceanData(it)
                    updateLocationData(it)
                }

            } else {
                rawForecasts.forEach {

                    if (it.forecast?.isOceanForecastOutdated() != false) {
                        updateOceanData(it.badested)
                    }

                    if (it.forecast?.isLocationForecastOutdated() != false) {
                        updateLocationData(it.badested)
                    }
                }
            }
            _isLoading.postValue(false)
        }
        job?.complete()
    }

    private suspend fun updateOceanData(badested: Badested) {

        val newData = try {
            OceanRequestManager.request(badested)
        } catch (ex: Exception) {
            // The updating of data failed, now this can have several reasons,
            // like no internet connection, bad response from the server etc..
            Log.e(TAG,"Exception when getting ocean data: ${ex.message}")
            null
        }

        if (!newData.isNullOrEmpty()) {
            forecastDao.newOceanForecast(newData)
        } else {
            Log.d(TAG, "newData for Ocean was null!")
        }
    }

    private suspend fun updateLocationData(badested: Badested) {

        val newData = try {
            LocationRequestManager.request(badested)
        } catch (ex: Exception) {
            // The updating of data failed, now this can have several reasons,
            // like no internet connection, bad response from the server etc..
            null
        }

        if (!newData.isNullOrEmpty()) {
            forecastDao.newLocationForecast(newData)
        } else {
            Log.d(TAG, "newData for Location was null!")
        }
    }

    fun cancelRequests() {
        Log.d(TAG, "cancelRequests: called...")
        job?.cancel()
    }
}