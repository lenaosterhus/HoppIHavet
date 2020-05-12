package com.example.badeapp.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.badeapp.models.Badested
import com.example.badeapp.models.BadestedForecast
import com.example.badeapp.models.alleBadesteder
import com.example.badeapp.persistence.ForecastDB
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.parseAsGmtIsoDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * This is the repository that handles the interactions to get the
 * summarised data of every badested. This would be the information
 * visible when first opening the app .
 *
 */

private const val TAG = "BadestedForecastRepo"



class BadestedForecastRepo(val application: Application) {

    private var lst: List<BadestedForecast>? = null

    private val DB = ForecastDB.getDatabase(application)

    val _summaries = MutableLiveData<List<BadestedForecast>>()
    val summaries : LiveData<List<BadestedForecast>> = _summaries

    val db_summaries = DB.badestedForecastDao().getAllCurrent().also {
        it.observeForever{ forecasts ->
            _summaries.value = forecasts;
        }
    }

    fun updateForecasts() {
        CoroutineScope(Dispatchers.IO).launch {


                //1) Check what ocean forecast and location forecasts needs to be updated
                val forecasts = DB.badestedForecastDao().getAllCurrentRaw()

                if (forecasts.isNullOrEmpty()) {
                    //Then no data is stored at all. We need to update all badesteder
                    CoroutineScope(Dispatchers.IO).launch {
                        alleBadesteder.forEach {
                            updateLocationData(it)
                            updateOceanData(it)
                        }
                    }
                    return@launch
                }

                forecasts.forEach {

                    if (it.forecast?.isOceanForecastOutdated() != false) {
                        updateOceanData(it.badested)
                    }

                    if (it.forecast?.isLocationForecastOutdated() != false) {
                        updateLocationData(it.badested)
                    }

                }

            _summaries.postValue(DB.badestedForecastDao().getAllCurrentRaw())
        }
    }

    private suspend fun updateOceanData(badested: Badested) {

        val newData = try {
            com.example.badeapp.api.oceanForecast.RequestManager.request(badested)
        } catch (ex: Exception) {
            //The updating of data failed, now this can have several reasons,
            // like no internet connection, bad response from the server etc..
            Log.d(TAG,"Exception when getting ocean data!")
            //@TODO handle
            null
        }

        if (!newData.isNullOrEmpty()) {
            DB.forecastDao().newOceanForecast(newData)
        } else {
            Log.d(TAG, "newData was null!")
        }


    }

    private suspend fun updateLocationData(badested: Badested) {

        CoroutineScope(Dispatchers.IO).launch {

            val newData = try {
                com.example.badeapp.api.locationForecast.RequestManager.request(badested)
            } catch (ex: Exception) {
                //The updating of data failed, now this can have several reasons,
                // like no internet connection, bad response from the server etc..
                //@TODO handle
                null
            }

            if (!newData.isNullOrEmpty()) {
                DB.forecastDao().newLocationForecast(newData)
            } else {
                Log.d(TAG, "newData was null!")
            }
        }

    }


}