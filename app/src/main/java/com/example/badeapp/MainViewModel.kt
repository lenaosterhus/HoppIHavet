package com.example.badeapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.badeapp.models.OceanForecast
import com.example.badeapp.models.WeatherForecast
import com.example.badeapp.repository.Repository

class MainViewModel: ViewModel() {

    private val TAG = "DEBUG - MainViewModel"
    // Trigger to initiate the request
    private val _lat: MutableLiveData<String> = MutableLiveData()
    private val _lon: MutableLiveData<String> = MutableLiveData()

    // How they do it in Google samples :)
    val weatherData: LiveData<WeatherForecast> = Transformations
        // switchMap = observing the argument. When it changes the operator will trigger and execute action inside {}
        .switchMap(_lon) {
            Repository.getWeatherData(_lat.value!!, it)
        }
    val oceanData: LiveData<OceanForecast> = Transformations
        .switchMap(_lon) {
            Repository.getOceanData(_lat.value!!, it)
        }

    fun setData(lat: String, lon: String) {
        // If the value has not changed --> don't do anything
        if (_lat.value == lat && _lon.value == lon) {
            return
        }
        _lat.value = lat
        _lon.value = lon
    }


    fun cancelJobs() {
        Log.d(TAG, "cancelJobs: ")
        Repository.cancelJobs()
    }
}