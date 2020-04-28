package com.example.badeapp.persistence

import androidx.room.*
import com.example.badeapp.models.OceanForecast


const val OF_TABLE = "Ocean_Forecast_Table"


@Dao
interface OceanForecastDao {

    /**
     * Returns the location forecast based on the lat and longitude.
     */
    @Query("SELECT * from $OF_TABLE WHERE lat = :lat AND lon = :lon")
    fun getForecasts(lat: String, lon: String): List<OceanForecast>


    /**
     * Inserts all the values in the list. It does not remove the pre-existing values.
     * You therefor would use
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(value: List<OceanForecast>)


    /**
     * Deletes all the LocationForecasts in the DB, that have the right lat and lon
     */
    @Query("DELETE FROM $OF_TABLE WHERE lat = :lat AND lon = :lon")
    fun removeAll(lat: String, lon: String)


    /**
     * Delete all the existing Forecasts, and also insert the new ones.
     * This is all done atomically.
     * @param values is a list of Forecasts, that all share the same lat and lon.
     */
    @Transaction //Makes it atomic
    fun replaceAll(values: List<OceanForecast>) {

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


    //@Query("SELECT DISTINCT * FROM $OF_TABLE WHERE lat = :lat AND lon = :lon AND datetime('now') -")
    //fun getCurrentForecast(lat:String,lon:String) : LiveData<OceanForecast>
}
