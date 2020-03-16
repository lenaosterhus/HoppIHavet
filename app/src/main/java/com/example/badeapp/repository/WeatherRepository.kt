package com.example.badeapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.badeapp.api.WeatherRetrofitBuilder
import com.example.badeapp.models.WeatherForecast
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

object WeatherRepository {

    private val TAG = "DEBUG - WeatherRepo"

    var job: CompletableJob? = null // For coroutines

    fun getData(lat: String, lon: String): LiveData<WeatherForecast> {
        Log.d(TAG, "getData: starting...")

        job = Job()

        return object: LiveData<WeatherForecast>() {
            // When getData() is called, onActive is activated and LiveData is returned
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    // CoroutineScope (IO + theJob) --> Creates a unique coroutine on a background thread
                    CoroutineScope(IO + theJob).launch {
                        val data = WeatherRetrofitBuilder.apiService.getData(lat, lon)
                        // LiveData value must be set on the main thread
                        withContext(Main) {
                            value = data
                            theJob.complete()
                        }
                    }
                }
            }
        }
    }

    // Used if activity is destroyed
    // Cancels everything in the CoroutineScope using the job
    fun cancelJobs() {
        job?.cancel()
    }
}