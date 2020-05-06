package com.example.badeapp.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.badeapp.R
import com.example.badeapp.models.BadestedSummary
import kotlinx.android.synthetic.main.activity_badested.*

private const val TAG = "DEBUG -BadestedActivity"

class BadestedActivity : BaseActivity() {

    private lateinit var badestedInView: BadestedSummary

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_badested)

        if (intent.hasExtra("badested")) {
            val badestedSummary = intent.getParcelableExtra<BadestedSummary>("badested")
            Log.d(TAG, "onCreate: $badestedSummary")
            badestedSummary?.let {
                badestedInView = badestedSummary
                setView()
            }
        }
    }

    private fun setView() {
        // TODO sett bilde av badestedet

        TextView_badested_name.text = badestedInView.badested.name

        TextView_badested_air_temp.text = badestedInView.airTempC.toString() + "°"
        TextView_badested_water_temp.text = badestedInView.waterTempC.toString() + "°"

        val icon = badestedInView.getIcon()

        if (icon != null) {
            ImageView_badested_symbol.setImageResource(icon)
        } else {
            ImageView_badested_symbol.setImageDrawable(null)
        }

        TextView_badested_description.text = badestedInView.badested.info
        TextView_badested_description.movementMethod = ScrollingMovementMethod()

    }
}