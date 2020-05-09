package com.example.badeapp.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.badeapp.models.Badested
import com.example.badeapp.models.BadestedForecast

private const val GET_SUMMARYS = """
    SELECT 
            ${LF_TABLE}.badested,
            ${LF_TABLE}.[from] as 'from',
            ${LF_TABLE}.[to] as 'to',
            waterTempC,
            ${LF_TABLE}.airTempC,
            ${LF_TABLE}.symbol,
            ${LF_TABLE}.precipitation,
            ${LF_TABLE}.windDirection,
            ${LF_TABLE}.windSpeedMps,
            ${LF_TABLE}.windSpeedName
        FROM $LF_TABLE
            JOIN $OF_TABLE ON 
                    ${OF_TABLE}.badested = ${LF_TABLE}.badested
                AND ${OF_TABLE}.[from] = ${LF_TABLE}.[from]
                AND ${OF_TABLE}.[to] = ${LF_TABLE}.[to]
"""

private const val GET_SUMMARYS_FOR_ONE = GET_SUMMARYS + " WHERE $LF_TABLE.badested = :badested"

private const val GET_SUMMARIES_MANY = GET_SUMMARYS + " WHERE \n" +
        " DATETIME('now') BETWEEN DATETIME(${LF_TABLE}.[from]) AND DATETIME(${LF_TABLE}.[to])"


@Dao
interface BadestedForecastDao {


    @Query(GET_SUMMARYS_FOR_ONE)
    fun getAll(badested: Badested): LiveData<List<BadestedForecast>>

    @Query(GET_SUMMARYS_FOR_ONE)
    fun getAllRaw(badested: Badested): List<BadestedForecast>


    @Query(GET_SUMMARIES_MANY)
    fun getAllCurrent(): LiveData<List<BadestedForecast>>


    @Query(GET_SUMMARIES_MANY)
    fun getAllCurrentRaw(): List<BadestedForecast>


}