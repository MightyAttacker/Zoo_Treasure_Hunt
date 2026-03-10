    package com.klen0010.flinders.zootreasurehunt
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Home
    import androidx.compose.material.icons.filled.Info
    import androidx.compose.ui.graphics.vector.ImageVector
    import kotlinx.serialization.Serializable

    @Serializable
    object HomeDestination

    @Serializable
    object AboutDestination

    sealed class BottomNavItem (
        var label: String,
        var icon: ImageVector,
        var route: String)
    {
        data object Home : BottomNavItem("Home", Icons.Default.Home, "home_destination")
        data object About : BottomNavItem("About", Icons.Default.Info, "about_destination")
    }