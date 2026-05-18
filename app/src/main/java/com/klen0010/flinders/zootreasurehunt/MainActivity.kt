package com.klen0010.flinders.zootreasurehunt

import android.hardware.Sensor
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.LocationResult
import com.klen0010.flinders.zootreasurehunt.navigation.AppNavHost
import com.klen0010.flinders.zootreasurehunt.navigation.BottomNavItem
import com.klen0010.flinders.zootreasurehunt.ui.components.EditSightingDialog
import com.klen0010.flinders.zootreasurehunt.ui.theme.ZooTreasureHuntTheme
import com.klen0010.flinders.zootreasurehunt.viewmodel.ZooViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.content.Context
import android.hardware.SensorManager
import com.klen0010.flinders.zootreasurehunt.data.StepCounterManager
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.klen0010.flinders.zootreasurehunt.ui.components.BadgeUnlockedOverlay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var stepCounterManager: StepCounterManager


    override fun onCreate(savedInstanceState: Bundle?) {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        stepCounterManager = StepCounterManager(this)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                100
            )
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ZooViewModel = viewModel()
            val badgeEvent by viewModel.badgeEvents.collectAsState()

            LaunchedEffect(badgeEvent) {
                badgeEvent?.let { badge ->

                    // simple popup first (we can upgrade later)
                    android.widget.Toast.makeText(
                        this@MainActivity,
                        "🏆 Badge Unlocked: ${badge.name}",
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                }
            }

            LaunchedEffect(Unit) {
                stepCounterManager.steps.collect {
                    viewModel.updateSteps(it)
                }
            }

            viewModel.setHasStepCounter(stepCounterManager.hasStepCounter)

            MaterialTheme {
                ZooApp(viewModel)
            }
        }
        startLocationUpdates()
        stepCounterManager.startListening()
        val sensorManager =
            getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val sensor =
            sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (sensor == null) {
            Log.d("STEP", "No step counter sensor")
        } else {
            Log.d("STEP", "Step counter sensor found")
        }

    }

    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000L)
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(5000)
        .setMaxUpdateDelayMillis(15000)
        .build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
            }
        }
    }

    private fun startLocationUpdates() {
        try {
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        stepCounterManager.stopListening()
    }

}

@Composable
fun ZooApp(viewModel: ZooViewModel) {
    val navController = rememberNavController()
    val badgeEvent by viewModel.badgeEvents.collectAsState()
    val steps by viewModel.stepCount.collectAsState()

    val bottomItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Stats,
        BottomNavItem.Badges,
        BottomNavItem.Settings,
        BottomNavItem.About
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            // That bar at the bottom for switching screens
            NavigationBar {
                bottomItems.forEach { item ->
                    val isSelected = currentDestination?.hasRoute(item.route::class) == true
                    val label = stringResource(item.labelRes)
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // This handles showing the right screen based on where you are
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel
        )

        if (badgeEvent != null) {
            BadgeUnlockedOverlay(
                badge = badgeEvent!!,
                onDismiss = { viewModel.clearBadgeEvent() }
            )
        }
        // If someone clicked edit, show the pop-up
        if (uiState.isDialogVisible) {
            uiState.selectedSighting?.let { sighting ->
                EditSightingDialog(
                    sighting = sighting,
                    onDismiss = { viewModel.dismissDialog() },
                    onSave = { viewModel.updateSighting(it) }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ZooAppPreview() {
    ZooTreasureHuntTheme {
        Text("Preview")
    }
}