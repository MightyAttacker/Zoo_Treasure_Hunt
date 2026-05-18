package com.klen0010.flinders.zootreasurehunt.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.klen0010.flinders.zootreasurehunt.model.Sighting
import com.klen0010.flinders.zootreasurehunt.data.SettingsRepository
import com.klen0010.flinders.zootreasurehunt.data.SightingRepository
import com.klen0010.flinders.zootreasurehunt.model.ZooUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.klen0010.flinders.zootreasurehunt.ui.screens.SortOption


// This is the brain of our app—it manages all the data and logic
@HiltViewModel
class ZooViewModel @Inject constructor(
    private val repository: SightingRepository,
    private val settingsRepository: SettingsRepository,
    application: Application
) : AndroidViewModel(application) {
    private val _rawSightings = MutableStateFlow<List<Sighting>>(emptyList())
    private val _uiState = MutableStateFlow(ZooUiState())

    private val _stepCount = MutableStateFlow(0)
    val stepCount: StateFlow<Int> = _stepCount

    fun updateSteps(steps: Int) {
        _stepCount.value = steps
    }

    private val _hasStepCounter = MutableStateFlow(false)
    val hasStepCounter: StateFlow<Boolean> = _hasStepCounter

    fun setHasStepCounter(value: Boolean) {
        _hasStepCounter.value = value
    }
    
    // This is what the UI observes to stay up to date
    val uiState:StateFlow<ZooUiState> = _uiState.asStateFlow()

    init {
        // Load saved sightings
        viewModelScope.launch {
            _rawSightings.value = repository.loadSightings()
        }

        // Combine sightings + sort option
        viewModelScope.launch {
            combine(
                _rawSightings,
                settingsRepository.sortOptionFlow
            ) { list, sortOption ->

                val sortedList = when (sortOption) {

                    SortOption.NAME -> {
                        list.sortedBy { it.name }
                    }

                    SortOption.DATE -> {
                        list
                    }

                    SortOption.DISTANCE -> {
                        list
                    }
                }

                _uiState.value.copy(
                    sightings = sortedList,
                    selectedSort = sortOption
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    // Pick an animal to edit and show the dialog
    fun selectSightingForEdit(sighting: Sighting?){
        _uiState.value = _uiState.value.copy(selectedSighting = sighting, isDialogVisible = sighting != null)
    }

    // Close the edit pop-up
    fun dismissDialog(){
        _uiState.value = _uiState.value.copy(selectedSighting = null, isDialogVisible = false)
    }

    // Update the list and save it to the phone's storage
    private fun updateAndSave(newList: List<Sighting>){
        _rawSightings.value = newList
        viewModelScope.launch { repository.saveSightings(newList) }
    }

    // New function to handle updating a single sighting
    fun updateSighting(sighting: Sighting) {
        val newList = _rawSightings.value.map {
            if (it.id == sighting.id) sighting else it
        }
        updateAndSave(newList)
        dismissDialog()
    }

    // Delete animal
    fun deleteSighting(sighting: Sighting) {
        val newList = _rawSightings.value.filter { it.id != sighting.id }
        updateAndSave(newList)
    }

    // Change how the list is sorted and remember it
    fun toggleSortOrder(sortOption: SortOption) {
        viewModelScope.launch {
            settingsRepository.setSortOption(sortOption)
        }
    }

    private val _userLocation = MutableStateFlow(
        LatLng(-34.9143, 138.6050)// default Adelaide Zoo
    )

    val userLocation: StateFlow<LatLng> = _userLocation.asStateFlow()

    fun updateUserLocation(location: LatLng) {
        _userLocation.value = location
    }
}