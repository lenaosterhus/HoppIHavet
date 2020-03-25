package com.example.badeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        // Observing changes to viewModel.data
        viewModel.weatherData.observe(this, Observer { weatherForecast ->
            Log.d(TAG, "onCreate WEATHER observer: ${weatherForecast}")
        })
        viewModel.oceanData.observe(this, Observer {
            Log.d(TAG, "onCreate OCEAN observer: ${it}")
        })

        viewModel.setData("59.9016", "10.665422")

        // Liste over steder
        // Hent data for listen over steder

    }

    // All jobs are canceled if activity is destroyed
    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelJobs()
    }
}
