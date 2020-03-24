package com.example.badeapp


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
        initRecyclerView(lifecycleOwner = this)


        viewModel.setData()
        setDataToView()

    }

    private fun setDataToView() {
        viewModel.badesteder.value?.let {
            recyclerAdapter.submitList(it)
        }
    }

    private fun initRecyclerView(lifecycleOwner: LifecycleOwner) {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity) // Vertikal layout
            recyclerAdapter = RecyclerAdapter(lifecycleOwner)
            adapter = recyclerAdapter
        }
    }


}
