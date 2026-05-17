package com.klen0010.flinders.zootreasurehunt.model

import kotlinx.serialization.Serializable
import java.util.UUID

// Data model for a single animal sighting
@Serializable
data class Sighting(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val isFound: Boolean = false,
    val imageRes: Int,
    val photoPath: String? = null,
    val notes: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
)