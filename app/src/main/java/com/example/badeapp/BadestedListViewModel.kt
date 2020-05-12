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


class BadestedListViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "DEBUG - BadestedListVM"

    val hasHalted = MediatorLiveData<Boolean>().also {
        it.addSource(MIThrottler.hasHalted, Observer { hasHalted ->
            if (hasHalted) {
                cancelRequests()
            }
        })
    }

    private val badestedForecastRepo = BadestedForecastRepo(application)


    val summaries: LiveData<List<BadestedForecast>> = badestedForecastRepo.summaries


    fun init() {
        Log.d(TAG, "init: initializing...")
    }


    fun updateData() {
        Log.d(TAG, "updateData: ...")
        // Setter Location- og OceanForecast for alle badestedene
        badestedForecastRepo.updateForecasts()
    }

    fun cancelRequests() {
        //@TODO
    }


}