package com.klen0010.flinders.zootreasurehunt.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

// These objects are just labels for our screens so the navigation knows where to go
@Serializable
object HomeDestination

@Serializable
object StatsDestination

@Serializable
object AboutDestination

@Serializable
object SettingsDestination

// This class helps us build the bottom bar by linking labels, icons, and destinations together
sealed class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: Any
) {
    data object Home : BottomNavItem(
        "Home",
        Icons.Default.Home,
        HomeDestination
    )

    data object Stats : BottomNavItem(
        "Stats",
        Icons.Default.BarChart,
        StatsDestination
    )

    data object Settings : BottomNavItem(
        "Settings",
        Icons.Filled.Settings,
        SettingsDestination
    )

    data object About : BottomNavItem(
        "About",
        Icons.Default.Info,
        AboutDestination
    )
}