package com.example.badeapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.badeapp.repository.Badested
import java.util.*

class MainViewModel: ViewModel() {

    private val TAG = "DEBUG - MainViewModel"

    val badesteder: MutableLiveData<List<Badested>> = MutableLiveData<List<Badested>>()
    val tid = MutableLiveData<Date>()

    fun init() {
        Log.d(TAG, "init: initializing...")
        badesteder.value = Badested::class.nestedClasses.map {
            it.objectInstance as Badested
        }
        updateData()
    }

    fun updateData() {
        Log.d(TAG, "updateData: ...")
        // Setter Location- og OceanForecast for alle badestedene
        badesteder.value?.forEach { badested ->
            badested.updateAll()
        }
    }
}