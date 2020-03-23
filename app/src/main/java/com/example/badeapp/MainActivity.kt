package com.example.badeapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val TAG = "DEBUG - MainActivity"

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Business-logic =
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.init(this)

        viewModel.finishedLoading.observe(this, Observer {
            if (it) {
                viewModel._locations.value?.forEach {
                    Log.d(TAG, "$it")
                    Log.d(TAG, "${it.oceanForecast.getCurrentSeaTemp()}")
                    Log.d(TAG, "${it.weatherForecast.getCurrentAirTemp()}")
                }
            }
        })

        viewModel.setData()

        // Liste over steder
        // Hent data for listen over steder

    }

    // All jobs are canceled if activity is destroyed
    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelJobs()
    }
}
