package com.klen0010.flinders.zootreasurehunt.data

import android.content.Context
import com.klen0010.flinders.zootreasurehunt.Sighting
import kotlinx.serialization.json.Json
import java.io.File

class SightingRepository(private val context: Context) {
    val fileName: String = "sightings.json"

    fun saveSightings(
        sightings: List<Sighting>
    ){
        val jsonString = Json.encodeToString(sightings)

        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
            outputStream.write(jsonString.toByteArray())
        }
    }

    private fun getDefaultSightings(): List<Sighting>{
        return listOf(
            Sighting(name = "Lion", imageUrl = "https://wilk0077.github.io/comp2012-images/assets-sm/african-lion-ai.jpg"),
            Sighting(name = "Red Panda", imageUrl = "https://wilk0077.github.io/comp2012-images/assets-sm/red-panda-ai.jpg"),
            Sighting(name = "Giraffe", imageUrl = "https://wilk0077.github.io/comp2012-images/assets-sm/giraffe-ai.jpg"),
            Sighting(name = "Kangaroo", imageUrl = "https://wilk0077.github.io/comp2012-images/assets-sm/red-kangaroo-ai.jpg"),
            Sighting(name = "Penguin", imageUrl = "https://wilk0077.github.io/comp2012-images/assets-sm/penguin-ai.jpg")
        )
    }

    fun loadSightings(): List<Sighting>{
        val file = File(context.filesDir, fileName)

        if (!file.exists()) {
            return getDefaultSightings()
        }


        return try {
            val jsonString = context.openFileInput(fileName).bufferedReader().use { it.readText() }
            Json.decodeFromString(jsonString)
        } catch (e: Exception) {
            getDefaultSightings()
        }
    }

}