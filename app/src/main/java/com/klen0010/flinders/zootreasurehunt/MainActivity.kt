package com.klen0010.flinders.zootreasurehunt

import android.os.Bundle
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.klen0010.flinders.zootreasurehunt.ui.theme.ZooTreasureHuntTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.klen0010.flinders.zootreasurehunt.data.FileSightingRepository
import com.klen0010.flinders.zootreasurehunt.data.SettingsRepository
import com.klen0010.flinders.zootreasurehunt.data.SightingRepository
import com.klen0010.flinders.zootreasurehunt.viewmodel.ZooViewModel
import com.klen0010.flinders.zootreasurehunt.navigation.AboutDestination
import com.klen0010.flinders.zootreasurehunt.navigation.BottomNavItem
import com.klen0010.flinders.zootreasurehunt.navigation.HomeDestination
import com.klen0010.flinders.zootreasurehunt.navigation.SettingsDestination
import com.klen0010.flinders.zootreasurehunt.ui.components.EditSightingDialog
import com.klen0010.flinders.zootreasurehunt.ui.screens.AboutScreen
import com.klen0010.flinders.zootreasurehunt.ui.screens.ListScreen
import com.klen0010.flinders.zootreasurehunt.ui.screens.SettingsScreen
import android.app.Application
import androidx.lifecycle.ViewModelProvider

    class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            val sightingRepository = FileSightingRepository(applicationContext)
            val settingsRepository = SettingsRepository(applicationContext)
            setContent {
                MaterialTheme {
                    ZooApp(
                        repository = sightingRepository,
                        settingsRepository = settingsRepository
                    )
                }
            }
        }
    }

@Composable
fun ZooApp(
    repository: SightingRepository,
    settingsRepository: SettingsRepository
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val viewModel: ZooViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ZooViewModel(
                    repository = repository,
                    settingsRepository = settingsRepository,
                    application = context.applicationContext as Application
                ) as T
            }
        }
    )
    val bottomItems = listOf(BottomNavItem.Home, BottomNavItem.Settings, BottomNavItem.About)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomItems.forEach { item ->
                    val isSelected = currentDestination?.route == item.route
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = HomeDestination,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<HomeDestination> {
                    ListScreen(
                        sightings = uiState.sightings,
                        onEditClick = { animal ->
                            viewModel.selectSightingForEdit(animal)
                        },
                        onDelete = { animal ->
                            viewModel.deleteSighting(animal)
                        }
                    )
                }
                composable<SettingsDestination> {
                    SettingsScreen(
                        isSortByName = uiState.isSortByName,
                        onSortChange = { viewModel.toggleSortOrder(it) }
                    )
                }
                composable<AboutDestination> {
                    AboutScreen()
                }
            }
        if (uiState.isDialogVisible) {
            uiState.selectedSighting?.let { sighting ->
                EditSightingDialog(
                    sighting = sighting,
                    onDismiss = { viewModel.dismissDialog() },
                    onSave = { viewModel.dismissDialog() }
                )
            }
        }
            }
        }

@Preview(showBackground = true)
@Composable
fun ZooAppPreview() {
    val context = LocalContext.current

    ZooTreasureHuntTheme {
        ZooApp(
            repository = FileSightingRepository(context),
            settingsRepository = SettingsRepository(context)
        )
    }
}