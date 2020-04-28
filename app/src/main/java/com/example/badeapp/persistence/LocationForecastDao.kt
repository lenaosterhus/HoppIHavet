package com.example.badeapp.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.badeapp.models.LocationForecast

const val LF_TABLE = "Location_Forecast_Table"


@Dao
interface LocationForecastDao {


    /**
     * Returns the location forecast based on the lat and longitude.
     */
    @Query("SELECT * from $LF_TABLE WHERE lat = :lat AND lon = :lon")
    fun getForecasts(lat: String, lon: String): List<LocationForecast>


    /**
     * Inserts all the values in the list. It does not remove the pre-existing values.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(value: List<LocationForecast>)


    /**
     * Deletes all the LocationForecasts in the DB, that have the right lat and lon
     */
    @Query("DELETE FROM $LF_TABLE WHERE lat = :lat AND lon = :lon")
    fun removeAll(lat: String, lon: String)


    /**
     * Delete all the existing Forecasts, and also insert the new ones.
     * This is all done atomically.
     * @param values is a list of Forecasts, that all share the same lat and lon.
     */
    @Transaction //Makes it atomic
    fun replaceAll(values: List<LocationForecast>) {

        if (values.isEmpty()) return

        val lat = values[0].lat
        val lon = values[0].lon

        require( //Throw IllegalArgumentException
            values.all {
                lat == it.lat && lon == it.lon
            }
        )

        removeAll(lat, lon)
        insertAll(values)
    }


    @Query("SELECT DISTINCT * FROM $LF_TABLE WHERE lat = :lat AND lon = :lon AND datetime('now') BETWEEN `from` AND `to`  ")
    fun getCurrentForecast(lat: String, lon: String): LiveData<LocationForecast>
}