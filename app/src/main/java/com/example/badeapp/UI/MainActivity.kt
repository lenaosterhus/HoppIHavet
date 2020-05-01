package com.example.badeapp.UI


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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

        viewModel.updateData()

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
