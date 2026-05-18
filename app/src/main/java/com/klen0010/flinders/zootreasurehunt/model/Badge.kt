package com.klen0010.flinders.zootreasurehunt.model

import androidx.compose.ui.graphics.Color

enum class BadgeRarity {
    BRONZE,
    SILVER,
    GOLD,
    DIAMOND
}

data class Badge(
    val name: String,
    val requiredSteps: Int,
    val unlocked: Boolean = false,
    val rarity: BadgeRarity = BadgeRarity.BRONZE
)

fun rarityColor(rarity: BadgeRarity): Color {
    return when (rarity) {
        BadgeRarity.BRONZE -> Color(0xFFCD7F32)
        BadgeRarity.SILVER -> Color(0xFFC0C0C0)
        BadgeRarity.GOLD -> Color(0xFFFFD700)
        BadgeRarity.DIAMOND -> Color(0xFF00E5FF)
    }
}