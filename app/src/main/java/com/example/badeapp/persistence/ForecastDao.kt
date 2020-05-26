package com.example.badeapp.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.badeapp.models.*


@Dao
interface ForecastDao {

    /**
     * This part of the dao is only used when inserting new data.
     */

    /**
     * This query deletes all the forecasts, that are outdated. We don't need forecasts
     * about a time period that has passed.
     */
    @Query("DELETE FROM Forecast WHERE DATETIME('now') > DATETIME([to])")
    fun private_deleteOutdated()

    /**
     * This query modifies existing forecasts, so that existing forecasts are updated.
     */
    @Update(entity=Forecast::class)
    fun private_updateLocationForecast(entries: List<LocationForecast>)

    @Update(entity=Forecast::class)
    fun private_updateOceanForecast(entries: List<OceanForecast>)


    /**
     * This query skips all the forecasts that already have a entry in the database, only inserting
     * the forecasts that are new.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE,entity=Forecast::class)
    fun private_addNewOceanForecasts(entries: List<OceanForecast>)

    @Insert(onConflict = OnConflictStrategy.IGNORE,entity=Forecast::class)
    fun private_addNewLocationForecasts(entries: List<LocationForecast>)


    /**
     * These are the insert methods for the dao
     */

    // All badesteder are inserted when the DB is created (see ForecastDB)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllBadesteder(badesteder: List<Badested>)

    // For testing
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun putBadested(badested: Badested)


    @Transaction
    fun newLocationForecast(entries:List<LocationForecast>){
        /**
         * 1) Delete outdated forecasts
         * 2) Update the forecast that already have a entry in the database.
         * 3) Create entries in the DB for the forecasts that are not present in the DB.
         */
        private_deleteOutdated()
        private_addNewLocationForecasts(entries)
        private_updateLocationForecast(entries)
    }

    @Transaction
    fun newOceanForecast(entries:List<OceanForecast>) {
        private_deleteOutdated()
        private_updateOceanForecast(entries)
        private_addNewOceanForecasts(entries)
    }

    /**
     * Getter methods
     */

    @Transaction
    @Query("SELECT * FROM BadestedForecast")
    fun getAllCurrent(): LiveData<List<BadestedForecast>>

    @Transaction
    @Query("SELECT * FROM BadestedForecast")
    fun getAllCurrentRaw(): List<BadestedForecast>

    @Transaction
    @Query("SELECT * FROM Badested LEFT JOIN Forecast on Badested.id = Forecast.badestedId")
    fun getAll(): LiveData<List<BadestedForecast>>

    @Transaction
    @Query("SELECT * FROM Badested LEFT JOIN Forecast on Badested.id = Forecast.badestedId")
    fun getAllRaw(): List<BadestedForecast>
}