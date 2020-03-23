package com.example.badeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.badeapp.repository.Badested


class BadestedRecycleViewAdapter(val context: Context) :
    RecyclerView.Adapter<BadestedRecycleViewAdapter.ElementView>() {

    class ElementView(val inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.rv_element, parent, false)) {
        lateinit var sted: Badested
        private lateinit var airTemp: TextView
        private lateinit var waterTemp: TextView

        init {
            airTemp = itemView.findViewById<TextView>(R.id.airTempC)
            waterTemp = itemView.findViewById<TextView>(R.id.airTempC)
        }

        fun bind(badested: Badested) {
            if (::sted.isInitialized) {
                sted.locationForecastInfo.removeObserver
            }
            sted.locationForecastInfo.
        }

    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BadestedRecycleViewAdapter.ElementView {
        val inflater = LayoutInflater.from(parent.context)
        return ElementView(inflater)
    }

    override fun getItemCount(): Int {
        return Badested::class.nestedClasses.size
    }

    override fun onBindViewHolder(holder: ElementView, position: Int) {
        val badested = Badested::class.nestedClasses.map { it.objectInstance as Badested }[position]
        holder.bind(badested)
    }


}