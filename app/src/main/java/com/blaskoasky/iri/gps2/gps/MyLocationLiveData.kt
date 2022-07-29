package com.blaskoasky.iri.gps2.gps

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.blaskoasky.iri.gps2.model.MyLocationModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class MyLocationLiveData(context: Context) : LiveData<MyLocationModel>() {

    private var fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    companion object {
        val THREE_MINUTE: Long = 180000
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = THREE_MINUTE
            fastestInterval = THREE_MINUTE / 3 // 1 Menit
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location ->
            location.also {
                setLocationData(it)
            }
        }
        startLocationUpdates()
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult ?: return

            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }

    }

    private fun setLocationData(location: Location) {
        value = MyLocationModel(location.longitude.toString(), location.latitude.toString())
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}