package com.example.badeapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import com.example.badeapp.R
import com.example.badeapp.models.Badested
import com.example.badeapp.models.BadestedForecast
import com.example.badeapp.models.DisplayedBadested
import kotlinx.android.synthetic.main.activity_badested.*


private const val TAG = "DEBUG -BadestedActivity"

class BadestedActivity : BaseActivity() {

    private lateinit var badestedInView: DisplayedBadested

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_badested)

        if (intent.hasExtra("badested")) {
            val badestedForecast = intent.getParcelableExtra<BadestedForecast>("badested")
            Log.d(TAG, "onCreate: $badestedForecast")
            badestedForecast?.let {
                badestedInView = badestedForecast.getDisplayedBadested()
                setView(badestedForecast)
            }
        }
    }

    private fun setView(badestedForecast: BadestedForecast) {
        ImageView_badested_image.setImageResource(badestedInView.image)

        TextView_badested_name.text = badestedInView.name

        TextView_badested_air_temp.text = badestedInView.airTempC
        TextView_badested_water_temp.text = badestedInView.waterTempC
        TextView_badested_precipitation.text = badestedInView.precipitation
        TextView_badested_wind.text = badestedInView.wind
        TextView_badested_valid_to.text = badestedInView.to

        val icon = badestedInView.icon

        if (icon != null) {
            ImageView_badested_symbol.setImageResource(icon)
        } else {
            ImageView_badested_symbol.setImageDrawable(null)
        }

        TextView_badested_description.text = badestedInView.info
        TextView_badested_description.movementMethod = ScrollingMovementMethod()



        // Foreløpig løsning: Søker etter navnet i Google Maps, med lat og lon som utgangspunkt
        // Ikke optimalt for Solvikbukta
        Button_badested_show_on_map.setOnClickListener {
            val gmmIntentUri: Uri = if (badestedForecast.badested.name == "Solvikbukta") {
                // Må korrigere søkeordet for at Google Maps skal finne stedet
                Uri.parse("geo:${badestedForecast.badested.lat},${badestedForecast.badested.lon}?q=Solviks venner")
            } else {
                Uri.parse("geo:${badestedForecast.badested.lat},${badestedForecast.badested.lon}?q=${badestedForecast.badested.name}")
            }
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }


        // Alternativ: Søke på lat + lon
        // Burde legge til egne lat + lon for maps-visning, nå viser noen plassering langt uti sjøen
//        Button_badested_show_on_map.setOnClickListener {
//            val gmmIntentUri: Uri = Uri.parse("geo:${badestedForecast.badested.lat},${badestedForecast.badested.lon}?q=${badestedForecast.badested.lat},${badestedForecast.badested.lon}")
//            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//            mapIntent.setPackage("com.google.android.apps.maps")
//            startActivity(mapIntent)
//        }
    }
}