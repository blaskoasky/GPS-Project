package com.blaskoasky.iri.gps2.Main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.blaskoasky.iri.gps2.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var geocoder: Geocoder
    private lateinit var addreses: List<Address>

    private val PERMISSION_CODE_LOCATION = 69

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepRequestLocationUpdates()
    }

    private fun prepRequestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationUpdates()
        } else {
            val permissionRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                requestPermissions(permissionRequest, PERMISSION_CODE_LOCATION)
            }
        }
    }

    private fun requestLocationUpdates() {
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        locationViewModel.getLocationLiveData().observe(this, { location ->
            val latitude = location.latitude
            val longitude = location.longitude

            binding.tvAddress.text = locationGeocode(latitude, longitude)
            binding.tvLatitude.text = latitude
            binding.tvLongitude.text = longitude
        })
    }

    private fun locationGeocode(latitude: String, longitude: String): String {
        geocoder = Geocoder(this, Locale.getDefault())
        addreses = geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1)

        return addreses[0].getAddressLine(0).toString()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdates()
                } else {
                    Toast.makeText(this, "Unable to get Permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}





