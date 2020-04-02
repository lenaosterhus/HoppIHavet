package com.example.badeapp.UI


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.badeapp.MainViewModel
import com.example.badeapp.R
import com.example.badeapp.repository.Badested
import kotlinx.android.synthetic.main.activity_main.*

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

        // For debugging only
        val triggerSize = viewModel.badesteder.value?.size?.times(2)
        Log.d(TAG, "Trigger: $triggerSize")

        viewModel.countUpdated.observe(this, Observer { count ->
            Log.d(TAG, "Change observed to COUNT_UPDATED: $count")
            if (count == triggerSize) {
                Log.d(TAG, "Ready to update recycler view")
                updateRecyclerAdapter()
            }
        })
    }

    private fun updateRecyclerAdapter() {
        viewModel.badesteder.value?.let {
            recyclerAdapter.submitList(it)
        }
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
