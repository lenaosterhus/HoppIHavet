package com.example.badeapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.badeapp.models.BadestedForecast
import com.example.badeapp.repository.BadestedForecastRepo



class BadestedViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "DEBUG - BadestedVM"

    private val badestedForecastRepo = BadestedForecastRepo(application)

    val badested = MutableLiveData<BadestedForecast>()


}