package com.blaskoasky.iri.gps2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blaskoasky.iri.gps2.databinding.ItemReviewBinding
import com.blaskoasky.iri.gps2.model.entity.MerchantEntity
import java.util.*

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private var reviews = ArrayList<MerchantEntity>()

    inner class ReviewViewHolder(binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = 15
}