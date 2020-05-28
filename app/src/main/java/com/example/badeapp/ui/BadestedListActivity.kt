package com.example.badeapp.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.badeapp.R
import com.example.badeapp.models.BadestedForecast
import com.example.badeapp.util.inTheFutureFromNow
import com.example.badeapp.util.isInternetAvailable
import com.example.badeapp.util.parseAsGmtIsoDate
import com.example.badeapp.viewModels.BadestedListViewModel
import kotlinx.android.synthetic.main.activity_badested_list.*

/**
 * This class handles events: This is where we observe LiveData from the viewModel
 * and update the UI when needed.
 */

class BadestedListActivity : AppCompatActivity(),
    RecyclerAdapter.Interaction {

    private lateinit var viewModel: BadestedListViewModel
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_badested_list)

        progressBar = findViewById(R.id.progress_bar)

        // Init view model
        viewModel = ViewModelProvider(this).get(BadestedListViewModel::class.java)

        // Init recycler view
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@BadestedListActivity)
            recyclerAdapter = RecyclerAdapter(this@BadestedListActivity)
            adapter = recyclerAdapter
        }

        subscribeObservers()
    }

    private fun subscribeObservers() {

        // Observing forecasts
        viewModel.forecasts.observe(this, Observer {
            recyclerAdapter.submitList(it)
        })

        // Observing if we're halted by MI, and displaying toast
        viewModel.hasHalted.observe(this, Observer { hasHalted ->
            if (hasHalted) {
                Toast.makeText(
                    this@BadestedListActivity,
                    resources.getString(R.string.has_halted_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        // Observing if we are loading data from database or API
        viewModel.isLoading.observe(this, Observer {isLoading ->
            showProgressBar(isLoading)
            if (!isLoading) {
                // If we are not loading any data (from DB) and we don't have internet, then
                // we show a toast to the user.
                if (!isInternetAvailable(this))
                    networkConnectionToast()
            }
        })

        // Observing if we are getting results from the search or not
        recyclerAdapter.noSearchResult.observe(this, Observer { noResult ->
            showNoSearchResult(noResult)
        })
    }

    private fun showProgressBar(visibility: Boolean) {
        progressBar.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
    }

    private fun networkConnectionToast() {
        // If no data is present in DB, and we don't have any internet show a toast to the user.
        if (viewModel.forecasts.value.isNullOrEmpty()) {
            Toast.makeText(
                this,
                resources.getString(R.string.no_internet_toast),
                Toast.LENGTH_LONG
            ).show()
        }

        // If data is more then 3h outdated, and we don't have any internet show a toast to the user.
        if (viewModel.forecasts.value?.any {
                it.forecast?.createdLocation?.parseAsGmtIsoDate()!!
                    .before(inTheFutureFromNow(-60 * 3))
            } == true) {
            Toast.makeText(
                this,
                resources.getString(R.string.outdated_info_toast),
                Toast.LENGTH_LONG
            ).show()
        }

        // Now we add a observer to network change, that tries to update once a network
        // connection is active. We can only do this in later versions of android sdk.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            addNetworkObserverToUpdateOnAvailable()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun addNetworkObserverToUpdateOnAvailable() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // Take action when network connection is gained
                viewModel.updateData()
            }
        })
    }

    // Search bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryString: String): Boolean {
                recyclerAdapter.filter.filter(queryString)
                return false
            }

            override fun onQueryTextChange(queryString: String): Boolean {
                recyclerAdapter.filter.filter(queryString)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun showNoSearchResult(noResult: Boolean) {
        if (noResult) {
            findViewById<TextView>(R.id.TextView_badestedList_noResult).visibility = View.VISIBLE
        } else {
            findViewById<TextView>(R.id.TextView_badestedList_noResult).visibility = View.INVISIBLE
        }
    }

    override fun onItemSelected(position: Int, item: BadestedForecast) {
        val intent = Intent(this, BadestedActivity::class.java)
        intent.putExtra("badestedForecast", item)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateData()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelRequests()
    }
}
