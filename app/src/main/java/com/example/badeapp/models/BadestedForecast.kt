package com.example.badeapp.models

import android.os.Parcelable
import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.liesBetweneInclusive
import com.example.badeapp.util.parseAsGmtIsoDate
import kotlinx.android.parcel.Parcelize

private const val TAG = "BadestedForecast"

@Parcelize
@DatabaseView(
    "SELECT * FROM Badested LEFT JOIN Forecast on Badested.id = Forecast.badestedId WHERE  DATETIME('now','+5 minutes')  BETWEEN DATETIME([from]) AND DATETIME([to])"
)
data class BadestedForecast(
    @Embedded val badested: Badested,
    @Embedded val forecast: Forecast?
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

    fun getIconDescription(): Int? = forecast?.getIconDescription()


    fun sameContentAs(other: BadestedForecast): Boolean {
        return badested == other.badested && forecast == other.forecast
    }

    override fun toString(): String {
        return "BadestedForecast(badested=${badested.name}, forecast=$forecast)"
    }







}



