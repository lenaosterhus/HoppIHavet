package com.example.badeapp.UI

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.badeapp.R
import com.example.badeapp.repository.Badested
import kotlinx.android.synthetic.main.rv_element.view.*

private val TAG = "DEBUG - Adapter"

class RecyclerAdapter(
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Badested>() {

        // Called to check whether two objects represent the same item.
        // Compare something unique!
        override fun areItemsTheSame(oldItem: Badested, newItem: Badested): Boolean {
            return (oldItem.lat == newItem.lat && oldItem.lon == newItem.lon)
        }

        // Called to check whether two items have the same data.
        // Used to make the update to the list item
        override fun areContentsTheSame(oldItem: Badested, newItem: Badested): Boolean {
            return oldItem.oceanForecastInfo.value == newItem.oceanForecastInfo.value &&
                    oldItem.locationForecastInfo.value == newItem.locationForecastInfo.value
        }
    }

    // AsyncListDiffer is an improvement on DiffUtil. Instead of calculating the amount of
    // update operations on the main thread, it calculates them on a background thread.
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
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    /** If a List is already present, a diff will be computed asynchronously on a background thread.
    * When the diff is computed, it will be applied,
    * and the new List will be swapped in.
     */
    fun submitList(list: List<Badested>) {
        differ.submitList(list)
    }

    class ElementView
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(badested: Badested) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, badested)
            }

            itemView.TextView_badested_name.text = badested.name
            itemView.TextView_badested_water_temp.text =
                badested.oceanForecastInfo.value?.vannTempC.toString()
            itemView.TextView_badested_air_temp.text =
                badested.locationForecastInfo.value?.getCurrentAirTempC().toString()
            itemView.ImageView_badested_image.clipToOutline = true
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Badested)
    }
}