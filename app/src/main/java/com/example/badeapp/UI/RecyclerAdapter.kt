package com.example.badeapp.UI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.badeapp.R
import com.example.badeapp.repository.Badested
import kotlinx.android.synthetic.main.rv_element.view.*

private val TAG = "DEBUG - Adapter"

class RecyclerAdapter(
    val badesteder: MutableList<Badested>,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    //maps badested -> view holder that contain them
    private val visible = mutableMapOf<Badested, RecyclerView.ViewHolder>()

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

                holder.badested?.let {
                    visible -= it
                }
                visible[badesteder[position]] = holder
                holder.bind(badesteder[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return badesteder.size
    }

    fun notifyChangeFor(badested: Badested) {
        if (badested in visible) {
            when (val holder = visible[badested]) {
                is ElementView -> {
                    holder.drawData()
                }
            }
        }
    }

    fun updateRecyclerAdapter() {
        visible.entries.forEach {
            when (val holder = it.value) {
                is ElementView -> {
                    holder.drawData()
                }
            }
        }
    }

    class ElementView
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        var badested: Badested? = null

        fun bind(sted: Badested) = with(itemView) {
            badested = sted
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, sted)
            }
            drawData()
        }

        fun drawData() {
            itemView.TextView_badested_name.text = badested?.name ?: ""
            itemView.TextView_badested_water_temp.text =
                badested?.waterTempC?.value?.toString() ?: ""
            itemView.TextView_badested_air_temp.text =
                badested?.airTempC?.value?.toString() ?: ""
            itemView.ImageView_badested_image.clipToOutline = true
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Badested)
    }
}