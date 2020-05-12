package com.example.badeapp.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.badeapp.models.Badested
import com.example.badeapp.models.BadestedForecast

/*
private const val GET_SUMMARYS_FOR_ONE = GET_SUMMARYS + " WHERE $LF_TABLE.badested = :badested"

private const val GET_SUMMARIES_MANY = GET_SUMMARYS + " WHERE \n" +
        " DATETIME('now') BETWEEN DATETIME(${LF_TABLE}.[from]) AND DATETIME(${LF_TABLE}.[to])"

*/

@Dao
interface BadestedForecastDao {
    //@Query("SELECT * FROM Forecast WHERE DATETIME('now') BETWEEN DATETIME([from]) AND DATETIME([to])")
    @Query("SELECT * FROM Badested LEFT JOIN Forecast on Badested.id = Forecast.badestedId and DATETIME('now') BETWEEN DATETIME([from]) AND DATETIME([to])")
    abstract fun getAllCurrent(): LiveData<List<BadestedForecast>>

    @Query("SELECT * FROM Badested LEFT JOIN Forecast on Badested.id = Forecast.badestedId and DATETIME('now') BETWEEN DATETIME([from]) AND DATETIME([to])")
    abstract fun getAllCurrentRaw(): List<BadestedForecast>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun putBadested(badested: Badested)
}