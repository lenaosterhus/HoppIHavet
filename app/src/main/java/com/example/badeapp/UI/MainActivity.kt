package com.example.badeapp.UI


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.badeapp.MainViewModel
import com.example.badeapp.R
import com.example.badeapp.repository.Badested
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel()
        initRecyclerView()

        // If symbol, airTempC or waterTempC changes, then update the RC adapter.
        viewModel.badesteder.forEach { badested ->
            Log.d(TAG, "onCreate: setting observers for $badested")
            badested.forecast.observe(this, Observer {
                Log.d(TAG, "onCreate: new value is set for $badested: $it")
                Log.d(TAG, "onCreate: Updating badesteder RC view for $badested")
                recyclerAdapter.notifyChangeFor(badested)
            })
        }


        //Update all the values at least once, so that the values that finished
        //their requests before the livedata was observer get initialised
        //recyclerAdapter.updateRecyclerAdapter()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.init()
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerAdapter =
                RecyclerAdapter(viewModel.badesteder)
            adapter = recyclerAdapter
        }
    }

    override fun onItemSelected(position: Int, item: Badested) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
