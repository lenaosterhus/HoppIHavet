package com.example.badeapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.example.badeapp.api.MIThrottler
import com.example.badeapp.models.BadestedForecast
import com.example.badeapp.repository.BadestedForecastRepo
import com.example.badeapp.repository.LocationForecastRepo
import com.example.badeapp.repository.OceanForecastRepo


class BadestedListViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "DEBUG - BadestedListVM"

    val hasHalted = MediatorLiveData<Boolean>().also {
        it.addSource(MIThrottler.hasHalted, Observer { hasHalted ->
            if (hasHalted) {
                cancelRequests()
            }
        })
    }

    private val locationForecastRepo = LocationForecastRepo(application)
    private val oceanForecastRepo = OceanForecastRepo(application)
    private val badestedForecastRepo = BadestedForecastRepo(application)


    val summaries: LiveData<List<BadestedForecast>> = badestedForecastRepo.summaries


    fun init() {
        Log.d(TAG, "init: initializing...")
        badestedForecastRepo.printRawDBQuerry() //@TODO remove
    }


    fun updateData() {
        Log.d(TAG, "updateData: ...")
        // Setter Location- og OceanForecast for alle badestedene
        locationForecastRepo.updateForecasts()
        oceanForecastRepo.updateForecasts()
    }

    fun cancelRequests() {
        locationForecastRepo.haltUpdates()
        oceanForecastRepo.haltUpdates()
    }

    //@TODO remove
    fun printRawDBQuerry() {
        badestedForecastRepo.printRawDBQuerry()
    }
}