package com.blaskoasky.iri.gps2.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blaskoasky.iri.gps2.entity.MerchantEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sqrt

class MainViewModel : ViewModel() {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var _locations: MutableLiveData<ArrayList<MerchantEntity>> =
        MutableLiveData<ArrayList<MerchantEntity>>()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToLocation()
    }

    private fun listenToLocation() {
        firestore.collection("mcLocation").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen Failed", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val allLocations = ArrayList<MerchantEntity>()
                val documents = snapshot.documents

                documents.forEach {
                    val location = it.toObject(MerchantEntity::class.java)
                    if (location != null) {
                        allLocations.add(location)
                    }
                }

                _locations.value = allLocations
            }
        }
    }

    fun saveToFirebase(merchant: MerchantEntity) {
        val document = firestore.collection("mcLocation").document(merchant.merchantId)
        merchant.merchantId = document.id

        val set = document.set(merchant)
        set.addOnSuccessListener {
            Log.d("Firebase Save", "document saved")
        }
        set.addOnFailureListener {
            Log.d("Firebase Failed to save", it.toString())
        }
    }

    fun addDistance(
        lat1: Double,
        lng1: Double,
        lat2: Double,
        lng2: Double,
        mDistance: MerchantEntity
    ) {
        val p = 0.017453292519943295
        val a = 0.5 - cos((lat2 - lat1) * p) / 2 +
                cos(lat1 * p) * cos(lat2 * p) *
                (1 - cos((lng2 - lng1) * p)) / 2

        val distance = 12742 * asin(sqrt(a))

        mDistance.distance = distance
    }

    fun addOpenHours(merchant: MerchantEntity) {
        if (merchant.openHours == "") {
            merchant.openHours = "09:00 - 18:00"
        } else {
            merchant.openHours = merchant.openHours
        }
    }


    internal var location: MutableLiveData<ArrayList<MerchantEntity>>
        get() {
            return _locations
        }
        set(value) {
            _locations = value
        }
}