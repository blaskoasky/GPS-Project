package com.blaskoasky.iri.gps2.tools

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.blaskoasky.iri.gps2.R
import com.blaskoasky.iri.gps2.databinding.ItemRowBinding
import com.blaskoasky.iri.gps2.dto.MerchantLocation
import com.bumptech.glide.Glide

class LocationAdapter(private val mContext: Context) :
    RecyclerView.Adapter<LocationAdapter.ListViewHolder>() {

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
                tvMerchantName.text = location.merchantName
                tvMerchantAddress.text = location.address
                tvDistance.text = String.format("%.2f km", location.distance)
                Glide.with(context)
                    .load(location.imgMerchant)
                    .error(R.drawable.placeplaceholder)
                    .into(imgMerchant)

                itemContainer.setOnClickListener {
                    Toast.makeText(
                        context,
                        "item ${location.merchantName} clicked",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}