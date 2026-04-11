package com.klen0010.flinders.zootreasurehunt.model

import kotlinx.serialization.Serializable

// This class holds everything the UI needs to know about to draw the screen
@Serializable
data class ZooUiState(
    val sightings: List<Sighting> = emptyList(),
    val isSortByName: Boolean = true,
    // If this isn't null, it means we're currently editing this specific animal
    val selectedSighting: Sighting? = null,
    val isDialogVisible: Boolean = false
)