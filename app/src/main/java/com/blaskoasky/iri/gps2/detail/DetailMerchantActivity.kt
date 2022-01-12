package com.blaskoasky.iri.gps2.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.blaskoasky.iri.gps2.R
import com.blaskoasky.iri.gps2.databinding.ActivityDetailMerchantBinding
import com.blaskoasky.iri.gps2.dto.MerchantEntity
import com.bumptech.glide.Glide

class DetailMerchantActivity : AppCompatActivity() {

    private lateinit var detailMerchantBinding: ActivityDetailMerchantBinding

    companion object {
        const val EXTRA_MERCHANT_DETAIL = "extra_merchant_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailMerchantBinding = ActivityDetailMerchantBinding.inflate(layoutInflater)
        setContentView(detailMerchantBinding.root)

        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Kambing"

        val selectedMerchant = intent.getParcelableExtra<MerchantEntity>(EXTRA_MERCHANT_DETAIL)

        if (selectedMerchant != null) {
            Glide.with(this)
                .load(selectedMerchant.imgMerchant)
                .error(R.drawable.placeplaceholder)
                .into(detailMerchantBinding.imgMainDtl)
            with(detailMerchantBinding) {
                tvMerchantNameDtl.text = selectedMerchant.merchantName
                tvClockDtl.text = selectedMerchant.openHours
                tvDistanceDtl.text = String.format("%.2f km", selectedMerchant.distance)
                tvAddressDtl.text = selectedMerchant.address
            }
        }
    }
}