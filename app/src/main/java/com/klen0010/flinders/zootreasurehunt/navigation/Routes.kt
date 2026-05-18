package com.klen0010.flinders.zootreasurehunt.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.klen0010.flinders.zootreasurehunt.R
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

@Serializable
object BadgesDestination

// This class helps us build the bottom bar by linking label resources, icons, and destinations together
sealed class BottomNavItem(
    val labelRes: Int,
    val icon: ImageVector,
    val route: Any
) {
    data object Home : BottomNavItem(
        R.string.nav_home,
        Icons.Default.Home,
        HomeDestination
    )

    data object Stats : BottomNavItem(
        R.string.nav_stats,
        Icons.Default.BarChart,
        StatsDestination
    )

    data object Badges : BottomNavItem(
        R.string.nav_badges,
        Icons.Default.EmojiEvents,
        BadgesDestination
    )

    data object Settings : BottomNavItem(
        R.string.nav_settings,
        Icons.Filled.Settings,
        SettingsDestination
    )

    data object About : BottomNavItem(
        R.string.nav_about,
        Icons.Default.Info,
        AboutDestination
    )
}