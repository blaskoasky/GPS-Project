package com.blaskoasky.iri.gps2.main

import android.app.Application
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import com.blaskoasky.iri.gps2.LocationLiveData
import java.util.*

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationLiveData = LocationLiveData(application)

    fun getLocationLiveData() = locationLiveData

}