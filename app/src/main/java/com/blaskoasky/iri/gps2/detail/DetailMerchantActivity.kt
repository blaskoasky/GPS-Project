package com.blaskoasky.iri.gps2.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.blaskoasky.iri.gps2.R
import com.blaskoasky.iri.gps2.databinding.ActivityDetailMerchantBinding
import com.blaskoasky.iri.gps2.dto.MerchantEntity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior

class DetailMerchantActivity : AppCompatActivity() {

    private lateinit var detailMerchantBinding: ActivityDetailMerchantBinding

    companion object {
        const val EXTRA_MERCHANT_DETAIL = "extra_merchant_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailMerchantBinding = ActivityDetailMerchantBinding.inflate(layoutInflater)
        setContentView(detailMerchantBinding.root)

        val selectedMerchant = intent.getParcelableExtra<MerchantEntity>(EXTRA_MERCHANT_DETAIL)

        BottomSheetBehavior.from(detailMerchantBinding.contentSheet).apply {
            peekHeight = 500
            this.state = BottomSheetBehavior.STATE_EXPANDED
        }

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

        supportActionBar?.title = "Detail Merchant"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}