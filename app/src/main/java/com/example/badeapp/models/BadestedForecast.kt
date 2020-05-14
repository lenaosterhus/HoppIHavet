package com.example.badeapp.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

private const val TAG = "DEBUG -BadestedForecast"

@Parcelize
data class BadestedForecast(
    @Embedded val badested: Badested,
    @Relation(
        parentColumn = "badestedId",
        entityColumn = "badestedId"
    )
    val forecast: List<Forecast>
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

    fun getWindDescription() = forecast.getOrNull(0)?.getWindDescription() ?: ""

    fun getAirTempCDescription() = forecast.getOrNull(0)?.getAirTempCDescription() ?: ""

    fun getWaterTempCDescription() = forecast.getOrNull(0)?.getWaterTempCDescription() ?: ""

    fun getPrecipitationDescription() = forecast.getOrNull(0)?.getPrecipitationDescription() ?: ""

    fun getValidToDescription() = forecast.getOrNull(0)?.getValidToDescription() ?: ""


    /**
     * Returns the resource id of the icon that best summarises the LocationForecast
     */
    fun getIcon(): Int? = forecast.getOrNull(0)?.getIcon()


    fun sameContentAs(other: BadestedForecast): Boolean {
        return badested == other.badested && forecast.getOrNull(0) == other.forecast.getOrNull(0)
    }

    override fun toString(): String {
        return "BadestedForecast(badested=${badested.name}, forecast=$forecast)"
    }


}



