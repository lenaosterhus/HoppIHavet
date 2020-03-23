package com.example.badeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.badeapp.repository.Badested
import kotlinx.android.synthetic.main.rv_element.view.*

class RecyclerAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Badested>() {

        override fun areItemsTheSame(oldItem: Badested, newItem: Badested): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Badested, newItem: Badested): Boolean {
            // @TODO("Må kansje sjekke at kun verdiene vi er interessert i har endret seg")
            return oldItem.locationForecastInfo.value == newItem.locationForecastInfo.value
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ElementView(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rv_element,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ElementView -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Badested>) {
        differ.submitList(list)
    }

    class ElementView
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        //lateinit var locationInfoObserver: Observer<LocationForecastInfo>


        fun bind(item: Badested) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.TextView_location_name.text = item.name
            itemView.TextView_water_temp.text = "Missing" //@TODO mye som må orndes
            itemView.TextView_air_temp.text = item.locationForecastInfo.value?.luftTempC.toString()

        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Badested)
    }
}