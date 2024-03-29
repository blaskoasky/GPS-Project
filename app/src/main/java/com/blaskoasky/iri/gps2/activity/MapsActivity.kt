package com.blaskoasky.iri.gps2.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blaskoasky.iri.gps2.R
import com.blaskoasky.iri.gps2.model.entity.MerchantEntity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    companion object {
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_LONGITUDE = "extra_longitude"
        const val EXTRA_LOCATIONS_MERCHANT = "extra_locations_merchant"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        actionBar?.setDisplayHomeAsUpEnabled(true)
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Biru biru location
        mMap.isMyLocationEnabled = true

        val myLatitude = intent.getStringExtra(EXTRA_LATITUDE)!!.toDouble()
        val myLongitude = intent.getStringExtra(EXTRA_LONGITUDE)!!.toDouble()
        val myLocation = LatLng(myLatitude, myLongitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))


        val allListMerchant = intent.getParcelableArrayListExtra<MerchantEntity>(EXTRA_LOCATIONS_MERCHANT)
        allListMerchant?.let { showAllMarkerMerchant(it) }
    }

    private fun showAllMarkerMerchant(allListMerchant: ArrayList<MerchantEntity>) {
        allListMerchant.forEach {
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(it.latitude.toDouble(), it.longitude.toDouble()))
                    .title(it.merchantName)
                    .snippet("Open Hours: ${it.openHours}")
            )
        }
    }
}
