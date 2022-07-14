package com.blaskoasky.iri.gps2.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blaskoasky.iri.gps2.R
import com.blaskoasky.iri.gps2.databinding.ActivityDetailMerchantBinding
import com.blaskoasky.iri.gps2.entity.MerchantEntity
import com.bumptech.glide.Glide
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class DetailMerchantActivity : AppCompatActivity() {

    private lateinit var detailMerchantBinding: ActivityDetailMerchantBinding
    private lateinit var reviewAdapter: ReviewAdapter

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
                tvAddressDtl.text = selectedMerchant.address
                tvDistanceDtl.text = checkDistance(selectedMerchant.distance)
                if (checkOpenHours(selectedMerchant.openHours)) {
                    tvOpen.text = "Open"
                } else {
                    tvOpen.text = "Closed"
                }
            }
        }

        reviewAdapter = ReviewAdapter()
        reviewAdapter.notifyDataSetChanged()
        with(detailMerchantBinding.rvReviews) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = reviewAdapter
        }
    }

    private fun checkOpenHours(openHours: String): Boolean {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val zone = ZoneId.systemDefault()
            val today = LocalDate.now(zone)

            val splitTheThing = openHours.split(" - ")
            val open = splitTheThing[0]
            val close = splitTheThing[1]

            val merchantOpen = LocalTime.parse(open)
            val merchantClosed = LocalTime.parse(close)

            val merchantStart = today.atTime(merchantOpen).atZone(zone)
            val merchantEnd = today.atTime(merchantClosed).atZone(zone)

            // output of these if it single digit, zdt will eror, it needs double digit
            val hours = LocalDateTime.now().hour.toString()
            val minutes = LocalDateTime.now().minute.toString()

            // thus this code exists to make them double digits
            val hh: String = if (hours.length == 1) {
                "0$hours"
            } else {
                hours
            }

            val mm: String = if (minutes.length == 1) {
                "0$minutes"
            } else {
                minutes
            }

            val timeNow = "$hh:$mm"
            // only accept double digits time format
            val zdt = today.atTime(LocalTime.parse(timeNow)).atZone(zone)

            return !(merchantStart.isAfter(zdt) || merchantEnd.isBefore(zdt))
        }
        return true // open
    }

    private fun checkDistance(distance: Double): String {
        val take2Numbers = String.format("%.2f", distance)

        return if (take2Numbers[0].toString() != "0") {
            String.format("%.2f km", distance)
        } else {
            val toMeters = take2Numbers.toDouble() * 1000
            String.format("%.0f m", toMeters)
        }
    }
}
