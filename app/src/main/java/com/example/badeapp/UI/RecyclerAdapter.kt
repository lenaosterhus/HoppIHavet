package com.example.badeapp.UI

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.badeapp.R
import com.example.badeapp.models.BadestedSummary
import kotlinx.android.synthetic.main.rv_element.view.*

private const val TAG = "ReyclerAdapter - DEBUG"

class RecyclerAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        return differ.currentList.size
    }

    fun submitList(list: List<BadestedSummary>) {
        Log.d(TAG, "Submitting list new list")
        differ.submitList(list)
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

                TextView_badested_name.text = summary?.badested?.name

                if (summary?.airTempC != null) {
                    TextView_badested_air_temp.text = summary!!.airTempC.toString() + "°"
                } else {
                    TextView_badested_air_temp.text = ""
                }

                if (summary?.waterTempC != null) {
                    TextView_badested_water_temp.text = summary!!.waterTempC.toString() + "°"
                } else {
                    TextView_badested_water_temp.text = ""
                }

                val icon = summary?.getIcon()

                if (icon != null) {
                    itemView.symbol_weather.setImageResource(icon)
                } else {
                    itemView.symbol_weather.setImageDrawable(null)
                }


            }

        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: BadestedSummary)
    }
}


