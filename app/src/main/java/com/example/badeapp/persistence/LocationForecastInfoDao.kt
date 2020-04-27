package com.example.badeapp.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.badeapp.models.LocationForecastInfo

private const val TABLE_NAME = "Location_Forecast_Info_Table"

@Dao
interface LocationForecastInfoDao {

    @Query("SELECT * from $TABLE_NAME WHERE lat = :lat AND lon = :lon")
    fun getForecastsInfo(lat: String, lon: String): LiveData<LocationForecastInfo>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setForecastInfo(value: LocationForecastInfo)
}