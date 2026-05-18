package com.klen0010.flinders.zootreasurehunt.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.klen0010.flinders.zootreasurehunt.ui.screens.AboutScreen
import com.klen0010.flinders.zootreasurehunt.ui.screens.ListScreen
import com.klen0010.flinders.zootreasurehunt.ui.screens.SettingsScreen
import com.klen0010.flinders.zootreasurehunt.ui.screens.StatsScreen
import com.klen0010.flinders.zootreasurehunt.viewmodel.ZooViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.android.gms.maps.model.LatLng
import com.klen0010.flinders.zootreasurehunt.data.distanceMeters
import com.klen0010.flinders.zootreasurehunt.ui.screens.SortOption

// Helper to figure out the order of our tabs
private fun getRouteOrder(destination: NavDestination?): Int {
    return when {
        destination?.hasRoute<HomeDestination>() == true -> 0
        destination?.hasRoute<StatsDestination>() == true -> 1
        destination?.hasRoute<SettingsDestination>() == true -> 2
        destination?.hasRoute<AboutDestination>() == true -> 3
        else -> 0
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: ZooViewModel
) {
    NavHost(

        navController = navController,
        startDestination = HomeDestination,
        modifier = modifier,
        enterTransition = {
            // If the new tab is to the right of the old one, slide from the Right (Start)
            // Otherwise, slide from the Left (End)
            val direction = if (getRouteOrder(targetState.destination) > getRouteOrder(initialState.destination)) {
                AnimatedContentTransitionScope.SlideDirection.Start
            } else {
                AnimatedContentTransitionScope.SlideDirection.End
            }
            fadeIn(animationSpec = tween(1000)) + slideIntoContainer(direction, animationSpec = tween(1000))
        },
        exitTransition = {
            val direction = if (getRouteOrder(targetState.destination) > getRouteOrder(initialState.destination)) {
                AnimatedContentTransitionScope.SlideDirection.Start
            } else {
                AnimatedContentTransitionScope.SlideDirection.End
            }
            fadeOut(animationSpec = tween(1000)) + slideOutOfContainer(direction, animationSpec = tween(1000))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(1000)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(1000)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(1000)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(1000)
            )
        }
    ) {
        composable<HomeDestination> {

            val uiState by viewModel.uiState.collectAsState()
            val userLocation by viewModel.userLocation.collectAsState()

            val sortedSightings = when (uiState.selectedSort) {

                SortOption.NAME -> {
                    uiState.sightings.sortedBy { it.name }
                }

                SortOption.DATE -> {
                    uiState.sightings
                }

                SortOption.DISTANCE -> {
                    uiState.sightings.sortedBy { sighting ->

                        val lat = sighting.latitude
                        val lng = sighting.longitude

                        if (lat != null && lng != null) {
                            distanceMeters(userLocation, LatLng(lat, lng)).toDouble()
                        } else {
                            Double.MAX_VALUE
                        }
                    }
                }
            }

            ListScreen(
                sightings = sortedSightings,
                userLocation = userLocation,
                onEditClick = { viewModel.selectSightingForEdit(it) },
                onDelete = { viewModel.deleteSighting(it) }
            )
        }

        composable<StatsDestination> {
            val uiState by viewModel.uiState.collectAsState()
            val steps by viewModel.stepCount.collectAsState()
            val hasStepCounter by viewModel.hasStepCounter.collectAsState()
            StatsScreen(
                sightings = uiState.sightings,
                stepCount = steps,
                hasStepCounter = hasStepCounter,
                viewModel = viewModel
            )
        }

        composable<SettingsDestination> {
            val uiState by viewModel.uiState.collectAsState()
            SettingsScreen(
                selectedSort = uiState.selectedSort,
                onSortChange = { viewModel.toggleSortOrder(it) }
            )
        }

        composable<AboutDestination> {
            AboutScreen()
        }
    }
}
