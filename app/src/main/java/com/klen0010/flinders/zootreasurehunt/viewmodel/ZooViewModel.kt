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

class ZooViewModel(
    private val repository: SightingRepository,
    private val settingsRepository: SettingsRepository,
    application: Application
) : AndroidViewModel(application) {
    private val _rawSightings = MutableStateFlow<List<Sighting>>(emptyList())
    private val _uiState = MutableStateFlow(ZooUiState())
    val uiState:StateFlow<ZooUiState> = _uiState.asStateFlow()

    init{
        viewModelScope.launch {
            val savedSightings = repository.loadSightings()
            _rawSightings.value = savedSightings
        }
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

    fun selectSightingForEdit(sighting: Sighting?){
        _uiState.value = _uiState.value.copy(selectedSighting = sighting, isDialogVisible = sighting != null)
    }

    fun dismissDialog(){
        _uiState.value = _uiState.value.copy(selectedSighting = null, isDialogVisible = false)
    }

    private fun updateAndSave(newList: List<Sighting>){
        _rawSightings.value = newList
        viewModelScope.launch { repository.saveSightings(newList) }
    }

    fun deleteSighting(sighting: Sighting) {
        val newList = _rawSightings.value.filter { it.id != sighting.id }
        updateAndSave(newList)
    }

    fun toggleSortOrder(sortByName: Boolean) {
        viewModelScope.launch {
            settingsRepository.setSortByName(sortByName)
        }
    }
}