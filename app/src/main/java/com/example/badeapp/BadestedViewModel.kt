package com.example.badeapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.badeapp.api.MIThrottler
import com.example.badeapp.models.BadestedSummary
import com.example.badeapp.repository.BadestedSummaryRepo
import com.example.badeapp.repository.LocationForecastRepo
import com.example.badeapp.repository.OceanForecastRepo


class BadestedViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "DEBUG - BadestedVM"

    private val badestedSummaryRepo = BadestedSummaryRepo(application)

    val badested = MutableLiveData<BadestedSummary>()


    fun init() {
        Log.d(TAG, "init: initializing...")
        badestedSummaryRepo.printRawDBQuerry() //@TODO remove
    }

}