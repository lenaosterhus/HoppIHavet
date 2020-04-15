package com.example.badeapp

import android.util.Log
import androidx.lifecycle.*
import com.example.badeapp.UI.MainActivity
import com.example.badeapp.repository.Badested

class MainViewModel: ViewModel() {

    private val TAG = "DEBUG - MainViewModel"

    private val _badesteder = MutableLiveData<List<Badested>>()

    // Syntaks for å endre måten man kan få tak i badesteder
    val badesteder: LiveData<List<Badested>>
        get() = _badesteder

    val countUpdated = MediatorLiveData<Int>()


    fun init() {
        Log.d(TAG, "init: initializing...")
        _badesteder.value = Badested::class.nestedClasses.map {
            it.objectInstance as Badested
        }
        _badesteder.value?.forEach { badested ->
            // Observerer om LocationForecast er ferdig oppdatert for badestedet
            countUpdated.addSource(badested.isFinishedUpdatingLocationForecast) { isFinished ->
                if (isFinished) {
                    countUpdated.value = countUpdated.value?.plus(1)
                    Log.d(TAG, "isFinished WEATHER $badested")
                }
            }
            // Observerer om OceanForecast er ferdig oppdatert for badestedet
            countUpdated.addSource(badested.isFinishedUpdatingOceanForecast) { isFinished ->
                if (isFinished) {
                    Log.d(TAG, "isFinished OCEAN $badested")
                    countUpdated.value = countUpdated.value?.plus(1)
                }
            }
        }
        updateData()
    }

    fun updateData() {
        Log.d(TAG, "updateData: ...")
        countUpdated.value = 0
        // Setter Location- og OceanForecast for alle badestedene
        _badesteder.value?.forEach { badested ->
            badested.updateLocationForecast()
            badested.updateOceanForecast()
        }
    }
}