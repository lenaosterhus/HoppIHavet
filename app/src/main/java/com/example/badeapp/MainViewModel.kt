package com.example.badeapp

import android.util.Log
import androidx.lifecycle.*
import com.example.badeapp.repository.Badested

class MainViewModel: ViewModel() {

    private val TAG = "DEBUG - MainViewModel"
    val _locations = MutableLiveData<List<Badested>>()
    lateinit var _repository: Repository
    val finishedLoading = MutableLiveData<Boolean>().apply {
        value = false
    }
    lateinit var owner: LifecycleOwner

    fun init(activity: MainActivity) {
        Log.d(TAG, "init: initializing...")
        _repository = Repository
        owner = activity
    }

    fun setData() {
        Log.d(TAG, "setData: ...")
        _locations.value = Badested::class.nestedClasses.map {
            it.objectInstance as Badested
        }

        var locationsSet = 0

        _locations.value?.forEach { badested ->
            Log.d(TAG, "setData: for $location")

            val weatherData: LiveData<LocationForecastInfo> =
                _repository.getWeatherData(location.lat, location.lon)
            weatherData.observe(owner, Observer {
                Log.d(TAG, "setData weather: Observing for $location")
                it?.let {
                    Log.d(TAG, "setData weather: Data existing for $location")
                    badested.locationForecastInfo = it
                    locationsSet++
                    if (locationsSet == _locations.value!!.size * 2) {
                        Log.d(TAG, "setData weather: finished loading")
                        finishedLoading.value = true
                    }
                }
            })

            val oceanData: LiveData<OceanForecast> =
                _repository.getOceanData(location.lat, location.lon)
            oceanData.observe(owner, Observer {
                Log.d(TAG, "setData ocean: Observing for $location")
                it?.let {
                    Log.d(TAG, "setData ocean: Data existing for $location")
                    location.oceanForecast = it
                    locationsSet++
                    if (locationsSet == _locations.value!!.size * 2) {
                        Log.d(TAG, "setData ocean: finished loading")
                        finishedLoading.value = true
                    }
                }
            })
        }
    }


    fun cancelJobs() {
        Log.d(TAG, "cancelJobs: ")
        Repository.cancelJobs()
    }
}