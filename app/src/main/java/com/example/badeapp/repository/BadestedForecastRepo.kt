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
import kotlin.math.log


/**
 * This is the repository that handles the interactions to get the
 * summarised data of every badested. This would be the information
 * visible when first opening the app .
 *
 */

private const val TAG = "BadestedForecastRepo"


class BadestedForecastRepo(val forecastDao: ForecastDao) {

    private val _forecasts = MutableLiveData<List<BadestedForecast>>()
    val forecasts : LiveData<List<BadestedForecast>> = _forecasts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun updateForecasts() {
        Log.d(TAG, "updateForecasts: Setting isLoading to true")
        _isLoading.postValue(true)

        CoroutineScope(Dispatchers.IO).launch {

            // 1) Check what ocean forecast and location forecasts needs to be updated
            val forecasts = forecastDao.getAllCurrentRaw()

                if (forecasts.isNullOrEmpty()) {
                    // Then no data is stored at all. We need to update all badesteder
                    alleBadesteder.forEach {
                        updateLocationData(it)
                        updateOceanData(it)
                    }
                    Log.d(TAG,"Forecasts was empty")
                    Log.d(TAG, "updateForecasts: Setting isLoading to false")
                    _isLoading.postValue(false)
                    return@launch
                }

                forecasts.forEach {

                    if(it.forecast == null){
                        Log.d(TAG,"Forecast was null for ${it.badested}")
                    }

                    if (it.forecast?.isOceanForecastOutdated() != false) {
                        Log.d(TAG,"Ocean data outdated for  ${it.badested}")
                        updateOceanData(it.badested)
                    }

                    if (it.forecast?.isLocationForecastOutdated() != false) {
                        Log.d(TAG,"Location data outdated for  ${it.badested}")
                        updateLocationData(it.badested)
                    }
                }

            _forecasts.postValue(forecastDao.getAllCurrentRaw())
            Log.d(TAG, "updateForecasts: Setting isLoading to false")
            _isLoading.postValue(false)
        }
    }

    private suspend fun updateOceanData(badested: Badested) {

        val newData = try {
            OceanRequestManager.request(badested)
        } catch (ex: Exception) {
            //The updating of data failed, now this can have several reasons,
            // like no internet connection, bad response from the server etc..
            Log.e(TAG,"Exception when getting ocean data: ${ex.message}")
            //@TODO handle
            null
        }

        if (!newData.isNullOrEmpty()) {
            forecastDao.newOceanForecast(newData)
        } else {
            Log.d(TAG, "newData was null!")
        }
    }

    private suspend fun updateLocationData(badested: Badested) {

        val newData = try {
            LocationRequestManager.request(badested)
        } catch (ex: Exception) {
            //The updating of data failed, now this can have several reasons,
            // like no internet connection, bad response from the server etc..
            //@TODO handle
            null
        }

        if (!newData.isNullOrEmpty()) {
            forecastDao.newLocationForecast(newData)
        } else {
            Log.d(TAG, "newData was null!")
        }
    }

    fun cancelRequests() {
        Log.d(TAG, "cancelRequests: called...")
        (Dispatchers.IO).cancel()
    }


}