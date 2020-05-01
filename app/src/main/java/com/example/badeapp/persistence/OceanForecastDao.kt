package com.example.badeapp.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.badeapp.models.Badested
import com.example.badeapp.models.OceanForecast


const val OF_TABLE = "Ocean_Forecast_Table"


@Dao
interface OceanForecastDao {

    /**
     * Returns all the forecasts stored for one badested
     */
    @Query("SELECT * from $OF_TABLE WHERE badested = :badested")
    fun getAllForecast(badested: Badested): List<OceanForecast>?


    /**
     * Returns LiveData for all the forecasts stored for one badested
     */
    @Query("SELECT * from $OF_TABLE WHERE badested = :badested")
    fun getAllForecastsLive(badested: Badested): LiveData<List<OceanForecast>>


    @Query("SELECT * FROM $OF_TABLE WHERE badested = :badested AND DATETIME('now') BETWEEN DATETIME([from]) AND DATETIME([to])")
    fun getCurrent(badested: Badested): OceanForecast?


    @Query("SELECT * FROM $OF_TABLE WHERE badested = :badested AND DATETIME('now') BETWEEN DATETIME(:from) AND DATETIME(:to)")
    fun getBetween(badested: Badested, from: String, to: String): List<OceanForecast>?


    /**
     * Inserts all the values in the list. It does not remove the pre-existing values,
     * unless they collide. You should basically never use this method. Use the replaceAll
     * method.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: OceanForecast)

    /**
     * Inserts the values in the list
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(value: List<OceanForecast>)


    /**
     * Deletes all the OceanForecasts in the DB, that have the right lat and lon
     */
    @Query("DELETE FROM $OF_TABLE WHERE badested = :badested")
    fun removeForecasts(badested: Badested)


    /**
     * Delete all the existing Forecasts, and also insert the new ones.
     * This is all done atomically.
     * @param values is a list of Forecasts, that all share the same lat and lon.
     */
    @Transaction //Makes it atomic
    fun replaceAll(values: List<OceanForecast>) {
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


    @Query("SELECT * from $OF_TABLE WHERE datetime('now') BETWEEN DATETIME([from]) AND DATETIME([to])")
    fun getAllCurrentForecasts(): LiveData<List<OceanForecast>>


    @Query("SELECT * from $OF_TABLE WHERE datetime('now') BETWEEN DATETIME([from]) AND DATETIME([to])")
    fun getAllCurrentForecastsRaw(): List<OceanForecast>






}
