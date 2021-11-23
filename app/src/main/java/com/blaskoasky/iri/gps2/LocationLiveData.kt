package com.blaskoasky.iri.gps2

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.blaskoasky.iri.gps2.dto.LocationDetails
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationLiveData(context: Context) : LiveData<LocationDetails>() {

    private var fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    companion object {
        val ONE_MINUTE: Long = 60000
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = ONE_MINUTE
            fastestInterval = ONE_MINUTE / 4
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
        value = LocationDetails(location.longitude.toString(), location.latitude.toString())
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

}