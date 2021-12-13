package com.blaskoasky.iri.gps2.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blaskoasky.iri.gps2.dto.MerchantLocation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class MainViewModel : ViewModel() {

    private var firestore: FirebaseFirestore

    private var _locations: MutableLiveData<ArrayList<MerchantLocation>> =
        MutableLiveData<ArrayList<MerchantLocation>>()

    init {
        firestore = FirebaseFirestore.getInstance()
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
                val allLocations = ArrayList<MerchantLocation>()
                val documents = snapshot.documents

                documents.forEach {
                    val location = it.toObject(MerchantLocation::class.java)
                    if (location != null) {
                        allLocations.add(location)
                    }
                }

                _locations.value = allLocations
            }
        }
    }

    internal var location: MutableLiveData<ArrayList<MerchantLocation>>
        get() {
            return _locations
        }
        set(value) {
            _locations = value
        }
}