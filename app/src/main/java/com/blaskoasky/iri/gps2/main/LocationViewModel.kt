package com.blaskoasky.iri.gps2.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.blaskoasky.iri.gps2.gps.MyLocationLiveData

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationLiveData = MyLocationLiveData(application)

    fun getLocationLiveData() = locationLiveData

}