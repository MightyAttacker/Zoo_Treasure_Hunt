package com.klen0010.flinders.zootreasurehunt.model

import kotlinx.serialization.Serializable
import java.util.UUID

// This is the data model for a single animal sighting
@Serializable
data class Sighting(
    // We give every sighting a unique ID so we can find it later
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val isFound: Boolean = false,
    val photoPath: String? = null,
    val notes: String = "",
    // A default image just in case one isn't provided
    val imageUrl: String = "https://wilk0077.github.io/comp2012-images/assets-sm/african-lion-ai.jpg"
)