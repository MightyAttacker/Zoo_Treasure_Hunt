package com.klen0010.flinders.zootreasurehunt.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

// This class handles getting the user's location
class LocationTracker(context: Context) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Start listening for location updates
    @SuppressLint("MissingPermission")
    fun start(onLocation: (LatLng) -> Unit) {

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000L
        ).build()

        fusedLocationClient.requestLocationUpdates(
            request,
            object : LocationCallback() {

                override fun onLocationResult(result: LocationResult) {
                    val loc = result.lastLocation ?: return
                    onLocation(LatLng(loc.latitude, loc.longitude))
                }
            },
            Looper.getMainLooper()
        )
    }
}