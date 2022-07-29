package com.blaskoasky.iri.gps2.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MerchantEntity(
        var merchantName: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var distance: Double = 0.0,
        var address: String = "",
        var merchantId: String = "",
        var imgMerchant: String = "",
        var openHours: String = "",
) : Parcelable {
}