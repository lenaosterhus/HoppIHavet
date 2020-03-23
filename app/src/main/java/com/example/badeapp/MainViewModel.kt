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
    }

    fun setData() {
        Log.d(TAG, "setData: ...")
        badesteder.value = Badested::class.nestedClasses.map {
            it.objectInstance as Badested
        }

        badesteder.value?.forEach { badested ->
            Log.d(TAG, "setData: for $badested")
            badested.updateLocationForecast()
        }
    }

}