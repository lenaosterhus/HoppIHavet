package com.example.badeapp.persistence

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.badeapp.models.*
import kotlin.reflect.jvm.internal.impl.resolve.constants.LongValue


private const val TAG = "ForecastDao"


@Dao
interface ForecastDao {

    /**
     * This part of the dao is only used when inserting new data.
     */

    @Query("DELETE FROM Forecast WHERE DATETIME('now') > DATETIME([to])")
    fun private_deleteOutdated()

    @Update(entity=Forecast::class)
    fun private_updateLocationForecast(entries: List<LocationForecast>)

    @Update(entity=Forecast::class)
    fun private_updateOceanForecast(entries: List<OceanForecast>)

    @Insert(onConflict = OnConflictStrategy.IGNORE,entity=Forecast::class)
    fun private_setInnNewOceanForecasts(entries: List<OceanForecast>)

    @Insert(onConflict = OnConflictStrategy.IGNORE,entity=Forecast::class)
    fun private_setInnNewLocationForecasts(entries: List<LocationForecast>)

    /**
     * All badesteder are inserted when the DB is created (see ForecastDB)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun putBadested(badested: Badested)


    /**
     * These are the insert methods for the dao
     */
    @Transaction
    fun newLocationForecast(entries:List<LocationForecast>){
        private_deleteOutdated()
        private_setInnNewLocationForecasts(entries)
        private_updateLocationForecast(entries)
    }

    @Transaction
    fun newOceanForecast(entries:List<OceanForecast>) {
        private_deleteOutdated()
        private_updateOceanForecast(entries)
        private_setInnNewOceanForecasts(entries)
    }


    @Query("SELECT * FROM Badested LEFT JOIN Forecast on Badested.id = Forecast.badestedId AND DATETIME('now') BETWEEN DATETIME([from]) AND DATETIME([to])")
    abstract fun getAllCurrent(): LiveData<List<BadestedForecast>>

    @Query("SELECT * FROM Badested LEFT JOIN Forecast on Badested.id = Forecast.badestedId AND DATETIME('now') BETWEEN DATETIME([from]) AND DATETIME([to])")
    abstract fun getAllCurrentRaw(): List<BadestedForecast>

    @Query("SELECT * FROM Badested LEFT JOIN Forecast on Badested.id = Forecast.badestedId")
    abstract fun getAll(): LiveData<List<BadestedForecast>>

    @Query("SELECT * FROM Badested LEFT JOIN Forecast on Badested.id = Forecast.badestedId")
    abstract fun getAllRaw(): List<BadestedForecast>


}