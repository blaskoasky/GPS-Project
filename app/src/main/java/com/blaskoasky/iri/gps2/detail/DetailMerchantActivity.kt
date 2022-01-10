package com.blaskoasky.iri.gps2.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.blaskoasky.iri.gps2.databinding.ActivityDetailMerchantBinding
import com.blaskoasky.iri.gps2.dto.MerchantEntity

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

        if (selectedMerchant != null) {
            detailMerchantBinding.textView.text = selectedMerchant.merchantName
            supportActionBar?.title = selectedMerchant.merchantName
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}