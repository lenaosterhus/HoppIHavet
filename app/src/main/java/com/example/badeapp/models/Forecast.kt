package com.example.badeapp.models

import android.os.Parcelable
import androidx.room.Entity
import com.example.badeapp.R
import com.example.badeapp.util.currentTime
import com.example.badeapp.util.getHour
import com.example.badeapp.util.parseAsGmtIsoDate
import kotlinx.android.parcel.Parcelize
import kotlin.math.roundToInt


@Parcelize
@Entity(
    primaryKeys = ["badestedId", "from", "to"]
)
data class Forecast(
    val badestedId : Int,
    val from: String,
    val to: String,
    val nextIssueLocation: String?,
    val nextIssueOcean: String?,
    val airTempC: Double?,
    val symbol: Int?,
    val precipitation: Double?,
    val windDirection: String?,
    val windSpeedMps: Double?,
    val windSpeedName: String?,
    val waterTempC : Double?,
    val createdLocation : String?,
    val createdOcean : String?
) : Parcelable {


    /**
     * Returns the resource id of the icon that best summarises the Forecast
     */
    fun getIcon(): Int? {
        val isDay = true //@TODO figure out if it is daytime aka is the sun up?

        if (isDay) {
            when (symbol) {
                1 -> return R.mipmap.wic_day_1 //Sun
                2 -> return R.mipmap.wic_day_2 //LightCloud
                3 -> return R.mipmap.wic_day_3 //PartlyCloud
                4 -> return R.mipmap.wic_day_4 //Cloud
                5 -> return R.mipmap.wic_day_5 //LightRainSun
                6 -> return R.mipmap.wic_day_6 //LightRainThunderSun
                7 -> return R.mipmap.wic_day_7 //SleetSun
                8 -> return R.mipmap.wic_day_8 //SnowSun
                9 -> return R.mipmap.wic_day_9 //LightRain
                10 -> return R.mipmap.wic_day_10 //Rain
                11 -> return R.mipmap.wic_day_11 //RainThunder
                12 -> return R.mipmap.wic_day_12 //Sleet
                13 -> return R.mipmap.wic_day_13 //Snow
                14 -> return R.mipmap.wic_day_14 //SnowThunder
                15 -> return R.mipmap.wic_day_15 //Fog
                20 -> return R.mipmap.wic_day_20 //SleetSunThunder
                21 -> return R.mipmap.wic_day_21 //SnowSunThunder
                22 -> return R.mipmap.wic_day_22 //LightRainThunder
                23 -> return R.mipmap.wic_day_23 //SleetThunder
                24 -> return R.mipmap.wic_day_24 //DrizzleThunderSun
                25 -> return R.mipmap.wic_day_25 //RainThunderSun
                26 -> return R.mipmap.wic_day_26 //LightSleetThunderSun
                27 -> return R.mipmap.wic_day_27 //HeavySleetThunderSun
                28 -> return R.mipmap.wic_day_28 //LightSnowThunderSun
                29 -> return R.mipmap.wic_day_29 //HeavySnowThunderSun
                30 -> return R.mipmap.wic_day_30 //DrizzleThunder
                31 -> return R.mipmap.wic_day_31 // LightSleetThunder
                32 -> return R.mipmap.wic_day_32 //HeavySleetThunder
                33 -> return R.mipmap.wic_day_33 //LightSnowThunder
                34 -> return R.mipmap.wic_day_34 //HeavySnowThunder
                40 -> return R.mipmap.wic_day_40 //DrizzleSun
                41 -> return R.mipmap.wic_day_41 //RainSun
                42 -> return R.mipmap.wic_day_42 //LightSleetSun
                43 -> return R.mipmap.wic_day_43 //HeavySleetSun
                44 -> return R.mipmap.wic_day_44 //LightSnowSun
                45 -> return R.mipmap.wic_day_45 //HeavysnowSun
                46 -> return R.mipmap.wic_day_46 //Drizzle
                47 -> return R.mipmap.wic_day_47 //LightSleet
                48 -> return R.mipmap.wic_day_48 //HeavySleet
                49 -> return R.mipmap.wic_day_49 //LightSnow
                50 -> return R.mipmap.wic_day_50 //HeavySnow
            }
        } else {
            when (symbol) {
                1 -> return R.mipmap.wic_night_1 //Sun
                2 -> return R.mipmap.wic_night_2 //LightCloud
                3 -> return R.mipmap.wic_night_3 //PartlyCloud
                4 -> return R.mipmap.wic_night_4 //Cloud
                5 -> return R.mipmap.wic_night_5 //LightRainSun
                6 -> return R.mipmap.wic_night_6 //LightRainThunderSun
                7 -> return R.mipmap.wic_night_7 //SleetSun
                8 -> return R.mipmap.wic_night_8 //SnowSun
                9 -> return R.mipmap.wic_night_9 //LightRain
                10 -> return R.mipmap.wic_night_10 //Rain
                11 -> return R.mipmap.wic_night_11 //RainThunder
                12 -> return R.mipmap.wic_night_12 //Sleet
                13 -> return R.mipmap.wic_night_13 //Snow
                14 -> return R.mipmap.wic_night_14 //SnowThunder
                15 -> return R.mipmap.wic_night_15 //Fog
                20 -> return R.mipmap.wic_night_20 //SleetSunThunder
                21 -> return R.mipmap.wic_night_21 //SnowSunThunder
                22 -> return R.mipmap.wic_night_22 //LightRainThunder
                23 -> return R.mipmap.wic_night_23 //SleetThunder
                24 -> return R.mipmap.wic_night_24 //DrizzleThunderSun
                25 -> return R.mipmap.wic_night_25 //RainThunderSun
                26 -> return R.mipmap.wic_night_26 //LightSleetThunderSun
                27 -> return R.mipmap.wic_night_27 //HeavySleetThunderSun
                28 -> return R.mipmap.wic_night_28 //LightSnowThunderSun
                29 -> return R.mipmap.wic_night_29 //HeavySnowThunderSun
                30 -> return R.mipmap.wic_night_30 //DrizzleThunder
                31 -> return R.mipmap.wic_night_31 // LightSleetThunder
                32 -> return R.mipmap.wic_night_32 //HeavySleetThunder
                33 -> return R.mipmap.wic_night_33 //LightSnowThunder
                34 -> return R.mipmap.wic_night_34 //HeavySnowThunder
                40 -> return R.mipmap.wic_night_40 //DrizzleSun
                41 -> return R.mipmap.wic_night_41 //RainSun
                42 -> return R.mipmap.wic_night_42 //LightSleetSun
                43 -> return R.mipmap.wic_night_43 //HeavySleetSun
                44 -> return R.mipmap.wic_night_44 //LightSnowSun
                45 -> return R.mipmap.wic_night_45 //HeavysnowSun
                46 -> return R.mipmap.wic_night_46 //Drizzle
                47 -> return R.mipmap.wic_night_47 //LightSleet
                48 -> return R.mipmap.wic_night_48 //HeavySleet
                49 -> return R.mipmap.wic_night_49 //LightSnow
                50 -> return R.mipmap.wic_night_50 //HeavySnow

                // Polar (midnight sun/constant dark) values
                101 -> return R.mipmap.wic_night_101 //Sun (polar)
                102 -> return R.mipmap.wic_night_102 //LightCloud
                103 -> return R.mipmap.wic_night_103 //PartlyCloud
                105 -> return R.mipmap.wic_night_105 //LightRainSun
                106 -> return R.mipmap.wic_night_106 //LightRainThunderSun
                107 -> return R.mipmap.wic_night_107 //SleetSun
                108 -> return R.mipmap.wic_night_108
                120 -> return R.mipmap.wic_night_120 //SleetSunThunder
                121 -> return R.mipmap.wic_night_121 //SnowSunThunder
                124 -> return R.mipmap.wic_night_124 //DrizzleThunderSun
                125 -> return R.mipmap.wic_night_125 //RainThunderSun
                126 -> return R.mipmap.wic_night_126 //LightSleetThunderSun
                127 -> return R.mipmap.wic_night_127 //HeavySleetThunderSun
                128 -> return R.mipmap.wic_night_128 //LightSnowThunderSun
                129 -> return R.mipmap.wic_night_129 //
                141 -> return R.mipmap.wic_night_141 //RainSun
                142 -> return R.mipmap.wic_night_142 //LightSleetSun
                143 -> return R.mipmap.wic_night_143 //HeavySleetSun
                144 -> return R.mipmap.wic_night_144 //LightSnowSun
                145 -> return R.mipmap.wic_night_145 //HeavySnowSun
            }
        }
        return null
    }

    /**
     * Returns the resource id of the icon description that best summarises the Forecast
     */
    fun getIconDescription(): Int? {
        when (symbol) {
            1 -> return R.string.wic_1_desc //Sun
            2 -> return R.string.wic_2_desc //LightCloud
            3 -> return R.string.wic_3_desc //PartlyCloud
            4 -> return R.string.wic_4_desc //Cloud
            5 -> return R.string.wic_5_desc //LightRainSun
            6 -> return R.string.wic_6_desc //LightRainThunderSun
            7 -> return R.string.wic_7_desc //SleetSun
            8 -> return R.string.wic_8_desc //SnowSun
            9 -> return R.string.wic_9_desc //LightRain
            10 -> return R.string.wic_10_desc //Rain
            11 -> return R.string.wic_11_desc //RainThunder
            12 -> return R.string.wic_12_desc //Sleet
            13 -> return R.string.wic_13_desc //Snow
            14 -> return R.string.wic_14_desc //SnowThunder
            15 -> return R.string.wic_15_desc //Fog
            20 -> return R.string.wic_20_desc //SleetSunThunder
            21 -> return R.string.wic_21_desc //SnowSunThunder
            22 -> return R.string.wic_22_desc //LightRainThunder
            23 -> return R.string.wic_23_desc //SleetThunder
            24 -> return R.string.wic_24_desc //DrizzleThunderSun
            25 -> return R.string.wic_25_desc //RainThunderSun
            26 -> return R.string.wic_26_desc //LightSleetThunderSun
            27 -> return R.string.wic_27_desc //HeavySleetThunderSun
            28 -> return R.string.wic_28_desc //LightSnowThunderSun
            29 -> return R.string.wic_29_desc //HeavySnowThunderSun
            30 -> return R.string.wic_30_desc //DrizzleThunder
            31 -> return R.string.wic_31_desc // LightSleetThunder
            32 -> return R.string.wic_32_desc //HeavySleetThunder
            33 -> return R.string.wic_33_desc //LightSnowThunder
            34 -> return R.string.wic_34_desc //HeavySnowThunder
            40 -> return R.string.wic_40_desc //DrizzleSun
            41 -> return R.string.wic_41_desc //RainSun
            42 -> return R.string.wic_42_desc //LightSleetSun
            43 -> return R.string.wic_43_desc //HeavySleetSun
            44 -> return R.string.wic_44_desc //LightSnowSun
            45 -> return R.string.wic_45_desc //HeavysnowSun
            46 -> return R.string.wic_46_desc //Drizzle
            47 -> return R.string.wic_47_desc //LightSleet
            48 -> return R.string.wic_48_desc //HeavySleet
            49 -> return R.string.wic_49_desc //LightSnow
            50 -> return R.string.wic_50_desc //HeavySnow
        }

        return null
    }

    fun isOceanForecastOutdated(): Boolean {
        return nextIssueOcean?.parseAsGmtIsoDate()?.before(currentTime()) ?: true
    }

    fun isLocationForecastOutdated(): Boolean {
        return nextIssueLocation?.parseAsGmtIsoDate()?.before(currentTime()) ?: true
    }

    /**
     * --------------------------------------------------------------------------------------
     * The following methods are used to get prettified versions of data in the Forecast
     */

    fun getWindDescription() : String {
        val windDirectionLongString =
            when (windDirection) {
                "N" -> "nord"
                "E" -> "øst"
                "S" -> "sør"
                "W" -> "vest"

                "NE" -> "nordøst"
                "SE" -> "sørøst"
                "SW" -> "sørvest"
                "NW" -> "nordvest"

                else ->  {
                    return windSpeedName + ", " + windSpeedMps?.toInt().toString() + " m/s"
                }
            }

        return windSpeedName + ", " + windSpeedMps?.toInt().toString() + " m/s fra " + windDirectionLongString
    }

    fun getAirTempCDescription() : String {
        if(airTempC == null) return ""
        return "${airTempC.roundToInt()}°"
    }

    fun getWaterTempCDescription() : String {
        if(waterTempC == null) return ""
        return "${waterTempC.roundToInt()}°"
    }

    fun getPrecipitationDescription() : String {
        if(precipitation == null) return ""
        return precipitation.roundToInt().toString() + " mm"
    }
    
    fun getValidToDescription() : String {
        return "Varselet gjelder til kl. " + getHour(to)
    }

    /**
     * --------------------------------------------------------------------------------------
     * The following methods are used for testing.
     */

    fun contains(locationForecast: LocationForecast): Boolean {
        return locationForecast.badestedId == badestedId
                && locationForecast.from == from
                && locationForecast.to == to
                && locationForecast.nextIssueLocation == nextIssueLocation
                && locationForecast.airTempC == airTempC
                && locationForecast.symbol == symbol
                && locationForecast.precipitation == precipitation
                && locationForecast.windDirection == windDirection
                && locationForecast.windSpeedMps == windSpeedMps
                && locationForecast.windSpeedName == windSpeedName
    }

    fun contains(oceanForecast: OceanForecast): Boolean {
        return oceanForecast.badestedId == badestedId
                && oceanForecast.from == from
                && oceanForecast.to == to
                && oceanForecast.nextIssueOcean == nextIssueOcean
                && oceanForecast.nextIssueOcean == nextIssueOcean
    }

    fun hasLocationData(): Boolean {
        return nextIssueLocation != null && nextIssueLocation.isNotBlank()
                && airTempC != null
                && symbol != null
                && precipitation != null
                && windDirection != null
                && windSpeedMps != null
                && windSpeedName != null
    }

    fun hasOceanData(): Boolean {
        return nextIssueOcean != null && nextIssueOcean.isNotBlank()
                && waterTempC != null
    }
}