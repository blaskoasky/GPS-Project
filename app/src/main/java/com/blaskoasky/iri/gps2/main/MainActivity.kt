package com.blaskoasky.iri.gps2.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.blaskoasky.iri.gps2.MapsActivity
import com.blaskoasky.iri.gps2.MapsActivity.Companion.EXTRA_ALTITUDE
import com.blaskoasky.iri.gps2.MapsActivity.Companion.EXTRA_LONGITUDE
import com.blaskoasky.iri.gps2.databinding.ActivityMainBinding
import com.blaskoasky.iri.gps2.dto.MerchantLocation
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_CODE_LOCATION = 69
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var locationViewModel: LocationViewModel

    private lateinit var _adapter: LocationAdapter

    private var _location = ArrayList<MerchantLocation>()
    private var _latitude = ""
    private var _longitude = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLocation.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            intent.putExtra(EXTRA_ALTITUDE, _latitude)
            intent.putExtra(EXTRA_LONGITUDE, _longitude)
            startActivity(intent)
        }

        requestLocationUpdates()
    }

    private fun requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationUpdates()
        } else {
            val permissionRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                requestPermissions(permissionRequest, PERMISSION_CODE_LOCATION)
            }
        }
    }

    private fun locationUpdates() {
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        locationViewModel.getLocationLiveData().observe(this, { location ->
            val latitude = location.latitude
            val longitude = location.longitude

            _latitude = latitude
            _longitude = longitude

            // DIUBAH NANTI
            _location.add(MerchantLocation("blank", latitude, longitude))

            _adapter = LocationAdapter()
            _adapter.setLatitudeLongitude(_location)
            with(binding.rvSimple) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = _adapter
            }
            //SAMPE SINI

            binding.tvAddress.text = locationGeocode(latitude, longitude)
            binding.tvLatitude.text = latitude
            binding.tvLongitude.text = longitude
        })
    }

    private fun locationGeocode(latitude: String, longitude: String): String {

        val geocoder = Geocoder(this, Locale.getDefault())
        val addreses = geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1) as List<Address>

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
                    locationUpdates()
                } else {
                    Toast.makeText(this, "Unable to get Permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}





