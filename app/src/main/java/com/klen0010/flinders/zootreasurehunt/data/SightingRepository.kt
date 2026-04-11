package com.klen0010.flinders.zootreasurehunt.data

import com.klen0010.flinders.zootreasurehunt.model.Sighting

// This is like a blueprint for how we handle our zoo data
interface SightingRepository {
    suspend fun saveSightings(sightings: List<Sighting>)
    suspend fun loadSightings(): List<Sighting>
    suspend fun addSighting(sighting: Sighting)
    suspend fun updateSighting(sighting: Sighting)
    suspend fun deleteSighting(sighting: Sighting)
}