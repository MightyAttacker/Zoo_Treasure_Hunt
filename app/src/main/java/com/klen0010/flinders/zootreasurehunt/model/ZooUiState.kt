package com.klen0010.flinders.zootreasurehunt.model

import kotlinx.serialization.Serializable
import com.klen0010.flinders.zootreasurehunt.ui.screens.SortOption


// This class holds everything the UI needs to know about to draw the screen
@Serializable
data class ZooUiState(
    val sightings: List<Sighting> = emptyList(),
    val selectedSort: SortOption = SortOption.NAME,
    val selectedSighting: Sighting? = null,
    val isDialogVisible: Boolean = false
)