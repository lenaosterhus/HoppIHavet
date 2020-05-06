package com.example.badeapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.badeapp.BadestedListViewModel
import com.example.badeapp.R
import com.example.badeapp.models.BadestedSummary
import kotlinx.android.synthetic.main.activity_badested_list.*

/**
 * Denne filen skal håndtere eventer, altså er det her vi obsercerer badestedenes
 * live data, og endrer på data etter behov.
 */

private const val TAG = "DEBUG - MainActivity"

class BadestedListActivity : BaseActivity(),
    RecyclerAdapter.Interaction {

    private lateinit var viewModel: BadestedListViewModel
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_badested_list)

        //Init view model
        viewModel = ViewModelProvider(this).get(BadestedListViewModel::class.java)
        viewModel.init()

        //Init recycler view
        recycler_view.apply {
            Log.d(TAG, "Inflate RecyclerView")
            layoutManager = LinearLayoutManager(this@BadestedListActivity)
            recyclerAdapter = RecyclerAdapter(this@BadestedListActivity)
            adapter = recyclerAdapter
        }

        // Subscribe observers
        viewModel.summaries.observe(this, Observer {
            Log.d(TAG, "Submitting list $it")
            viewModel.printRawDBQuerry() //@TODO remove
            recyclerAdapter.submitList(it)
        })


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

    override fun onResume() {
        super.onResume()
        viewModel.updateData()
    }


    override fun onItemSelected(position: Int, item: BadestedSummary) {
        Log.d(TAG, "onItemSelected: CLICKED: $position")
        Log.d(TAG, "onItemSelected: CLICKED: $item")

        val intent = Intent(this, BadestedActivity::class.java)
        intent.putExtra("badested", item)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelRequests()
    }
}
