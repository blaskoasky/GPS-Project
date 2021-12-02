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
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_CODE_LOCATION = 69
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var viewModel: MainViewModel

    private lateinit var _adapter: LocationAdapter

    private var _latitude = ""
    private var _longitude = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        binding.btnLocation.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            intent.putExtra(EXTRA_ALTITUDE, _latitude)
            intent.putExtra(EXTRA_LONGITUDE, _longitude)
            startActivity(intent)
        }

        viewModel.specimen.observe(this, { merchantList ->

            _adapter = LocationAdapter()
            _adapter.setLatitudeLongitude(merchantList)
            _adapter.notifyDataSetChanged()

            with(binding.rvSimple) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = _adapter
            }

            merchantList
        })

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
        locationViewModel.getLocationLiveData().observe(this, { location ->
            _latitude = location.latitude
            _longitude = location.longitude

            binding.tvAddress.text = locationGeocode(location.latitude, location.longitude)
            binding.tvLatitude.text = location.latitude
            binding.tvLongitude.text = location.longitude
        })
    }

    private fun locationGeocode(latitude: String, longitude: String): String {

        val geocoder = Geocoder(this, Locale.getDefault())
        val addreses =
            geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1) as List<Address>

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





