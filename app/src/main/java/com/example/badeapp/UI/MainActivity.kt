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

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity) // Vertikal layout
            recyclerAdapter =
                RecyclerAdapter(viewModel.badesteder.value!!)
            adapter = recyclerAdapter
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel()
        initRecyclerView()

        //If one of symbol, aitTempC or waterTempC changes, then update the RC adapter.
        viewModel.badesteder.value?.forEach { badested ->
            badested.symbol.observe(this, Observer {
                Log.d(TAG, "symbol is set for $badested")
                Log.d(TAG, "Updating badesteder RC view for $badested symbol")
                recyclerAdapter.notifyChangeFor(badested)
            })
            badested.waterTempC.observe(this, Observer {
                Log.d(TAG, "water temp is set for $badested")
                Log.d(TAG, "Updating badesteder RC view for $badested water temp")
                recyclerAdapter.notifyChangeFor(badested)
            })
            badested.airTempC.observe(this, Observer {
                Log.d(TAG, "air temp is set for $badested")
                Log.d(TAG, "Updating badesteder RC view for $badested air temp")
                recyclerAdapter.notifyChangeFor(badested)
            })
        }

        Log.d(TAG, "Updating badesteder RC view for everyone")
        //Update all the values at least once
        recyclerAdapter.updateRecyclerAdapter()
    }


    override fun onItemSelected(position: Int, item: Badested) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
