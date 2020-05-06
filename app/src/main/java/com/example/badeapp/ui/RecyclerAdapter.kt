package com.example.badeapp.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.badeapp.R
import com.example.badeapp.models.BadestedSummary
import kotlinx.android.synthetic.main.activity_badested.view.*
import kotlinx.android.synthetic.main.rv_element.view.*
import java.util.*

private const val TAG = "ReyclerAdapter - DEBUG"



class RecyclerAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var unFilterdList: List<BadestedSummary>? = null

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BadestedSummary>() {

        override fun areItemsTheSame(oldItem: BadestedSummary, newItem: BadestedSummary): Boolean {
            return oldItem.badested == newItem.badested
        }

        override fun areContentsTheSame(
            oldItem: BadestedSummary,
            newItem: BadestedSummary
        ): Boolean {
            return oldItem.waterTempC == newItem.waterTempC && oldItem.symbol == newItem.symbol &&
                    oldItem.airTempC == newItem.airTempC
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
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        //return filteredBadestedList.size @TODO figure out
        return differ.currentList.size
    }

    fun submitList(list: List<BadestedSummary>) {
        Log.d(TAG, "Submitting list new list")
        differ.submitList(list)
        unFilterdList = list
    }

    class RVElement
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        var summary: BadestedSummary? = null

        fun bind(item: BadestedSummary) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            summary = item
            draw()
        }

        fun draw() {
            with(itemView) {

                TextView_element_name.text = summary?.badested?.name

                if (summary?.airTempC != null) {
                    TextView_element_air_temp.text = summary!!.airTempC.toString() + "°"
                } else {
                    TextView_element_air_temp.text = ""
                }

                if (summary?.waterTempC != null) {
                    TextView_element_water_temp.text = summary!!.waterTempC.toString() + "°"
                } else {
                    TextView_element_water_temp.text = ""
                }

                val icon = summary?.getIcon()

                if (icon != null) {
                    itemView.ImageView_element_symbol.setImageResource(icon)
                } else {
                    itemView.ImageView_element_symbol.setImageDrawable(null)
                }
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: BadestedSummary)
    }


    /**
     * Returns a filter that can be used to constrain data with a filtering
     * pattern.
     *
     * @return a filter used to constrain data
     */
    override fun getFilter(): Filter { //TODO figure this out
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val filteredBadestedList: List<BadestedSummary>?

                Log.d(TAG, "performFiltering: $constraint")
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    // No filter implemented we return the whole list
                    Log.d(TAG, "performFiltering: input is empty")
                    filteredBadestedList = unFilterdList
                }
                else {
                    filteredBadestedList = unFilterdList?.filter {
                        val name: CharSequence = it.badested.name.toUpperCase(Locale.ROOT)
                        name.contains(charSearch.toUpperCase(Locale.ROOT))
                    }
                }
                val results = FilterResults()
                results.values = filteredBadestedList
                results.count = filteredBadestedList?.size ?: 0
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                differ.submitList(results?.values as List<BadestedSummary>?)
            }
        }
    }
}

