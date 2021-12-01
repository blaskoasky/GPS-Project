package com.blaskoasky.iri.gps2.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blaskoasky.iri.gps2.databinding.ItemRowBinding
import com.blaskoasky.iri.gps2.dto.LocationDetails

class LocationAdapter : RecyclerView.Adapter<LocationAdapter.ListViewHolder>() {

    private var listLocation = ArrayList<LocationDetails>()

    fun setLatitudeLongitude(location: List<LocationDetails>?) {
        if (location == null) return
        this.listLocation.clear()
        this.listLocation.addAll(location)
    }


    class ListViewHolder(private val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(location: LocationDetails) {
            with(binding) {
                // tvMerchant.text = location.merchant
                tvLatitude2.text = location.latitude
                tvLongitude2.text = location.longitude
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val location = listLocation[position]
        holder.bind(location)
    }

    override fun getItemCount(): Int = listLocation.size
}