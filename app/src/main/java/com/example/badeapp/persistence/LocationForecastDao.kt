package com.example.badeapp.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.badeapp.models.Badested
import com.example.badeapp.models.LocationForecast

const val LF_TABLE = "Location_Forecast_Table"


@Dao
interface LocationForecastDao {

    /**
     * Returns all the forecasts stored for one badested
     */
    @Query("SELECT * from $LF_TABLE WHERE badested = :badested")
    fun getAllForecast(badested: Badested): List<LocationForecast>?


    /**
     * Returns LiveData for all the forecasts stored for one badested
     */
    @Query("SELECT * from $LF_TABLE WHERE badested = :badested")
    fun getAllForecastsLive(badested: Badested): LiveData<List<LocationForecast>>


    @Query("SELECT * FROM $LF_TABLE WHERE badested = :badested AND DATETIME('now') BETWEEN DATETIME([from]) AND DATETIME([to])")
    fun getCurrent(badested: Badested): LocationForecast?


    @Query("SELECT * FROM $LF_TABLE WHERE badested = :badested AND DATETIME('now') BETWEEN DATETIME(:from) AND DATETIME(:to)")
    fun getBetween(badested: Badested, from: String, to: String): List<LocationForecast>?


    /**
     * Inserts all the values in the list. It does not remove the pre-existing values,
     * unless they collide. You should basically never use this method. Use the replaceAll
     * method.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: LocationForecast)

    /**
     * Inserts the values in the list
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(value: List<LocationForecast>)


    /**
     * Deletes all the LocationForecasts in the DB, that have the right lat and lon
     */
    @Query("DELETE FROM $LF_TABLE WHERE badested = :badested")
    fun removeForecasts(badested: Badested)


    /**
     * Delete all the existing Forecasts, and also insert the new ones.
     * This is all done atomically.
     * @param values is a list of Forecasts, that all share the same lat and lon.
     */
    @Transaction //Makes it atomic
    fun replaceAll(values: List<LocationForecast>) {
        if (values.isEmpty()) return

        val badested = values[0].badested
        require( //Throw IllegalArgumentException
            values.all {
                it.badested == badested
            }
        )

        removeForecasts(badested)
        insertAll(values)
    }


    @Query("SELECT * from $LF_TABLE WHERE datetime('now') BETWEEN DATETIME([from]) AND DATETIME([to])")
    fun getAllCurrentForecasts(): LiveData<List<LocationForecast>>


    @Query("SELECT * from $LF_TABLE WHERE datetime('now') BETWEEN DATETIME([from]) AND DATETIME([to])")
    fun getAllCurrentForecastsRaw(): List<LocationForecast>


}
