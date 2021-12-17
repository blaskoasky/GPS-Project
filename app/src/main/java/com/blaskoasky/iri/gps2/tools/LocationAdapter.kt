package com.blaskoasky.iri.gps2.tools

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.blaskoasky.iri.gps2.databinding.ItemRowBinding
import com.blaskoasky.iri.gps2.dto.MerchantLocation

class LocationAdapter(private val mContext: Context) : RecyclerView.Adapter<LocationAdapter.ListViewHolder>() {

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
        holder.bind(location, mContext)
    }

    override fun getItemCount(): Int = listLocation.size

    inner class ListViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(location: MerchantLocation, context: Context) {
            with(binding) {
                tvMerchant.text = location.merchantName
                tvLatitude2.text = location.latitude
                tvLongitude2.text = location.longitude
                tvAddress2.text = location.address
                tvDistance.text = String.format("%.2f km", location.distance)

                itemContainer.setOnClickListener {
                    Toast.makeText(context, "item ${location.merchantName} clicked", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}