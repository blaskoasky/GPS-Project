package com.blaskoasky.iri.gps2.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.blaskoasky.iri.gps2.tools.LocationLiveData

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationLiveData = LocationLiveData(application)

    fun getLocationLiveData() = locationLiveData

}