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
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// This is the brain of our app—it manages all the data and logic
@HiltViewModel
class ZooViewModel @Inject constructor(
    private val repository: SightingRepository,
    private val settingsRepository: SettingsRepository,
    application: Application
) : AndroidViewModel(application) {
    private val _rawSightings = MutableStateFlow<List<Sighting>>(emptyList())
    private val _uiState = MutableStateFlow(ZooUiState())
    
    // This is what the UI observes to stay up to date
    val uiState:StateFlow<ZooUiState> = _uiState.asStateFlow()

    init{
        // Load up our saved sightings when the app starts
        viewModelScope.launch {
            val savedSightings = repository.loadSightings()
            _rawSightings.value = savedSightings
        }
        
        // Combine the list with our settings to sort it correctly
        viewModelScope.launch {
            combine(_rawSightings, settingsRepository.sortByNameFlow) { list, sortByName ->
                val sortedList = if (sortByName) {
                    list.sortedBy { it.name }
                } else {
                    list.sortedByDescending { it.isFound }
                }
                _uiState.value.copy(sightings = sortedList, isSortByName = sortByName)
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

    // Goodbye animal!
    fun deleteSighting(sighting: Sighting) {
        val newList = _rawSightings.value.filter { it.id != sighting.id }
        updateAndSave(newList)
    }

    // Change how the list is sorted and remember it
    fun toggleSortOrder(sortByName: Boolean) {
        viewModelScope.launch {
            settingsRepository.setSortByName(sortByName)
        }
    }
}