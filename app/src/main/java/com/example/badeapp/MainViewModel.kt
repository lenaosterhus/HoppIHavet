package com.example.badeapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.badeapp.models.WeatherForcast
import com.example.badeapp.repository.Repository

class MainViewModel: ViewModel() {

    private val TAG = "DEBUG - MainViewModel"
    private val _test: MutableLiveData<String> = MutableLiveData()

    val data: LiveData<WeatherForcast.Container> = Transformations
        .switchMap(_test) {
            Repository.getData()
        }

    fun setData() {
        _test.value = "test"
    }


    fun cancelJobs() {
        Log.d(TAG, "cancelJobs: ")
        Repository.cancelJobs()
    }



}