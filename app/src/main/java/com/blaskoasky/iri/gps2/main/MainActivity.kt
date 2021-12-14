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
import com.blaskoasky.iri.gps2.MapsActivity.Companion.EXTRA_LOCATIONS_MERCHANT
import com.blaskoasky.iri.gps2.MapsActivity.Companion.EXTRA_LONGITUDE
import com.blaskoasky.iri.gps2.databinding.ActivityMainBinding
import com.blaskoasky.iri.gps2.dto.MerchantLocation
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_CODE_LOCATION = 100
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var viewModel: MainViewModel

    private lateinit var _adapter: LocationAdapter

    private var _latitude = ""
    private var _longitude = ""

    private var arrayListLocation: ArrayList<MerchantLocation> = ArrayList()


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
            intent.putExtra(EXTRA_LOCATIONS_MERCHANT, arrayListLocation)
            startActivity(intent)
        }

        binding.btnSync.setOnClickListener {
            tesSync()
        }

        viewModel.location.observe(this, { merchantList ->

            arrayListLocation = merchantList

            _adapter = LocationAdapter()
            _adapter.setLatitudeLongitude(merchantList)
            _adapter.notifyDataSetChanged()

            with(binding.rvSimple) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = _adapter
            }
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


    private fun distance(
        lat1: Double,
        lng1: Double,
        lat2: Double,
        lng2: Double,
        mDistance: MerchantLocation
    ) {
        val p = 0.017453292519943295
        val a = 0.5 - cos((lat2 - lat1) * p) / 2 +
                cos(lat1 * p) * cos(lat2 * p) *
                (1 - cos((lng2 - lng1) * p)) / 2

        val distance = 12742 * asin(sqrt(a))

        val formattedDistance = String.format("%.2f km", distance)

        mDistance.distance = formattedDistance
    }

    //// PERCOBAAAAN <<<<<<<<<<<
    private fun tesSync() {
        arrayListLocation.forEach {
            distance(
                _latitude.toDouble(),
                _longitude.toDouble(),
                it.latitude.toDouble(),
                it.longitude.toDouble(),
                it
            )

            var merchant = MerchantLocation().apply {
                address = it.address
                distance = it.distance
                latitude = it.latitude
                longitude = it.longitude
                merchantId = it.merchantId
                merchantName = it.merchantName
            }
            viewModel.saveToFirebase(merchant)
        }
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





