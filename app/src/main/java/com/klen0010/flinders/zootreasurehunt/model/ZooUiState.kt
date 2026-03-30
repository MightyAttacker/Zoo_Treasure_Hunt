package com.klen0010.flinders.zootreasurehunt.model

import kotlinx.serialization.Serializable

@Serializable

data class ZooUiState(
    val sightings: List<Sighting> = emptyList(),
    val isSortByName: Boolean = true,
    val selectedSighting: Sighting? = null,
    val isDialogVisible: Boolean = false
)