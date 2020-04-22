package com.example.badeapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.badeapp.repository.Badested
import java.util.*

class MainViewModel: ViewModel() {

    private val TAG = "DEBUG - MainViewModel"

    // Skjer ingen endringer i selve listen... Ikke poeng at det er LiveData?
    lateinit var badesteder: List<Badested>

    val tid = MutableLiveData<Date>()

    fun init() {
        Log.d(TAG, "init: initializing...")
        badesteder = Badested::class.nestedClasses.map {
            it.objectInstance as Badested
        }
        updateData()
    }

    fun updateData() {
        Log.d(TAG, "updateData: ...")
        // Setter Location- og OceanForecast for alle badestedene
        badesteder.forEach { badested ->
            badested.updateAll()
        }
    }
}