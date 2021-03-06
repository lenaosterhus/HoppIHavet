package com.example.badeapp.api.locationForecast


import android.util.Log
import com.example.badeapp.models.Badested
import com.example.badeapp.models.LocationForecast
import com.example.badeapp.util.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

private const val TAG = "LocationRespFormat"

internal data class LocationResponseFormat(
    @Expose @SerializedName("product") val product: Product?,
    @Expose @SerializedName("created") val created: String?,
    @Expose @SerializedName("meta") val meta: Meta?
) {
    /**
     * Returns a location forecast info object that is a nicer summary of the data present in this class.
     * As of this time it contains two values. The nextIssue, that says when we are allowed to
     * request new forecasts (for this location), and a list of LocationForecastInfo.Forecasts.
     * The forecasts are a summary of this class's Time objects, containing the
     */
    fun summarise(badested: Badested): List<LocationForecast>? {

        //Set next issue time to the given time or at NEXT_UPDATE... time (20 min)
        val nextIssue: String = meta?.model?.nextrun ?: inTheFutureFromNow(
            NEXT_UPDATE_WHEN_NO_NEXTISSUE_MIN
        ).toGmtIsoString()

        //Get when response was created
        val createdLocation = created ?: currentTime().toGmtIsoString()

        // Get list of hourly or instant summarised forecasts
        val timeList = getHourlyForecasts()?.map { time ->
            time.summariseTime(badested, nextIssue,createdLocation)
        }?.toMutableList() ?: return null



        /*
        Now there are some forecasts that overlap. One forecast goes from 11 -> 12, while the other
        shows a snapshot from 12->12. We need to combine them because some of them might hold values
        the other lacks, like the snapshot holding the symbol.
        */

        val noTimeSpan = timeList.filter { time -> time.from == time.to }
        val oneHourSpan = timeList.filter { time -> time.from != time.to }.toMutableList()

        for ((index, value) in oneHourSpan.withIndex()) {
            for (other in noTimeSpan) {
                if (value.from == other.from || value.to == other.to) {
                    oneHourSpan[index] = LocationForecast(
                        badested.id,
                        value.from,
                        value.to,
                        nextIssue,
                        value.airTempC ?: other.airTempC,
                        value.symbol ?: other.symbol,
                        value.precipitation ?: other.precipitation,
                        value.windDirection ?: other.windDirection,
                        value.windSpeedMps ?: other.windSpeedMps,
                        value.windSpeedName ?: other.windSpeedName,
                        createdLocation
                    )
                    break
                }
            }
        }
        if (oneHourSpan.isNullOrEmpty()) return null

        return oneHourSpan
    }


    /**
     *  Returns a list of the hourlyForecasts
     */
    private fun getHourlyForecasts(): List<Time>? {
        val returnedList = product?.time?.toMutableList() ?: return null
        return returnedList
            .filter {
                it.durationMin() < 61L // Make sure only the ones that last a hour are included
            }
    }
}

// ----- TOP LEVEL -----
internal data class Product(
    @Expose @SerializedName("time") val time: List<Time>?,
    @Expose @SerializedName("class") val classy: String?
)

internal data class Meta(
    @Expose @SerializedName("model") val model: Model?
)

// ----- -----
internal data class Time(
    @Expose @SerializedName("datatype") val datatype: String?,
    @Expose @SerializedName("from") val from: String,
    @Expose @SerializedName("to") val to: String,
    @Expose @SerializedName("location") val location: Location?
) {
    // For logcat
    override fun toString(): String {
        return "\nTime(from=$from, to=$to, location=$location)"
    }

    fun summariseTime(badested: Badested, nextIssue: String, created: String): LocationForecast {
        val symbol = location?.getSymbol()
        val airTempC = location?.getAirTempC()
        val precipitationMm = location?.precipitation?.value
        val windDirection = location?.windDirection?.name
        val windSpeedMps = location?.windSpeed?.mps
        val windSpeedName = location?.windSpeed?.name


        return LocationForecast(
            badested.id,
            from,
            to,
            nextIssue,
            airTempC,
            symbol,
            precipitationMm,
            windDirection,
            windSpeedMps,
            windSpeedName,
            created
        )
    }


    fun durationMin(): Long {
        val diff = minBetween(from, to)
        return diff!!
    }
}


internal data class Model(
    @Expose @SerializedName("nextrun") val nextrun: String?,
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("from") val from: String?,
    @Expose @SerializedName("to") val to: String?,
    @Expose @SerializedName("termin") val termin: String?,
    @Expose @SerializedName("runended") val runended: String?
)

