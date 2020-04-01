package com.example.badeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.badeapp.models.LocationForecastInfo
import com.example.badeapp.models.OceanForecastInfo
import com.example.badeapp.repository.Badested
import kotlinx.android.synthetic.main.rv_element.view.*


class RecyclerAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Badested>() {

        override fun areItemsTheSame(oldItem: Badested, newItem: Badested): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Badested, newItem: Badested): Boolean {
            // @TODO("Må kansje sjekke at kun verdiene vi er interessert i har endret seg")
            return oldItem.locationForecastInfo.value == newItem.locationForecastInfo.value &&
                    oldItem.oceanForecastInfo.value == newItem.oceanForecastInfo.value
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
            interaction,
            lifecycleOwner
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

    fun submitList(list: List<Badested>) {
        differ.submitList(list)
    }

    class ElementView
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val lifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(itemView) {

        private lateinit var locationInfoObserver: Observer<LocationForecastInfo?>
        private lateinit var oceanInfoObserver: Observer<OceanForecastInfo?>

        fun bind(badested: Badested) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, badested)
            }

            itemView.TextView_badested_name.text = badested.name
            itemView.TextView_badested_water_temp.text =
                badested.oceanForecastInfo.value?.vannTempC.toString()
            itemView.TextView_badested_air_temp.text =
                badested.locationForecastInfo.value?.luftTempC.toString()
            itemView.ImageView_badested_image.clipToOutline = true

            //Update/change observer for locationforecast
            if (::locationInfoObserver.isInitialized) {
                badested.locationForecastInfo.removeObserver(locationInfoObserver)
            }
            locationInfoObserver =
                Observer { t -> itemView.TextView_badested_air_temp.text = t?.luftTempC.toString() }
            badested.locationForecastInfo.observe(lifecycleOwner, locationInfoObserver)

            if (::oceanInfoObserver.isInitialized) {
                badested.oceanForecastInfo.removeObserver(oceanInfoObserver)
            }
            oceanInfoObserver =
                Observer { t -> itemView.TextView_badested_water_temp.text = t?.vannTempC.toString() }
            badested.oceanForecastInfo.observe(lifecycleOwner, oceanInfoObserver)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Badested)
    }
}