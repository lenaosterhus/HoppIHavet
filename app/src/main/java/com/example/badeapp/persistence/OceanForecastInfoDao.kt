package com.example.badeapp.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.badeapp.models.OceanForecastInfo

private const val TABLE_NAME = "Ocean_Forecast_Info_Table"

@Dao
interface OceanForecastInfoDao {

    @Query("SELECT * FROM $TABLE_NAME WHERE lat = :lat AND lon = :lon")
    fun getForecastsInfo(lat: String, lon: String): LiveData<OceanForecastInfo>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun setForecastInfo(it: OceanForecastInfo)



}
