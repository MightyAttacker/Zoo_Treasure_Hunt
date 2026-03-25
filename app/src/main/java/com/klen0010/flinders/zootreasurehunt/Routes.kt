    package com.klen0010.flinders.zootreasurehunt
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Home
    import androidx.compose.material.icons.filled.Info
    import androidx.compose.material.icons.filled.Settings
    import androidx.compose.ui.graphics.vector.ImageVector
    import kotlinx.serialization.Serializable


    @Serializable
    object HomeDestination

    @Serializable
    object AboutDestination

    @Serializable
    object SettingsDestination

    sealed class BottomNavItem (
        val label: String,
        val icon: ImageVector,
        val route: Any)
    {
        data object Home : BottomNavItem(
            "Home",
            Icons.Default.Home,
            HomeDestination)
        data object Settings : BottomNavItem(
            "Settings",
            Icons.Filled.Settings,
            SettingsDestination
            )
        data object About : BottomNavItem("About", Icons.Default.Info, AboutDestination)
    }