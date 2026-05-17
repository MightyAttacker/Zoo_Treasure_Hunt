package com.klen0010.flinders.zootreasurehunt.ui.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.klen0010.flinders.zootreasurehunt.model.Sighting
import android.os.Looper
import com.google.android.gms.location.*
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.klen0010.flinders.zootreasurehunt.viewmodel.ZooViewModel

@SuppressLint("MissingPermission")
@Composable
fun MiniMap(
    context: Context,
    sightings: List<Sighting>,
    viewModel: ZooViewModel
) {
    val appContext = LocalContext.current

    val userLocation by viewModel.userLocation.collectAsState()

    val fusedLocationClient =
        remember { LocationServices.getFusedLocationProviderClient(appContext) }

    var permissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted = granted
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    LaunchedEffect(permissionGranted) {
        if (!permissionGranted) return@LaunchedEffect

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            3000L
        ).build()

        fusedLocationClient.requestLocationUpdates(
            request,
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val loc = result.lastLocation ?: return

                    viewModel.updateUserLocation(
                        LatLng(loc.latitude, loc.longitude)
                    )
                }
            },
            Looper.getMainLooper()
        )
    }

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(userLocation) {
        cameraPositionState.move(
            CameraUpdateFactory.newLatLngZoom(userLocation, 17f)
        )
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = permissionGranted
        )
    ) {
        sightings.forEach { sighting ->
            val lat = sighting.latitude
            val lng = sighting.longitude

            if (lat != null && lng != null) {
                Marker(
                    state = MarkerState(LatLng(lat, lng)),
                    title = sighting.name,
                    snippet = if (sighting.isFound) "Found!" else "Not found yet"
                )
            }
        }
    }
}