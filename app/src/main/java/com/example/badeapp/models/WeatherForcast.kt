package com.example.badeapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

object WeatherForcast {
    // created = når data ble hentet ISO
    data class Container(val product: Product?, val created: String?, val meta: Meta?)

    // ----- TOP LEVEL -----
// Container for data - classy = class i JSON
    data class Product(
        @Expose @SerializedName("time")     val time: List<Time>?,
        @Expose @SerializedName("class")    val classy: String?
    )


    // Container for metadata
    data class Meta(
        @Expose  @SerializedName("model")    val model: Model?
    )

    // ----- -----
// Hoveddelen av Product:
// datatype = "forcast", from & to = tidspunkt ISO, location = værdata
    data class Time(
        @Expose @SerializedName("datatype")    val datatype: String?,
        @Expose @SerializedName("from")        val from: String?,
        @Expose  @SerializedName("location")   val location: Location?,
        @Expose  @SerializedName("to")         val to: String?
    )

    // metadata for sporringen.
// nextrun = VIKTIG
// termin = Siste hele klokkeslett
    data class Model(
        @Expose @SerializedName("nextrun")   val nextrun: String?,
        @Expose @SerializedName("name")      val name: String?,
        @Expose @SerializedName("from")      val from: String?,
        @Expose @SerializedName("to")        val to: String?,
        @Expose @SerializedName("termin")    val termin: String?,
        @Expose  @SerializedName("runended") val runended: String?

    )

    // Værdata på location
    data class Location(
        @Expose @SerializedName("windDirection")   val windDirection: WindDirection?,
        @Expose @SerializedName("humidity")        val humidity: Humidity?,
        @Expose @SerializedName("temperature")     val temperature: Temperature?,
        @Expose @SerializedName("mediumClouds")    val mediumClouds: MediumClouds?,
        @Expose @SerializedName("cloudiness")      val cloudiness: Cloudiness?,
        @Expose @SerializedName("windSpeed")       val windSpeed: WindSpeed?,
        @Expose @SerializedName("temperatureProbability") val temperatureProbability: TemperatureProbability?,
        @Expose @SerializedName("altitude")        val altitude: String?,
        @Expose @SerializedName("highClouds")      val highClouds: HighClouds?,
        @Expose @SerializedName("pressure")        val pressure: Pressure?,
        @Expose @SerializedName("longitude")       val longitude: String?,
        @Expose @SerializedName("windGust")        val windGust: WindGust?,
        @Expose @SerializedName("fog")             val fog: Fog?,
        @Expose @SerializedName("dewpointTemperature") val dewpointTemperature: DewpointTemperature?,
        @Expose @SerializedName("latitude")        val latitude: String?,
        @Expose @SerializedName("windProbability") val windProbability: WindProbability?,
        @Expose @SerializedName("lowClouds")       val lowClouds: LowClouds?
    )
    // id er optional
    data class Temperature(
        @Expose @SerializedName("unit")  val unit: String?,
        @Expose @SerializedName("id")    val id: String?,
        @Expose @SerializedName("value") val value: String?
    )

    // Element denoting the wind speed by name, at 10 m above ground, in meters per second or the Beaufort scale.
// Oppgis enten i baufort (0-12 egen skala med navn og virkning på sjo) eller mps
    data class WindSpeed(
        @Expose @SerializedName("beaufort") val beaufort: String?,
        @Expose @SerializedName("mps")      val mps: String?,
        @Expose @SerializedName("name")     val name: String?,
        @Expose @SerializedName("id")       val id: String?
    )

    // id = Irrelevant, gis i enten percent eller eights
    data class Cloudiness(
        @Expose @SerializedName("id")       val id: String?,
        @Expose @SerializedName("percent")  val percent: String?,
        @Expose @SerializedName("eights")   val eights: Int?
    )


    // ----- FORELOPIG IRRELEVANT -----
// id = Irrelevant, unit = Celcius?
    data class DewpointTemperature(
        @Expose @SerializedName("value")    val value: String?,
        @Expose @SerializedName("id")       val id: String?,
        @Expose @SerializedName("unit")     val unit: String?
    )

    // id = Irrelevant,
    data class Fog(
        @Expose @SerializedName("percent")  val percent: String?,
        @Expose @SerializedName("id")       val id: String?
    )

    // id = Irrelevant
    data class HighClouds(
        @Expose @SerializedName("id")       val id: String?,
        @Expose @SerializedName("percent")  val percent: String?
    )

    // unit = %
    data class Humidity(
        @Expose @SerializedName("value")    val value: String?,
        @Expose @SerializedName("unit")     val unit: String?
    )

    data class LowClouds(
        @Expose @SerializedName("percent")  val percent: String?,
        @Expose @SerializedName("id")       val id: String?
    )

    data class MaxTemperature(
        @Expose @SerializedName("id")       val id: String?,
        @Expose @SerializedName("unit")     val unit: String?,
        @Expose @SerializedName("value")    val value: String?
    )

    data class MediumClouds(
        @Expose @SerializedName("percent")  val percent: String?,
        @Expose @SerializedName("id")       val id: String?
    )

    data class MinTemperature(
        @Expose @SerializedName("value")    val value: String?,
        @Expose @SerializedName("unit")     val unit: String?,
        @Expose @SerializedName("id")       val id: String?
    )

    data class Precipitation(
        @Expose @SerializedName("unit")     val unit: String?,
        @Expose @SerializedName("maxvalue") val maxvalue: String?,
        @Expose @SerializedName("value")    val value: String?,
        @Expose @SerializedName("minvalue") val minvalue: String?
    )

    data class Pressure(
        @Expose @SerializedName("unit")     val unit: String?,
        @Expose @SerializedName("id")       val id: String?,
        @Expose @SerializedName("value")    val value: String?
    )

    // id = "cloud", "sun" etc., number = id fra WeatherIcon API
    data class Symbol(
        @Expose @SerializedName("id")       val id: String?,
        @Expose @SerializedName("number")   val number: String?
    )

    data class SymbolProbability(
        @Expose @SerializedName("unit")     val unit: String?,
        @Expose @SerializedName("value")    val value: String?
    )

    data class TemperatureProbability(
        @Expose @SerializedName("value")    val value: String?,
        @Expose @SerializedName("unit")     val unit: String?
    )

    data class WindDirection(
        @Expose @SerializedName("deg")      val deg: String?,
        @Expose @SerializedName("name")     val name: String?,
        @Expose @SerializedName("id")       val id: String?
    )

    data class WindGust(
        @Expose @SerializedName("mps")      val mps: String?,
        @Expose @SerializedName("id")       val id: String?
    )

    data class WindProbability(
        @Expose @SerializedName("unit")     val unit: String?,
        @Expose @SerializedName("value")    val value: String?
    )
}