package com.example.badeapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.badeapp.api.MyRetrofitBuilder
import com.example.badeapp.models.WeatherForecast
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

object Repository {

    private val TAG = "DEBUG - Repository"

    var job: CompletableJob? = null

    fun getData(): LiveData<WeatherForecast.Container> {
        Log.d(TAG, "getData: starting...")
        job = Job()

        return object: LiveData<WeatherForecast.Container>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        // MÅ ENDRES!
                        val data = MyRetrofitBuilder.apiService.getData("59.9193", "10.7522")
                        withContext(Main) {
                            value = data
                            theJob.complete()
                        }
                    }
                }
            }
        }
    }

    fun cancelJobs() {
        job?.cancel()
    }
}