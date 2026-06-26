package com.klen0010.flinders.zootreasurehunt.data

import android.location.Location
import com.google.android.gms.maps.model.LatLng

//Distance Function
fun distanceMeters(user: LatLng, target: LatLng): Float {
    val results = FloatArray(1)

    //Distance Calculation
    Location.distanceBetween(
        user.latitude,
        user.longitude,
        target.latitude,
        target.longitude,
        results
    )

    return results[0]
}