package com.blaskoasky.iri.gps2.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MerchantLocation(
        val merchantName: String = "",
        val latitude: String = "",
        val longitude: String = "",
        val distance: String = "0km",
) : Parcelable {
}