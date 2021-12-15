package com.blaskoasky.iri.gps2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blaskoasky.iri.gps2.dto.MerchantLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    companion object {
        const val EXTRA_ALTITUDE = "extra_altitude"
        const val EXTRA_LONGITUDE = "extra_longitude"
        const val EXTRA_LOCATIONS_MERCHANT = "key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Biru biru location
        mMap.isMyLocationEnabled = true

        val altitude = intent.getStringExtra(EXTRA_ALTITUDE)!!.toDouble()
        val longitude = intent.getStringExtra(EXTRA_LONGITUDE)!!.toDouble()

        val myLocation = LatLng(altitude, longitude)
        //mMap.addMarker(MarkerOptions().position(myLocation).title("My location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))

        val x =
            intent.getParcelableArrayListExtra<MerchantLocation>(EXTRA_LOCATIONS_MERCHANT)

        x?.forEach {
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(it.latitude.toDouble(), it.longitude.toDouble()))
                    .title(it.merchantName)
            )
        }

    }

}