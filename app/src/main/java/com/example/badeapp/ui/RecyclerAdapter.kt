package com.example.badeapp.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.badeapp.R
import com.example.badeapp.models.BadestedForecast
import kotlinx.android.synthetic.main.rv_element.view.*
import java.util.*


class RecyclerAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var unFilteredList: List<BadestedForecast>? = null

    val noSearchResult = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BadestedForecast>() {
        override fun areItemsTheSame(oldItem: BadestedForecast, newItem: BadestedForecast): Boolean {
            return oldItem.badested == newItem.badested
        }
        override fun areContentsTheSame(
            oldItem: BadestedForecast,
            newItem: BadestedForecast
        ): Boolean {
            return oldItem.sameContentAs(newItem)
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return RVElement(
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
            is RVElement -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<BadestedForecast>) {
        differ.submitList(list)
        unFilteredList = list
    }

    class RVElement
    constructor (itemView: View, private val interaction: Interaction?) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(forecast: BadestedForecast) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, forecast)
            }
            with(itemView) {

                TextView_element_name.text = forecast.badested.name
                TextView_element_place.text = forecast.badested.place

                if (forecast.forecast?.airTempC != null) {
                    TextView_element_air_temp.text = forecast.getAirTempCDescription()
                } else {
                    TextView_element_air_temp.text = ""
                }

                if (forecast.forecast?.waterTempC != null) {
                    TextView_element_water_temp.text =
                        forecast.getWaterTempCDescription()
                } else {
                    TextView_element_water_temp.text = ""
                }

                val icon = forecast.getIcon()

                if (icon != null) {
                    itemView.ImageView_element_symbol.setImageResource(icon)
                } else {
                    itemView.ImageView_element_symbol.setImageDrawable(null)
                }

                val iconDescription = forecast.getIconDescription()
                if (iconDescription != null) {
                    itemView.ImageView_element_symbol.contentDescription =
                        resources.getString(iconDescription)
                }

                ImageView_element_image.setImageResource(forecast.badested.image)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: BadestedForecast)
    }

    /**
     * Returns a filter that can be used to constrain data with a filtering
     * pattern.
     *
     * @return a filter used to constrain data
     */
    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val filteredBadestedList: List<BadestedForecast>?

                val charSearch = constraint.toString()
                filteredBadestedList = if (charSearch.isEmpty()) {
                    // No filter implemented we return the whole list
                    unFilteredList
                } else {
                    unFilteredList?.filter {

                        val name: CharSequence = it.badested.name.toUpperCase(Locale.ROOT)
                        val place: CharSequence = it.badested.place.toUpperCase(Locale.ROOT)
                        name.contains(charSearch.toUpperCase(Locale.ROOT)) || place.contains(charSearch.toUpperCase(Locale.ROOT))
                    }
                }

                noSearchResult.postValue(filteredBadestedList.isNullOrEmpty())

                val results = FilterResults()
                results.values = filteredBadestedList
                results.count = filteredBadestedList?.size ?: 0
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                differ.submitList(results?.values as List<BadestedForecast>?)
            }
        }
    }
}

