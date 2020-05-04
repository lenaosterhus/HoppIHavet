package com.example.badeapp.UI

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.badeapp.MainViewModel
import com.example.badeapp.R
import com.example.badeapp.models.BadestedSummary
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Denne filen skal håndtere eventer, altså er det her vi obsercerer badestedenes
 * live data, og endrer på data etter behov.
 */

private const val TAG = "DEBUG - MainActivity"

class MainActivity : AppCompatActivity(),
    RecyclerAdapter.Interaction {

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Init view model
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.init()

        //Init recycler view
        recycler_view.apply {
            Log.d(TAG, "Inflate RecyclerView")
            layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerAdapter = RecyclerAdapter()
            adapter = recyclerAdapter
        }


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
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelRequests()
    }
}
