package com.klen0010.flinders.zootreasurehunt.data

import android.content.Context
import com.klen0010.flinders.zootreasurehunt.model.Sighting
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject

// This class handles saving and loading our zoo sightings to a file on the phone
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

    // Update an existing sighting if we changed something
    override suspend fun updateSighting(sighting: Sighting) {
        val currentList = loadSightings().map {
            if (it.id == sighting.id) sighting else it
        }
        saveSightings(currentList)
    }

    // Remove a sighting from our list
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
            Sighting(name = "Lion", imageUrl = "https://wilk0077.github.io/comp2012-images/assets-sm/african-lion-ai.jpg"),
            Sighting(name = "Red Panda", imageUrl = "https://wilk0077.github.io/comp2012-images/assets-sm/red-panda-ai.jpg"),
            Sighting(name = "Giraffe", imageUrl = "https://wilk0077.github.io/comp2012-images/assets-sm/giraffe-ai.jpg"),
            Sighting(name = "Kangaroo", imageUrl = "https://wilk0077.github.io/comp2012-images/assets-sm/red-kangaroo-ai.jpg"),
            Sighting(name = "Penguin", imageUrl = "https://wilk0077.github.io/comp2012-images/assets-sm/penguin-ai.jpg")
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