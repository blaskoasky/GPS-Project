package com.blaskoasky.iri.gps2.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blaskoasky.iri.gps2.databinding.ActivityMainBinding
import com.blaskoasky.iri.gps2.entity.MerchantEntity
import com.blaskoasky.iri.gps2.maps.MapsActivity
import com.blaskoasky.iri.gps2.maps.MapsActivity.Companion.EXTRA_LATITUDE
import com.blaskoasky.iri.gps2.maps.MapsActivity.Companion.EXTRA_LOCATIONS_MERCHANT
import com.blaskoasky.iri.gps2.maps.MapsActivity.Companion.EXTRA_LONGITUDE
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_CODE_LOCATION = 100
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var _adapter: MainAdapter

    private var _myLatitude = ""
    private var _myLongitude = ""
    private var arrayListMerchant: ArrayList<MerchantEntity> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        requestLocationUpdates()
        setLayout()
        myLocationLiveData()
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

    private fun setLayout() {
        binding.rvSimple.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                with(binding.btnShowAllMerchant) {
                    if (dy > 0 && visibility == View.VISIBLE) {
                        visibility = View.GONE
                    } else if (dy < 0 && visibility != View.VISIBLE) {
                        visibility = View.VISIBLE
                    }
                }
            }
        })

        binding.refreshLayout.setOnRefreshListener {

            binding.refreshLayout.isRefreshing = false

            merchantSaveSync(_myLatitude, _myLongitude)
        }

        binding.btnShowAllMerchant.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            intent.putExtra(EXTRA_LATITUDE, _myLatitude)
            intent.putExtra(EXTRA_LONGITUDE, _myLongitude)
            intent.putExtra(EXTRA_LOCATIONS_MERCHANT, arrayListMerchant)
            startActivity(intent)
        }

        with(binding.myLocationContainer) {
            setOnClickListener {
                binding.tvMyAddress.maxLines = 3
            }
            setOnLongClickListener {
                binding.tvMyAddress.maxLines = 1
                true
            }
        }
    }

    private fun myLocationLiveData() {
        viewModel.location.observe(this) { merchantList ->

            arrayListMerchant = merchantList

            _adapter = MainAdapter(this)
            _adapter.setLatitudeLongitude(merchantList)
            _adapter.notifyDataSetChanged()

            with(binding.rvSimple) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = _adapter
            }
        }
    }

    private fun locationUpdates() {
        locationViewModel.getLocationLiveData().observe(this) { location ->

            // keeps distance updated
            merchantSaveSync(location.latitude, location.longitude)

            with(binding.tvMyAddress) {
                text = locationGeocode(location.latitude, location.longitude)
            }

            // pass myLatLng
            _myLatitude = location.latitude
            _myLongitude = location.longitude
        }
    }

    private fun locationGeocode(latitude: String, longitude: String): String {

        val geocode = Geocoder(this, Locale.getDefault())
        val address =
            geocode.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1) as List<Address>

        return address[0].getAddressLine(0).toString()
    }

    private fun addAddressMerchant(mMerchant: MerchantEntity) {
        mMerchant.address = locationGeocode(mMerchant.latitude, mMerchant.longitude)
    }

    private fun merchantSaveSync(myLat: String, myLng: String) {
        arrayListMerchant.forEach { merchant ->

            // add distance gps to merchant
            viewModel.addDistance(
                myLat.toDouble(),
                myLng.toDouble(),
                merchant.latitude.toDouble(),
                merchant.longitude.toDouble(),
                merchant
            )

            // add open hours
            viewModel.addOpenHours(merchant)

            // add address merchant to dto
            addAddressMerchant(merchant)

            // save each merchant to firebase
            val merchantSave = MerchantEntity().apply {
                merchantName = merchant.merchantName
                latitude = merchant.latitude
                longitude = merchant.longitude
                distance = merchant.distance
                address = merchant.address
                merchantId = merchant.merchantId
                imgMerchant = merchant.imgMerchant
                openHours = merchant.openHours
            }
            viewModel.saveToFirebase(merchantSave)
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