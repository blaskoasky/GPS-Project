package com.blaskoasky.iri.gps2.Main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.blaskoasky.iri.gps2.LocationLiveData

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationliveData = LocationLiveData(application)

    fun getLocationLiveData() = locationliveData

}