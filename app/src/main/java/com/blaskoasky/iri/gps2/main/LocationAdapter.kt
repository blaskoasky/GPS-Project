package com.blaskoasky.iri.gps2.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blaskoasky.iri.gps2.databinding.ItemRowBinding
import com.blaskoasky.iri.gps2.dto.MerchantLocation

class LocationAdapter : RecyclerView.Adapter<LocationAdapter.ListViewHolder>() {

    private var listLocation = ArrayList<MerchantLocation>()

    fun setLatitudeLongitude(location: List<MerchantLocation>?) {
        if (location == null) return
        this.listLocation.clear()
        this.listLocation.addAll(location)
        this.listLocation.sortBy {
            it.distance
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

    inner class ListViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(location: MerchantLocation) {
            with(binding) {
                tvMerchant.text = location.merchantName
                tvLatitude2.text = location.latitude
                tvLongitude2.text = location.longitude
                tvAddress2.text = location.address
                tvDistance.text = String.format("%.2f km", location.distance)
            }
        }
    }
}