package com.example.badeapp.api.LocationForecast


import android.util.Log
import com.example.badeapp.models.LocationForecastInfo
import com.example.badeapp.util.inTheFutureFromNow
import com.example.badeapp.util.minBetween
import com.example.badeapp.util.toGmtIsoString
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

private const val TAG = "DEBUG - LFRFormat"
private const val NEXT_UPDATE_WHEN_NO_NEXTISSUE_MIN = 20L

// // created = når data ble hentet ISO
internal data class ResponseFormat(
    @Expose @SerializedName("product") val product: Product?,
    @Expose @SerializedName("created") val created: String?,
    @Expose @SerializedName("meta") val meta: Meta?
) {

    fun summarise(): LocationForecastInfo {

        val luftTempC: Double? =9.9 //@TODO remove
        val symbol: Int? = 1  //@TODO remove

        //Set next issue time to the given time or at NEXT_UPDATE... time (20 min)
        val nextIssue: String = meta?.model?.nextrun ?: inTheFutureFromNow(NEXT_UPDATE_WHEN_NO_NEXTISSUE_MIN).toGmtIsoString()

        //val timeList = getHourlyForecasts()
        //val hourlyForecasts = merge(timeList)

        return LocationForecastInfo(luftTempC, symbol, nextIssue)
    }

    /*
    private fun merge(timeList: List<Time>): List<LocationForecastInfo.Forecast> {
        var grouped = timeList.groupBy { time -> time.from}.values
        grouped.map{
            group -> summarise(group)
        }
    }
    */

    /**
     *  Returns a list of today's hourly forecasts
     */
    private fun getHourlyForecasts(): List<Time>? {
        val returnedList = product?.time?.toMutableList() ?: return null
        return returnedList
            .filter {
                it.durationMin() < 61L //Make sure only the ones that last a  hour are included
            }.sortedBy {
                it.from
            }
    }

    /** @TODO remove
     * When viewing a location you expect a icon showing weather status (cloudy, sunny etc..)
     * MI assigns different symbols for every integer.
     * @TODO: Flyttes til LocationForecastInfo

    private fun getCurrentSymbolNumber(): Int? {
        product?.time?.get(0)?.let {
            return it.location?.symbol?.number?.toInt()
        }
        return null
    }
    */

    /* @TODO REMOVE
    private fun getCurrentAirTemp(): Double? {
        product?.time?.get(0)?.let {
            return it.location?.temperature?.value?.toDouble()
        }
        return null
    }
    */
}

// ----- TOP LEVEL -----
// Container for data - classy = class i JSON
internal data class Product(
    @Expose @SerializedName("time") val time: List<Time>?,
    @Expose @SerializedName("class") val classy: String?
)

// Container for metadata
internal data class Meta(
    @Expose @SerializedName("model") val model: Model?
)

// ----- -----
// Hoveddelen av Product:
// datatype = "forcast", from & to = tidspunkt ISO, location = værdata
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

    // @TODO: Noen av datoene blir parset feil! Sjekk logging
    fun durationMin(): Long {
        val diff = minBetween(from,to)

        if (diff == null ) {
            Log.e(TAG, "Tidsintervall diff ikke funnet for:\n\tTo:   $to,\n\tFrom: $from\n")
        } else if ( diff < 0L) {
            Log.e(TAG, "Tidsintervall diff mindre enn 0 for:\n\tTo:   $to,\n\tFrom: $from\n\tdiff: $diff\n")
        }
        return diff!!
    }
}

// metadata for sporringen.
// nextrun = VIKTIG
// termin = Siste hele klokkeslett
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
        return "Location(temperature=$temperature, cloudiness=$cloudiness, windSpeed=$windSpeed, precipitation=$precipitation)"
    }
}

// id er optional
internal data class Temperature(
    @Expose @SerializedName("unit") val unit: String?,
    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("value") val value: String?
)


// Element denoting the wind speed by name, at 10 m above ground, in meters per second or the Beaufort scale.
// Oppgis enten i baufort (0-12 egen skala med navn og virkning på sjo) eller mps
internal data class WindSpeed(
    @Expose @SerializedName("beaufort") val beaufort: String?,
    @Expose @SerializedName("mps") val mps: String?,
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("id") val id: String?
)

// id = Irrelevant, gis i enten percent eller eights
internal data class Cloudiness(

    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("percent") val percent: String?,
    @Expose @SerializedName("eights") val eights: Int?
)


// ----- FORELOPIG IRRELEVANT -----
// id = Irrelevant, unit = Celcius?
internal data class DewpointTemperature(
    @Expose @SerializedName("value") val value: String?,
    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("unit") val unit: String?
)

// id = Irrelevant,
internal data class Fog(
    @Expose @SerializedName("percent") val percent: String?,
    @Expose @SerializedName("id") val id: String?
)

// id = Irrelevant
internal data class HighClouds(
    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("percent") val percent: String?
)

// unit = %
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

internal data class Precipitation(
    @Expose @SerializedName("unit") val unit: String?,
    @Expose @SerializedName("maxvalue") val maxvalue: String?,
    @Expose @SerializedName("value") val value: String?,
    @Expose @SerializedName("minvalue") val minvalue: String?
)

internal data class Pressure(
    @Expose @SerializedName("unit") val unit: String?,
    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("value") val value: String?
)

// id = "cloud", "sun" etc., number = id fra WeatherIcon API
internal data class Symbol(
    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("number") val number: String?
)

internal data class TemperatureProbability(
    @Expose @SerializedName("value") val value: String?,
    @Expose @SerializedName("unit") val unit: String?
)

internal data class WindDirection(
    @Expose @SerializedName("deg") val deg: String?,
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("id") val id: String?
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
