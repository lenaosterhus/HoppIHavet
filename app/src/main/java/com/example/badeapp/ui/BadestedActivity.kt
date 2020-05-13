package com.example.badeapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import com.example.badeapp.R
import com.example.badeapp.models.Badested
import com.example.badeapp.models.BadestedForecast
import kotlinx.android.synthetic.main.activity_badested.*


private const val TAG = "DEBUG -BadestedActivity"

class BadestedActivity : BaseActivity() {

    private lateinit var badestedInView: BadestedForecast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_badested)

        if (intent.hasExtra("badested")) {
            val badestedForecast = intent.getParcelableExtra<BadestedForecast>("badested")
            Log.d(TAG, "onCreate: $badestedForecast")
            badestedInView = badestedForecast!!
        }
        setView()
    }

    private fun setView() {

        ImageView_badested_image.setImageResource(badestedInView.image)
        Log.d(TAG,badestedInView.name)
        TextView_badested_name.text = badestedInView.name

        TextView_badested_air_temp.text = badestedInView.getAirTempCDescription()
        TextView_badested_water_temp.text = badestedInView.getWaterTempCDescription()
        TextView_badested_precipitation.text = badestedInView.getPrecipitationDescription()
        TextView_badested_wind.text = badestedInView.getWindDescription()
        TextView_badested_valid_to.text = badestedInView.getValidToDescription()

        val icon = badestedInView.getIcon()

        if (icon != null) {
            ImageView_badested_symbol.setImageResource(icon)
        } else {
            ImageView_badested_symbol.setImageDrawable(null)
        }

        TextView_badested_description.text = badestedInView.info
        TextView_badested_description.movementMethod = ScrollingMovementMethod()

        Log.d(TAG, "setView: setter fasiliteter: ${badestedInView.facilities}")
        TextView_badested_facilities.text = badestedInView.facilities



        // Foreløpig løsning: Søker etter navnet i Google Maps, med lat og lon som utgangspunkt
        // Ikke optimalt for Solvikbukta
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

        // Viser fasilitetene og skjuler knappen
        Button_badested_facilities.setOnClickListener {
            container_facilities.visibility = View.VISIBLE
            Button_badested_facilities.visibility = View.INVISIBLE
        }


        // Alternativ: Søke på lat + lon
        // Burde legge til egne lat + lon for maps-visning, nå viser noen plassering langt uti sjøen
//        Button_badested_show_on_map.setOnClickListener {
//            val gmmIntentUri: Uri = Uri.parse("geo:${badestedInView.badested.lat},${badestedInView.badested.lon}?q=${badestedInView.badested.lat},${badestedInView.badested.lon}")
//            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//            mapIntent.setPackage("com.google.android.apps.maps")
//            startActivity(mapIntent)
//        }
    }
}