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
) : Parcelable {

    /**
     * Returns the resource id of the icon that best summarises the LocationForecast
     */
    fun getIcon(): Int? {
        return forecast?.getIcon()
    }


    fun getDisplayedBadested() : DisplayedBadested {

        // TODO: Gjør null-check
        return DisplayedBadested(
            name = badested.name,
            info = badested.info,
            facilities = badested.facilities,
            waterTempC =  forecast?.waterTempC.toString() + "°",
            airTempC =  forecast?.airTempC.toString() + "°",
            precipitation = forecast?.precipitation?.toInt().toString() + " mm",
            wind = forecast?.getWindDescription() ?: "",
            icon = getIcon(),
            to = "Varselet gjelder til kl. " + getHour(forecast?.to ?: ""),
            image = badested.image
        )
    }

    fun sameContentAs(newItem: BadestedForecast): Boolean {
        //@TODO
        return false;
    }


}



