package com.example.badeapp.persistence

import android.location.Location
import android.util.Log
import androidx.room.*
import com.example.badeapp.models.Forecast
import com.example.badeapp.models.LocationForecast
import com.example.badeapp.models.OceanForecast
import kotlin.reflect.jvm.internal.impl.resolve.constants.LongValue


private const val TAG = "ForecastDao"

private const val DELETE_OLD = """
    DELETE FROM Forecast WHERE DATETIME('now') > DATETIME([to])
"""

@Dao
interface ForecastDao {

    /**
     * This dao is only used when inserting new data.
     */

    @Query(DELETE_OLD)
    fun private_deleteOutdated()

    @Update(entity=Forecast::class)
    fun private_updateLocationForecast(entries: List<LocationForecast>)

    @Update(entity=Forecast::class)
    fun private_updateOceanForecast(entries: List<OceanForecast>)

    @Insert(onConflict = OnConflictStrategy.IGNORE,entity = Forecast::class)
    fun private_setInnNewOceanForecasts(entries: List<OceanForecast>)

    @Insert(onConflict = OnConflictStrategy.IGNORE,entity = Forecast::class)
    fun private_setInnNewLocationForecasts(entries: List<LocationForecast>)


    @Transaction
    fun newLocationForecast(entries:List<LocationForecast>){
        private_deleteOutdated()
        private_setInnNewLocationForecasts(entries)
        private_updateLocationForecast(entries)
    }


    @Transaction
    fun newOceanForecast(entries:List<OceanForecast>) {
        Log.d(TAG,"1")
        private_deleteOutdated()
        Log.d(TAG,"2")
        private_updateOceanForecast(entries)
        Log.d(TAG,"3")
        private_setInnNewOceanForecasts(entries)
        Log.d(TAG,"4")
    }


}