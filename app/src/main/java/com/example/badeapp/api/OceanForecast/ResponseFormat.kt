package com.example.badeapp.api.OceanForecast

import com.example.badeapp.models.OceanForecastInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*


internal data class ResponseFormat(
    @Expose @SerializedName("mox:forecastPoint") val forecastPoint: Point?,
    @Expose @SerializedName("mox:issueTime") val issueTime: IssueTime?,
    @Expose @SerializedName("mox:nextIssueTime") val nextIssueTime: NextIssueTime?,
    @Expose @SerializedName("mox:forecast") val forecast: List<Forecast>? // Objektene har ikke navn...?
) {

    fun summarize(lat: String, lon: String): OceanForecastInfo {

        val NEXT_UPDATE_WHEN_NO_NEXTISSUE = 20 * 60000
        //@TODO ikke bare ta første ellement
        val vannTempC = forecast?.get(0)?.forecast?.seaTemperature?.content?.toDouble()

        var nextIssue: String? = nextIssueTime?.timeInstant?.timePosition
        if (nextIssue == null) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.GERMANY)
            dateFormat.timeZone = TimeZone.getTimeZone("GMT")
            nextIssue =
                dateFormat.format(Date(System.currentTimeMillis() + NEXT_UPDATE_WHEN_NO_NEXTISSUE))
        }

        return OceanForecastInfo(lat, lon, vannTempC, nextIssue!!)
    }

    data class Point(
        @Expose @SerializedName("gml:pos") val pos: String? // Eks: 10.6654 59.9016 -2147483648
    )

    // ---- TIME -----
    data class IssueTime(
        @Expose @SerializedName("gml:TimeInstant") val timeInstant: TimeInstant
    )

    data class NextIssueTime(
        @Expose @SerializedName("gml:TimeInstant") val timeInstant: TimeInstant
    )

    // Format timePosition: "2020-04-02T14:00:00Z"
    data class TimeInstant(
        @Expose @SerializedName("gml:timePosition") val timePosition: String?
    )
    // ----  -----

    class Forecast(
        @Expose @SerializedName("metno:OceanForecast") val forecast: OceanForecastClass?
    ) {
        // Nødvendig for å få god utskrift til logcat
        override fun toString(): String {
            return "\nForecast(forecast=$forecast)"
        }
    }

    data class OceanForecastClass(
        @Expose @SerializedName("mox:seaTemperature") val seaTemperature: SeaTemperature,
        @Expose @SerializedName("mox:significantTotalWaveHeight") val significantTotalWaveHeight: SignificantTotalWaveHeight,
        @Expose @SerializedName("mox:validTime") val validTime: ValidTime
    )

    // uom = Cel
    data class SeaTemperature(
        @Expose @SerializedName("content") val content: String?,
        @Expose @SerializedName("uom") val uom: String?
    )

    data class SignificantTotalWaveHeight(
        @Expose @SerializedName("content") val content: String?
    )

    // uom = m/s
    data class SeaCurrentSpeed(
        @Expose @SerializedName("content") val content: String?,
        @Expose @SerializedName("uom") val uom: String?
    )

    data class SeaCurrentDirection(
        @Expose @SerializedName("content") val content: String?,
        @Expose @SerializedName("deg") val deg: String?
    )

    data class ValidTime(
        @Expose @SerializedName("gml:TimePeriod") val timePeriod: TimePeriod?
    )

    data class TimePeriod(
        @Expose @SerializedName("gml:begin") val begin: String?,
        @Expose @SerializedName("gml:end") val end: String?
    )
}