// Værdata på location
internal data class Location(
    @Expose @SerializedName("windDirection") val windDirection: WindDirection?,
    @Expose @SerializedName("humidity") val humidity: Humidity?,
    @Expose @SerializedName("temperature") val temperature: Temperature?,
    @Expose @SerializedName("mediumClouds") val mediumClouds: MediumClouds?,
    @Expose @SerializedName("cloudiness") val cloudiness: Cloudiness?,
    @Expose @SerializedName("windSpeed") val windSpeed: WindSpeed?,
    @Expose @SerializedName("temperatureProbability") val temperatureProbability: TemperatureProbability?,
    @Expose @SerializedName("altitude") val altitude: String?,
    @Expose @SerializedName("highClouds") val highClouds: HighClouds?,
    @Expose @SerializedName("pressure") val pressure: Pressure?,
    @Expose @SerializedName("longitude") val longitude: String?,
    @Expose @SerializedName("windGust") val windGust: WindGust?,
    @Expose @SerializedName("fog") val fog: Fog?,
    @Expose @SerializedName("dewpointTemperature") val dewpointTemperature: DewpointTemperature?,
    @Expose @SerializedName("latitude") val latitude: String?,
    @Expose @SerializedName("windProbability") val windProbability: WindProbability?,
    @Expose @SerializedName("lowClouds") val lowClouds: LowClouds?,
    @Expose @SerializedName("groundCover") val groundCover: GroundCover?,
    @Expose @SerializedName("maximumPrecipitation") val maximumPrecipitation: Precipitation?,
    @Expose @SerializedName("precipitation") val precipitation: Precipitation?,
    @Expose @SerializedName("minTemperature") val minTemperature: Temperature?,
    @Expose @SerializedName("minTemperatureDay") val minTemperatureDay: Temperature?,
    @Expose @SerializedName("minTemperatureNight") val minTemperatureNight: Temperature?,
    @Expose @SerializedName("maxTemperature") val maxTemperature: Temperature?,
    @Expose @SerializedName("maxTemperatureDay") val maxTemperatureDay: Temperature?,
    @Expose @SerializedName("maxTemperatureNight") val maxTemperatureNight: Temperature?,
    @Expose @SerializedName("uv") val uv: Uv?,
    @Expose @SerializedName("tidalwater") val tidalwater: Tidalwater?,
    @Expose @SerializedName("currentDirection") val currentDirection: UnitValue?,
    @Expose @SerializedName("maxWaveHeight") val maxWaveHeight: UnitValue?,
    @Expose @SerializedName("surfaceTemperature") val surfaceTemperature: UnitValue?,
    @Expose @SerializedName("waveDirection") val waveDirection: UnitValue?,
    @Expose @SerializedName("wavePeriod") val wavePeriod: UnitValue?,
    @Expose @SerializedName("waveHeight") val waveHeight: UnitValue?,
    @Expose @SerializedName("bias") val bias: UnitValue?,
    @Expose @SerializedName("numberofobservations") val numberOfObservations: UnitValue?,
    @Expose @SerializedName("meanabsoluteerror") val meanAbsoluteError: UnitValue?,
    @Expose @SerializedName("score") val score: Score?,
    @Expose @SerializedName("maxWindSpeed") val maxWindSpeed: WindSpeed?,
    @Expose @SerializedName("areaMaxWindSpeed") val areaMaxWindSpeed: WindSpeed?,
    @Expose @SerializedName("stateOfTheSea") val stateOfTheSea: StateOfTheSea?,
    @Expose @SerializedName("snowDepth") val snowDepth: SnowDepth?,
    @Expose @SerializedName("weather") val weather: Weather?,
    @Expose @SerializedName("symbol") val symbol: Symbol?,
    @Expose @SerializedName("forest-fire") val forestFire: UnitValue?,
    @Expose @SerializedName("symbolProbability") val symbolProbability: UnitValue?
) {
    // For logcat
    override fun toString(): String {
        return "Location(temperature=$temperature, windSpeed=$windSpeed, precipitation=$precipitation)"
    }

    /**
     * Gets the number representing the weather icon in symbol that summarises the forecast.
     * It seems that MI is undergoing some api changes, and are moving the
     * weather icon around. This method looks around for where it could be.
     */
    fun getSymbol(): Int? {

        weather?.symbol?.let { return it }
        symbol?.number?.let { return it }

        return null
    }

    fun getAirTempC(): Double? {

        val unit = temperature?.unit
        val temp = temperature?.value?.toDouble()

        when {
            unit == "celsius" && temp != null -> {
                //Log.d(TAG, "Returning $temp")
                return temp
            }

            unit != null -> {
                Log.e(TAG, "The given unit for temperature was unexpected. $unit != expected, celsius"
                )
            }
            temp != null -> {
                Log.e(TAG, "The unit for temperature was not given (null), assuming celsius!")
                return temp
            }
        }
        return null
    }
}

