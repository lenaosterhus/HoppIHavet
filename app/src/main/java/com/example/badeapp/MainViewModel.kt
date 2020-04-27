package com.example.badeapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.badeapp.repository.Badested

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "DEBUG - MainViewModel"

    // Skjer ingen endringer i selve listen... Ikke poeng at det er LiveData?
    lateinit var badesteder: List<Badested>

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
            badested.initialiseDB(getApplication<Application>().applicationContext)
            badested.updateAll()
        }
    }
}