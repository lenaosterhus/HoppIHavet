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
 * live data.
 */

class MainActivity : AppCompatActivity(),
    RecyclerAdapter.Interaction {

    private val TAG = "DEBUG - MainActivity"

    lateinit var viewModel: MainViewModel
    private lateinit var recyclerAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Business-logic =
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.init()

        initRecyclerView()

        //If one of symbol, aitTempC or waterTempC changes, then update the RC adapter.
        viewModel.badesteder.value?.forEach { badested ->
            badested.symbol.observe(this, Observer {
                Log.d(TAG, "symbol is set for $badested")
                Log.d(TAG, "Updating badesteder RC view for $badested symbol")
                updateRecyclerAdapter(badested)
            })
            badested.waterTempC.observe(this, Observer {
                Log.d(TAG, "water temp is set for $badested")
                Log.d(TAG, "Updating badesteder RC view for $badested water temp")
                updateRecyclerAdapter(badested)
            })
            badested.airTempC.observe(this, Observer {
                Log.d(TAG, "air temp is set for $badested")
                Log.d(TAG, "Updating badesteder RC view for $badested air temp")
                updateRecyclerAdapter(badested)
            })
        }
        Log.d(TAG, "Updating badesteder RC view for everyone")
        updateRecyclerAdapter()
    }

    private fun updateRecyclerAdapter(badested: Badested) {
        recyclerAdapter.notifyChangeFor(badested)
    }

    private fun updateRecyclerAdapter() {
        recyclerAdapter.updateRecyclerAdapter()
    }


    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity) // Vertikal layout
            recyclerAdapter =
                RecyclerAdapter(this@MainActivity)
            adapter = recyclerAdapter
        }
    }

    override fun onItemSelected(position: Int, item: Badested) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
