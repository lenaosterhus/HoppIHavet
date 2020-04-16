package com.example.badeapp.models

import android.util.Log
import com.example.badeapp.R
import com.example.badeapp.util.DATE_FORMAT
import java.util.*


private val TAG = "DEBUG - LocationInfo"

data class LocationForecastInfo(val luftTempC: Double?, val symbol: Int?, val nextIssue: String?) {

    fun isOutdated(): Boolean {
        return minUntilOutdated() < 10L
    }

    fun minUntilOutdated(): Long {
//        Log.d(TAG, "nextIssue: $nextIssue")

        val updateTime: Date? = DATE_FORMAT.parse(nextIssue)
        val currentTime = Date()
//            Log.d(TAG, "updateTime: $updateTime")
//            Log.d(TAG, "currentTime: $currentTime")

        updateTime?.let {
            val diff = updateTime.time - currentTime.time // millisek
            val diffMin = diff / (1000 * 60) // min
//            Log.d(TAG, "diffMin: $diffMin")

            return diffMin
        }
        Log.d(TAG, "updateTime = null")
        return 0
    }

    private fun getCurrentSymbolNumber(): Int? {
//        getCurrentTime()?.let {
//            return it.location?.symbol?.number?.toInt()
//        }
        return null
    }



    /*
    *  This function looks at the weather data to determine what little icon best summarises
    *  the weather.
    */
    fun getIcon(isDay:Boolean = true): Int? {

        if (isDay) {
            when (symbol) {
                1  -> return R.mipmap.wic_1_day //Sun - @TODO!
                2  -> return R.mipmap.wic_2_day //LightCloud - @TODO!
                3  -> return R.mipmap.wic_day_3 //PartlyCloud
                4  -> return R.mipmap.wic_day_4 //Cloud
                5  -> return R.mipmap.wic_day_5 //LightRainSun
                6  -> return R.mipmap.wic_day_6 //LightRainThunderSun
                7  -> return R.mipmap.wic_day_7 //SleetSun
                8  -> return R.mipmap.wic_day_8 //SnowSun
                9  -> return R.mipmap.wic_day_9 //LightRain
                10 -> return R.mipmap.wic_10_day //Rain - @TODO!
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
                1  -> return R.mipmap.wic_night_1 //Sun
                2  -> return R.mipmap.wic_night_2 //LightCloud
                3  -> return R.mipmap.wic_night_3 //PartlyCloud
                4  -> return R.mipmap.wic_night_4 //Cloud
                5  -> return R.mipmap.wic_night_5 //LightRainSun
                6  -> return R.mipmap.wic_night_6 //LightRainThunderSun
                7  -> return R.mipmap.wic_night_7 //SleetSun
                8  -> return R.mipmap.wic_night_8 //SnowSun
                9  -> return R.mipmap.wic_night_9 //LightRain
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

                //Polar (midnight sun/constant dark) values
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

        Log.w(TAG,"The given symbol ${symbol} is not mapped to a image!")
        return null

    }
}