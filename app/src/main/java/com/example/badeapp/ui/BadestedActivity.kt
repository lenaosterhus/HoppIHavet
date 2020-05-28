package com.example.badeapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.badeapp.R
import com.example.badeapp.models.BadestedForecast
import kotlinx.android.synthetic.main.activity_badested.*


class BadestedActivity : AppCompatActivity() {

    private lateinit var badestedInView: BadestedForecast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_badested)

        if (intent.hasExtra("badestedForecast")) {
            val badestedForecast =
                intent.getParcelableExtra<BadestedForecast>("badestedForecast")
            badestedInView = badestedForecast!!
        }
        setView()
    }

    private fun setView() {

        ImageView_badested_image.setImageResource(badestedInView.image)

        /*
         * Setting text fields
         */
        TextView_badested_name.text = badestedInView.name

        TextView_badested_air_temp.text = badestedInView.getAirTempCDescription()
        TextView_badested_water_temp.text = badestedInView.getWaterTempCDescription()
        TextView_badested_precipitation.text = badestedInView.getPrecipitationDescription()
        TextView_badested_wind.text = badestedInView.getWindDescription()
        TextView_badested_valid_to.text = badestedInView.getValidToDescription()

        // Setting properties for the description TextView
        TextView_badested_description.text = badestedInView.info
        TextView_badested_facilities.text = badestedInView.facilities

        /*
         * Setting icon and icon description
         */
        val icon = badestedInView.getIcon()

        if (icon != null) {
            ImageView_badested_symbol.setImageResource(icon)
        } else {
            ImageView_badested_symbol.setImageDrawable(null)
        }

        val iconDescription = badestedInView.getIconDescription()

        if (iconDescription != null) {
            val weatherDescription = resources.getString(iconDescription)
            TextView_badested_weather_desc.text = weatherDescription

            val symbolDescription = "Symbol: " + resources.getString(iconDescription)
            ImageView_badested_symbol.contentDescription =
                symbolDescription
        }

        /*
         * Setting actions for buttons
         */
        Button_badested_show_on_map.setOnClickListener {
            val gmmIntentUri: Uri = if (badestedInView.name == "Solvikbukta") {
                // Må korrigere søkeordet for at Google Maps skal finne stedet
                Uri.parse("geo:${badestedInView.badested.lat},${badestedInView.badested.lon}?q=Solviks venner")
            } else {
                Uri.parse("geo:${badestedInView.badested.lat},${badestedInView.badested.lon}?q=${badestedInView.badested.name}")
            }
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        // Shows facilities and hides button
        Button_badested_facilities.setOnClickListener {
            container_facilities.visibility = View.VISIBLE
            Button_badested_facilities.visibility = View.INVISIBLE
        }
    }
}