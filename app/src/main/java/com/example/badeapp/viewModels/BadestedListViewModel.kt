package com.example.badeapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.badeapp.api.MIThrottler
import com.example.badeapp.api.MIThrottler.stopTimeMin
import com.example.badeapp.models.BadestedForecast
import com.example.badeapp.persistence.ForecastDB
import com.example.badeapp.repository.BadestedForecastRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BadestedListViewModel(application: Application) : AndroidViewModel(application) {

    private val badestedForecastRepo =
        BadestedForecastRepo(ForecastDB.getDatabase(application).forecastDao())

    val forecasts: LiveData<List<BadestedForecast>> = badestedForecastRepo.forecasts

    val isLoading = badestedForecastRepo.isLoading

    val hasHalted = MediatorLiveData<Boolean>().apply {
        addSource(MIThrottler.hasHalted) { hasHalted ->
            if (hasHalted) {
                cancelRequests()
                this.value = true

                CoroutineScope(Dispatchers.IO).launch {
                    delay(stopTimeMin * 1001)
                    if (MIThrottler.canResume()) {
                        updateData()
                    }
                }
            } else {
                this.value = false
            }
        }
    }


    fun updateData() {
        // Setting Location- og OceanForecast for all badested that are outdated
        badestedForecastRepo.updateForecasts()
    }

    fun cancelRequests() {
        badestedForecastRepo.cancelRequests()
    }
}