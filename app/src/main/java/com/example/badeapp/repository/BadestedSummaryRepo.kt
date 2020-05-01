package com.example.badeapp.repository

import android.app.Application
import android.util.Log
import com.example.badeapp.models.BadestedSummary
import com.example.badeapp.persistence.ForecastDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * This is the repository that handles the interactions to get the
 * summarised data of every badested. This would be the information
 * visible when first opening the app .
 *
 */

private const val TAG = "BadestedSummaryRepo"


class BadestedSummaryRepo(val application: Application) {

    private var lst: List<BadestedSummary>? = null

    private val DB = ForecastDB.getDatabase(application)

    val summaries = DB.badestedSummaryDao().getAllCurrent()


    fun printRawDBQuerry() {
        Log.d(TAG, "Starting raw querry print")
        CoroutineScope(Dispatchers.IO).launch {

            Log.d(TAG, "Raw querry Inside IO thread")
            val res = DB.badestedSummaryDao().getAllCurrentRaw()
            if (res.isEmpty()) {
                Log.d(TAG, "WTF  its empty!")
            } else {
                Log.d(TAG, "Halelulja")
            }
            Log.d(TAG, res.toString())
        }
        Log.d(TAG, "Stopping raw querry")
    }

}