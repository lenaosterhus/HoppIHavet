package com.example.badeapp.models

import android.content.res.Resources
import android.os.Parcelable
import android.util.Log
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.example.badeapp.R
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.getHour
import com.example.badeapp.util.liesBetweneInclusive
import com.example.badeapp.util.parseAsGmtIsoDate
import kotlinx.android.parcel.Parcelize

private const val TAG = "DEBUG -BadestedForecast"

@Parcelize
data class BadestedForecast(
    @Embedded val badested: Badested,
    @Relation(
        parentColumn = "id",
        entityColumn = "badestedId"
    )
    val forecast : Forecast?
)
    : Parcelable
{

    val name : String
        get() = badested.name

    val info : String
        get() = badested.info

    val facilities: String
        get() = badested.facilities

    val image : Int
        get() = badested.image

    fun getWindDescription() = forecast?.getWindDescription() ?: ""


    fun getAirTempCDescription() = forecast?.getAirTempCDescription() ?: ""

    fun getWaterTempCDescription() = forecast?.getWaterTempCDescription() ?: ""

    fun getPrecipitationDescription() = forecast?.getPrecipitationDescription() ?: ""

    fun getValidToDescription() = forecast?.getValidToDescription() ?: ""


    /**
     * Returns the resource id of the icon that best summarises the LocationForecast
     */
    fun getIcon(): Int? = forecast?.getIcon()



    fun sameContentAs(newItem: BadestedForecast): Boolean {
        //@TODO
        return false;
    }


}



