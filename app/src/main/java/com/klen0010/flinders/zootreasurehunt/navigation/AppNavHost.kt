package com.klen0010.flinders.zootreasurehunt.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

// Handles all the screen switching and animations
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
        // Fancy sliding animations when moving between screens
        enterTransition = {
            fadeIn(animationSpec = tween(3000)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(3000)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(3000)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(3000)
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(3000)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(3000)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(3000)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(3000)
            )
        }
    ) {
        // The main list of zoo sightings
        composable<HomeDestination> {
            val uiState by viewModel.uiState.collectAsState()

            ListScreen(
                sightings = uiState.sightings,
                onEditClick = { viewModel.selectSightingForEdit(it) },
                onDelete = { viewModel.deleteSighting(it) }
            )
        }

        // New Stats view to see how you're doing
        composable<StatsDestination> {
            val uiState by viewModel.uiState.collectAsState()
            StatsScreen(sightings = uiState.sightings)
        }

        // App preferences
        composable<SettingsDestination> {
            val uiState by viewModel.uiState.collectAsState()

            SettingsScreen(
                isSortByName = uiState.isSortByName,
                onSortChange = { viewModel.toggleSortOrder(it) }
            )
        }

        // About Screen
        composable<AboutDestination> {
            AboutScreen()
        }
    }
}
