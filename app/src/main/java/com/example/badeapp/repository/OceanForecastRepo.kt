package com.example.badeapp.repository

import android.app.Application
import android.util.Log
import com.example.badeapp.models.Badested
import com.example.badeapp.models.alleBadesteder
import com.example.badeapp.persistence.ForecastDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class OceanForecastRepo(val application: Application) {

    private val TAG = "OceanForecastRepo"

    private val DB = ForecastDB.getDatabase(application)

    /**
     * This is the data source the repo shares, containing the currently
     * valid weather predictions.
     */
    val forecasts = DB.oceanForecastDao().getAllCurrentForecasts()

    /**
     * We don't want multiple threads to update the location values at once.
     * This would cause us to make unnecessary calls to the MI servers.
     */
    val mutex = Mutex()

    /**
     * This job contains the job doing all the updates. If we cancel this, then all
     * updates going from this Repo will stop.
     */
    private var updateJob: Job? = null

    /**
     * This function updates the weather data if there exists newer data.
     * It does not update if the existing data if its up to date, if someone else
     * is currently updating, or we have recently failed an update.
     */
    fun updateForecasts() {
        Log.d(TAG, "Updating oceanForecast")
        //If someone else is doing an update, then we give up.
        if (mutex.isLocked) return

        updateJob = CoroutineScope(Dispatchers.IO).launch {

            mutex.withLock { //Only one person updating data at once

                //1) Get all the current forecasts, that would be the ones valid now
                // if these are missing or outdated we update its corresponding badested

                val currentForecasts = DB.oceanForecastDao().getAllCurrentForecastsRaw()
                //If currentForecasts is null, then we either have never requested forecasts, or
                //it is at least a long time since last time we used the app. We therefor need
                //to do a new request for every badested
                if (currentForecasts.isNullOrEmpty()) {
                    Log.d(
                        TAG,
                        "Checking ${currentForecasts} for updates resuls in updating all values"
                    )
                    alleBadesteder.forEach {
                        update(it)
                    }
                } else if (currentForecasts.all { !it.isOutdated() }) {
                    Log.d(TAG, "Not updating anything, because all is up to date.")
                } else {
                    currentForecasts.forEach {
                        Log.d(TAG, "Checking ${currentForecasts} for updates")
                        if (it.isOutdated()) {
                            update(it.badested)
                        }
                    }
                }
            }
        }
    }

    /**
     * This method updates
     */
    private suspend fun update(badested: Badested) {

        CoroutineScope(Dispatchers.IO).launch {

            val newData = try {
                com.example.badeapp.api.OceanForecast.RequestManager.request(badested)
            } catch (ex: Exception) {
                //The updating of data failed, now this can have several reasons,
                // like no internet connection, bad response from the server etc..
                //@TODO handle
                null
            }

            if (!newData.isNullOrEmpty()) {
                Log.d(TAG, "Writing newData to database")
                DB.oceanForecastDao().replaceAll(newData)
            } else {
                Log.d(TAG, "newData was null!")
            }
        }

    }


    fun haltUpdates() {
        updateJob?.cancel()
    }


}