package com.example.badeapp.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.badeapp.models.OceanForecastInfo


@Dao
interface OceanForecastInfoDao {

    @Query("SELECT * FROM Ocean_Forecast_Info_Table WHERE lat = :lat AND lon = :lon")
    fun getOceanForecastInfo(lat: String, lon: String): OceanForecastInfo


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(it: OceanForecastInfo)


}
