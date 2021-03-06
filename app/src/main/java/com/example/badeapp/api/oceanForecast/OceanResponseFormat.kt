package com.example.badeapp.api.oceanForecast

import android.util.Log
import com.example.badeapp.models.Badested
import com.example.badeapp.models.OceanForecast
import com.example.badeapp.util.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


private const val TAG = "OCEAN-RESPONSE-FORMAT"

internal data class OceanResponseFormat(
    @Expose @SerializedName("mox:forecastPoint") val forecastPoint: Point?,
    @Expose @SerializedName("mox:issueTime") val issueTime: IssueTime?,
    @Expose @SerializedName("mox:nextIssueTime") val nextIssueTime: NextIssueTime?,
    @Expose @SerializedName("mox:forecast") val forecast: List<Forecast>?
) {

    fun summarise(badested: Badested): List<OceanForecast>? {

        val nextIssue: String = nextIssueTime?.timeInstant?.timePosition ?: inTheFutureFromNow(
            NEXT_UPDATE_WHEN_NO_NEXTISSUE_MIN
        ).toGmtIsoString()

        val issue = issueTime?.timeInstant?.timePosition ?: currentTime().toGmtIsoString()

        val forecasts = forecast?.mapNotNull { it ->
            it.summariseForecast(badested, nextIssue, issue)
        }

        if (forecasts.isNullOrEmpty()) return null

        return forecasts
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
        // For logcat
        override fun toString(): String {
            return "\nForecast(forecast=$forecast)"
        }

        fun summariseForecast(badested: Badested, nextIssue: String, issueTime: String): OceanForecast? {

            val from = forecast?.validTime?.timePeriod?.begin
            val to = forecast?.validTime?.timePeriod?.end
            val waterTempC = forecast?.seaTemperature?.content?.toDouble()

            when {
                from == null && to == null -> {
                    Log.e(TAG, "Failed to get 'from'  and 'to' for the ocean forecast")
                    return null
                }

                from != to && to != null -> {
                     // The ocean forecast api looks like it only shows the data for a instance.
                     // So we expect from == to.
                    Log.e(TAG, "'from' != 'to'    $from != $to")
                    return OceanForecast(badested.id, from!!, to, nextIssue, waterTempC,issueTime)
                }
            }

            val instance = to ?: from
            val newFrom = instance!!.parseAsGmtIsoDate()!!.subOneHour().toGmtIsoString()

            return OceanForecast(badested.id, newFrom, to!!, nextIssue, waterTempC,issueTime)
        }
    }

    data class OceanForecastClass(
        @Expose @SerializedName("mox:seaTemperature") val seaTemperature: SeaTemperature,
        @Expose @SerializedName("mox:validTime") val validTime: ValidTime
    )

    data class SeaTemperature(
        @Expose @SerializedName("content") val content: String?,
        @Expose @SerializedName("uom") val uom: String?
    )

    data class SignificantTotalWaveHeight(
        @Expose @SerializedName("content") val content: String?
    )

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
