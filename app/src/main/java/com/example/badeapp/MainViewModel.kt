package com.example.badeapp

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.badeapp.repository.Badested

class MainViewModel: ViewModel() {

    private val TAG = "DEBUG - MainViewModel"
    val badesteder = MutableLiveData<List<Badested>>()

    lateinit var owner: LifecycleOwner

    fun init(activity: MainActivity) {
        Log.d(TAG, "init: initializing...")
        owner = activity

        val badestedList: List<Badested> = Badested::class.nestedClasses.map {
            it.objectInstance as Badested
        }

        setData(badestedList)
    }

    private fun setData(badestedList: List<Badested>) {
        Log.d(TAG, "setData: ...")

        // Setter Location- og OceanForecast for alle badestedene
        badestedList.forEach { badested ->
            Log.d(TAG, "setData: for $badested")
            badested.updateLocationForecast()
            badested.updateOceanForecast()
        }
        badesteder.value = badestedList
    }

}