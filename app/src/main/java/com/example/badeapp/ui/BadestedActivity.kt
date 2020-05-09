package com.example.badeapp.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.badeapp.R
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
                setView()
            }
        }
    }

    private fun setView() {
        // TODO sett bilde av badestedet

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

    }
}