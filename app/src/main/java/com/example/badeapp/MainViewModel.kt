package com.example.badeapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.example.badeapp.api.MIThrottler
import com.example.badeapp.repository.Badested

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "DEBUG - MainViewModel"

    val badesteder: List<Badested> by lazy {
        Badested::class.nestedClasses.map {
            (it.objectInstance as Badested).apply {
                this.initialiseDB(getApplication())
            }
        }
    }


    val hasHalted = MediatorLiveData<Boolean>()

    fun init() {
        Log.d(TAG, "init: initializing...")

        hasHalted.addSource(MIThrottler.hasHalted, Observer { hasHalted ->
            if (hasHalted) {
                cancelRequests()
            }
        })

        updateData()

    }


    fun updateData() {
        Log.d(TAG, "updateData: ...")
        // Setter Location- og OceanForecast for alle badestedene
        badesteder.forEach { badested ->
            badested.initialiseDB(getApplication<Application>().applicationContext)
            badested.updateAll()
        }
    }

    fun cancelRequests() {
        badesteder.forEach {
            it.cancelJobs()
        }
    }
}