internal data class Temperature(
    @Expose @SerializedName("unit") val unit: String?,
    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("value") val value: String?
)


// Element denoting the wind speed by name, at 10 m above ground, in meters per second or the Beaufort scale.
// Oppgis enten i baufort (0-12 egen skala med navn og virkning på sjo) eller mps
internal data class WindSpeed(
    @Expose @SerializedName("beaufort") val beaufort: String?,
    @Expose @SerializedName("mps") val mps: Double?,
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("id") val id: String?
)

internal data class Precipitation(
    @Expose @SerializedName("unit") val unit: String?,
    @Expose @SerializedName("maxvalue") val maxvalue: String?,
    @Expose @SerializedName("value") val value: Double?,
    @Expose @SerializedName("minvalue") val minvalue: String?
)

internal data class Symbol(
    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("number") val number: Int?
)

internal data class WindDirection(
    @Expose @SerializedName("deg") val deg: String?,
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("id") val id: String?
)


// ----- FORELOPIG IRRELEVANT -----

internal data class Cloudiness(
    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("percent") val percent: String?,
    @Expose @SerializedName("eights") val eights: Int?
)

internal data class DewpointTemperature(
    @Expose @SerializedName("value") val value: String?,
    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("unit") val unit: String?
)

internal data class Fog(
    @Expose @SerializedName("percent") val percent: String?,
    @Expose @SerializedName("id") val id: String?
)

internal data class HighClouds(
    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("percent") val percent: String?
)

internal data class Humidity(
    @Expose @SerializedName("value") val value: String?,
    @Expose @SerializedName("unit") val unit: String?
)

internal data class LowClouds(
    @Expose @SerializedName("percent") val percent: String?,
    @Expose @SerializedName("id") val id: String?
)

internal data class MediumClouds(
    @Expose @SerializedName("percent") val percent: String?,
    @Expose @SerializedName("id") val id: String?
)

internal data class Pressure(
    @Expose @SerializedName("unit") val unit: String?,
    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("value") val value: String?
)

internal data class TemperatureProbability(
    @Expose @SerializedName("value") val value: String?,
    @Expose @SerializedName("unit") val unit: String?
)

internal data class WindGust(
    @Expose @SerializedName("mps") val mps: String?,
    @Expose @SerializedName("id") val id: String?
)

internal data class WindProbability(
    @Expose @SerializedName("unit") val unit: String?,
    @Expose @SerializedName("value") val value: String?
)

internal data class GroundCover(
    @Expose @SerializedName("number") val number: Int, // 0 - 9
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("id") val id: String?
)

internal data class Weather(
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("number") val number: Int, //0-99
    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("symbol") val symbol: Int?
)

internal data class SnowDepth(
    @Expose @SerializedName("cm") val cm: Double,
    @Expose @SerializedName("id") val id: String?
)

internal data class StateOfTheSea(
    @Expose @SerializedName("number") val number: Int, // 0-9
    @Expose @SerializedName("metre") val metre: String?,
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("id") val id: String?
)

internal data class Score(
    @Expose @SerializedName("unit") val unit: String?,
    @Expose @SerializedName("overall") val overall: Int?,
    @Expose @SerializedName("very_good") val veryGood: Int?,
    @Expose @SerializedName("good") val good: Int?,
    @Expose @SerializedName("mediocre") val mediocre: Int?
)

internal data class Uv(
    @Expose @SerializedName("uvi_clear") val uviClear: UnitValue,
    @Expose @SerializedName("uvi_partly_cloudy") val uviPartlyCloudy: UnitValue,
    @Expose @SerializedName("uvi_cloudy") val uviCloudy: UnitValue,
    @Expose @SerializedName("uvi_forecast") val uviForecast: UnitValue,
    @Expose @SerializedName("ozon") val ozone: UnitValue,
    @Expose @SerializedName("snowcover") val snowCover: UnitValue,
    @Expose @SerializedName("cloudcover") val cloudCover: UnitValue,
    @Expose @SerializedName("albedo") val albedo: UnitValue,
    @Expose @SerializedName("solar_zenith") val solar_zenith: UnitValue
)

internal data class UnitValue(
    @Expose @SerializedName("unit") val unit: String,
    @Expose @SerializedName("value") val value: String
)

internal data class Tidalwater(
    @Expose @SerializedName("unit") val unit: String?,
    @Expose @SerializedName("tidal") val tidal: Int,
    @Expose @SerializedName("weathercorrection") val weatherCorrection: Int?
)
