package com.example.badeapp.UI

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.badeapp.R
import com.example.badeapp.repository.Badested
import kotlinx.android.synthetic.main.rv_element.view.*
import java.util.*

private const val TAG = "DEBUG - Adapter"

class RecyclerAdapter(
    private val badesteder: List<Badested>,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    internal var filteredBadestedList = badesteder



    /**
     * This value maps a badested to the ViewHolder that it is present in.
     * If the badested is not present in the map, then it is not visible
     * on screen.
     */
    private val visible = mutableMapOf<Badested, ElementView>()

    // .................................
    // Recycler adapter methods
    //---------------------------------
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
                visible[filteredBadestedList[position]] = holder
                holder.bind(filteredBadestedList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredBadestedList.size
    }


    // ........................................
    // Methods for keeping shown data updated
    //-----------------------------------------
    fun notifyChangeFor(badested: Badested) {
        visible[badested]?.drawData()
    }

    fun updateRecyclerAdapter() {
        visible.entries.forEach {
            it.value.drawData()
        }
    }


    // ..........................................
    // The RV element containing the information
    //-------------------------------------------
    class ElementView(
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
            findImage()
        }

        fun drawData() {
            //@TODO flytte strengene inn i resources.
            itemView.TextView_badested_name.text = badested?.name ?: ""
            itemView.TextView_badested_water_temp.text =
                badested?.forecast?.value?.waterTempC?.toString() + "°"
            itemView.TextView_badested_air_temp.text =
                badested?.forecast?.value?.airTempC?.toString() + "°"

            val symbol = badested?.forecast?.value?.symbol.let {
                if (it == null) {
                    itemView.symbol_water.setImageDrawable(null)
                } else {
                    itemView.symbol_weather.setImageResource(it)
                }
            }

            itemView.ImageView_badested_image.clipToOutline = true
        }

        private fun findImage() {
            val images = arrayOf(
                R.drawable.badebilder_hovedoya,
                R.drawable.badebilder_sorenga,
                R.drawable.badebilder_tjuvholmen,
                R.drawable.badebilder_paradisbukta,
                R.drawable.badebilder_huk,
                R.drawable.badebilder_bekkelagsbadet,
                R.drawable.badebilder_bekkensten,
                R.drawable.badebilder_bestemorstranda,
                R.drawable.badebilder_bygdoy,
                R.drawable.badebilder_fiskevollbukta,
                R.drawable.badebilder_gressholmen,
                R.drawable.badebilder_rambergoya,
                R.drawable.badebilder_hvervenbukta,
                R.drawable.badebilder_haaoybukta,
                R.drawable.badebilder_taajebukta,
                R.drawable.badebilder_ingierstrand,
                R.drawable.badebilder_katten,
                R.drawable.badebilder_langoeyene,
                R.drawable.badebilder_nordstrandbad,
                R.drawable.badebilder_sollerudstranda,
                R.drawable.badebilder_solvikbukta,
                R.drawable.badebilder_ulvoya
            )

            itemView.ImageView_badested_image.setImageResource(images[badested?.imgPos!!])
            itemView.ImageView_badested_image.clipToOutline = true
        }
    }


    interface Interaction {
        fun onItemSelected(position: Int, item: Badested)
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
                Log.d(TAG, "performFiltering: $constraint")
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    // No filter implemented we return the whole list
                    Log.d(TAG, "performFiltering: input is empty")
                    filteredBadestedList = badesteder
                }
                else {
                    val resultList = badesteder.filter {
                        val name: CharSequence = it.name.toUpperCase(Locale.ROOT)
                        name.contains(charSearch.toUpperCase(Locale.ROOT))
                    }
                    filteredBadestedList = resultList
                }
                val results = FilterResults()
                results.values = filteredBadestedList
                results.count = filteredBadestedList.size
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredBadestedList = results?.values as List<Badested>
                notifyDataSetChanged()
            }
        }
    }
}