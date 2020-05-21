package com.example.badeapp

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.badeapp.api.MIThrottler
import com.example.badeapp.models.BadestedForecast
import com.example.badeapp.persistence.ForecastDB
import com.example.badeapp.repository.BadestedForecastRepo
import com.example.badeapp.util.inTheFutureFromNow
import com.example.badeapp.util.isInternetAvailable
import com.example.badeapp.util.parseAsGmtIsoDate

private const val TAG = "BadestedListVM"

class BadestedListViewModel(application: Application) : AndroidViewModel(application) {

    private val badestedForecastRepo =
        BadestedForecastRepo(ForecastDB.getDatabase(application).forecastDao())

    val isLoading = MediatorLiveData<Boolean>().apply {
        this.addSource(badestedForecastRepo.isLoading) { bool ->
            this.value = bool
            if (bool == false) {
                //If we are not loading any data (from DB) and we don't have internet, then
                //We show a toast to the user.
                if (!isInternetAvailable(getApplication()))
                    networkConnectionToast()
            }
        }
    }



    val hasHalted = MediatorLiveData<Boolean>().apply {
        addSource(MIThrottler.hasHalted) { hasHalted ->
            if (hasHalted) {
                cancelRequests()
                this.value = true
            } else {
                this.value = false
            }
        }
    }

    val forecasts: LiveData<List<BadestedForecast>> = badestedForecastRepo.forecasts

    fun networkConnectionToast() {

        //If no data is present in DB, and we don't have any internet show a toast to the user.
        if (forecasts.value.isNullOrEmpty()) {
            Toast.makeText(
                getApplication(),
                getApplication<Application>().resources.getString(R.string.no_internet_toast),
                Toast.LENGTH_LONG
            ).show()
            //If no data is present in DB, and we don't have any internet show a toast to the user.
        }

        //If data is more then 3h outdated, and we don't have any internet show a toast to the user.
        if (forecasts.value?.any {
                it.forecast?.createdLocation?.parseAsGmtIsoDate()!!
                    .before(inTheFutureFromNow(-60 * 3))
            } == true) {
            Toast.makeText(
                getApplication(),
                getApplication<Application>().resources.getString(R.string.outdated_info_toast),
                Toast.LENGTH_LONG
            ).show()
        }

        //Now we add a observer to network change, that tries to update once a network
        // connection is active. We can only do this in later versions of android sdk.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            addNetworkObserverToUpdateOnAvailable()
        }
    }

    fun updateData() {
        Log.d(TAG, "updateData: ...")
        // Setter Location- og OceanForecast for alle badestedene som er utdatert
        badestedForecastRepo.updateForecasts()



        if(!isInternetAvailable(getApplication())) {

            //If no data is present in DB, and we don't have any internet show a toast to the user.
            if(forecasts.value.isNullOrEmpty()) {
                Toast.makeText(
                    getApplication(),
                    getApplication<Application>().resources.getString(R.string.no_internet_toast),
                    Toast.LENGTH_LONG
                ).show()
                //If no data is present in DB, and we don't have any internet show a toast to the user.
            }

            //If data is more then 3h outdated, and we don't have any internet show a toast to the user.
            if(forecasts.value?.any{
                    it.forecast?.createdLocation?.parseAsGmtIsoDate()!!.before(inTheFutureFromNow(-60*3))
                } == true) {
                Toast.makeText(
                    getApplication(),
                    getApplication<Application>().resources.getString(R.string.outdated_info_toast),
                    Toast.LENGTH_LONG
                ).show()
            }

            //Now we add a observer to network change, that tries to update once a network
            // connection is active. We can only do this in later versions of android sdk.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                addNetworkObserverToUpdateOnAvailable()
            }
        }



    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun addNetworkObserverToUpdateOnAvailable() {
        val connectivityManager = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                //take action when network connection is gained
                updateData()
            }
        })
    }

    fun cancelRequests() {
        badestedForecastRepo.cancelRequests()
    }
}