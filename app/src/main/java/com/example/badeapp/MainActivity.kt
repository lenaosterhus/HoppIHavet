package com.example.badeapp


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.badeapp.repository.Badested
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "DEBUG - MainActivity"

    lateinit var viewModel: MainViewModel
    private lateinit var recyclerAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Business-logic =
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.init(this)
        initRecyclerView()

        // Observerer endringer til listen over badesteder
        viewModel.badesteder.observe(this, Observer { badesteder ->
            badesteder?.forEach {
                subscribeObservers(it)
            }
        })
    }

    /**
     * Observerer endringer på badestedenes varsler
     */
    private fun subscribeObservers(badested: Badested) {
        badested.locationForecastInfo.observe(this, Observer {
            updateRecyclerAdapter()
        })
        badested.oceanForecastInfo.observe(this, Observer {
            updateRecyclerAdapter()
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
            recyclerAdapter = RecyclerAdapter(this@MainActivity)
            adapter = recyclerAdapter
        }
    }
}
