package com.example.badeapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.badeapp.BadestedListViewModel
import com.example.badeapp.R
import com.example.badeapp.models.BadestedForecast
import kotlinx.android.synthetic.main.activity_badested_list.*

/**
 * Denne filen skal håndtere eventer, altså er det her vi observerer badestedenes
 * live data, og endrer på data etter behov.
 */

private const val TAG = "BadestedListActivity"

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
            Log.d(TAG, "Inflate RecyclerView")
            layoutManager = LinearLayoutManager(this@BadestedListActivity)
            recyclerAdapter = RecyclerAdapter(this@BadestedListActivity)
            adapter = recyclerAdapter
        }

        subscribeObservers()
    }

    private fun subscribeObservers() {

        // Observing forecasts
        viewModel.forecasts.observe(this, Observer {
            Log.d(TAG, "Submitting list $it")
            recyclerAdapter.submitList(it)
        })

        // Observing if we're halted by MI, and displaying toast
        viewModel.hasHalted.observe(this, Observer { hasHalted ->
            Log.d(TAG, "subscribeObservers: hasHalted changed!")

            if (hasHalted) {
                Log.d(TAG, "subscribeObservers: HAS HALTED")
                Toast.makeText(
                    this@BadestedListActivity,
                    resources.getString(R.string.has_halted_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        // Observing if we are loading data from database or API
        viewModel.isLoading.observe(this, Observer {
            showProgressBar(it)
        })
    }

    private fun showProgressBar(visibility: Boolean) {
        Log.d(TAG, "showProgressBar: called with visibility: $visibility...")

        progressBar.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
    }

    // Search bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryString: String): Boolean {
                Log.d(TAG, "onQueryTextSubmit: $queryString")
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

    override fun onItemSelected(position: Int, item: BadestedForecast) {
        val intent = Intent(this, BadestedActivity::class.java)
        intent.putExtra("badestedForecast", item)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: called")
        viewModel.updateData()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelRequests()
    }
}
