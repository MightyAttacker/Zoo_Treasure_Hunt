package com.klen0010.flinders.zootreasurehunt.data

import android.content.Context
import com.klen0010.flinders.zootreasurehunt.model.Sighting
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject
import com.klen0010.flinders.zootreasurehunt.R

// This class handles saving and loading zoo sightings to a file on the phone
class FileSightingRepository @Inject constructor (
    @ApplicationContext private val context: Context
) : SightingRepository {
    val fileName: String = "sightings.json"

    // Add a new animal sighting to our list
    override suspend fun addSighting(sighting: Sighting) {
        val currentList = loadSightings().toMutableList()
        currentList.add(sighting)
        saveSightings(currentList)
    }

    // Update an existing sighting if the user changed something
    override suspend fun updateSighting(sighting: Sighting) {
        val currentList = loadSightings().map {
            if (it.id == sighting.id) sighting else it
        }
        saveSightings(currentList)
    }

    // Remove a sighting from the list
    override suspend fun deleteSighting(sighting: Sighting) {
        val currentList = loadSightings().filter { it.id != sighting.id }
        saveSightings(currentList)
    }

    // Convert the list to JSON and write it to internal storage
    override suspend fun saveSightings(sightings: List<Sighting>) {
        withContext(Dispatchers.IO){
            val jsonString = Json.encodeToString(sightings)

            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write(jsonString.toByteArray())
            }
        }

    }

    // Some starter animals if the list is empty
    private fun getDefaultSightings(): List<Sighting>{
        return listOf(
            Sighting(name = "Sumatran Tiger",
                imageRes = R.drawable.sumatran_tiger,
                latitude = -34.912324,
                longitude = 138.606814),
            Sighting(name = "Panda",
                imageRes = R.drawable.panda,
                latitude = -34.914280,
                longitude = 138.606413),
            Sighting(name = "American Alligator",
                imageRes = R.drawable.american_alligator,
                latitude = -34.912836,
                longitude = 138.608068),
            Sighting(name = "Australian Pelican",
                imageRes = R.drawable.australian_pelican,
                latitude = -34.913256,
                longitude = 138.606543),
            Sighting(name = "Meerkat",
                imageRes = R.drawable.meerkat,
                latitude = -34.913348,
                longitude = 138.605560),
            Sighting(name = "Komodo Dragon",
                imageRes = R.drawable.komodo_dragon,
                latitude = -34.914521,
                longitude = 138.605755),
            Sighting(name = "Giraffe",
                imageRes = R.drawable.giraffe,
                latitude = -34.913221,
                longitude = 138.605342),
            Sighting(name = "Dingo",
                imageRes = R.drawable.dingo,
                latitude = -34.912513,
                longitude = 138.606294)
        )
    }

    // Read the sightings from the file or give back the defaults if it's not there
    override suspend fun loadSightings(): List<Sighting> {
        return withContext(Dispatchers.IO) {
            val file = File(context.filesDir, fileName)
            if (!file.exists()) return@withContext getDefaultSightings()

            try {
                val jsonString = context.openFileInput(fileName).bufferedReader().use {
                    it.readText()
                }
                Json.decodeFromString(jsonString)
            } catch (e: Exception) {
                getDefaultSightings()
            }
        }
    }
}