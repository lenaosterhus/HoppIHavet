package com.example.badeapp.persistence

import androidx.room.Dao

/**
 * The data we are accessing in this context is just a different view into the existing tables
 * BadestedForecast = Combination of LocationForecast and OceanForecast
 */

@Dao
interface BadestedForecastDao {

    //@Query("")
    //fun getBadestedForecasts(lat:String,lon:String) : LiveData<List<BadestedForecast>>

    //@Query("")
    // fun getCurrentBadestedForecast(lat:String,lon:String) : LiveData<BadestedForecast>
}