package com.klen0010.flinders.zootreasurehunt.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klen0010.flinders.zootreasurehunt.model.Badge
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import com.klen0010.flinders.zootreasurehunt.model.rarityColor


@Composable
fun BadgeScreen(
    badges: List<Badge>,
    currentSteps: Int
) {

    val highestBadge = badges
        .filter { it.unlocked }
        .maxByOrNull { it.requiredSteps }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Safari Badges",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = highestBadge?.let {
                "Current Rank: ${it.name}"
            } ?: "No badges unlocked yet",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Grid-like layout
        val rows = badges.chunked(3)

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row.forEach { badge ->
                        BadgeItem(
                            badge = badge,
                            currentSteps = currentSteps
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun BadgeItem(
    badge: Badge,
    currentSteps: Int
){

    val scale = remember { Animatable(0.8f) }
    val glowAlpha = remember { Animatable(0f) }

    LaunchedEffect(badge.unlocked) {
        if (badge.unlocked) {
            scale.animateTo(
                targetValue = 1.15f,
                animationSpec = tween(250)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(250)
            )

            glowAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(500)
            )
        }
    }

    val bgColor = if (badge.unlocked) Color(0xFFFFD700) else Color(0xFF444444)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {

        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(scale.value)
                .clip(CircleShape)
                .background(bgColor)
                .border(
                    width = 3.dp,
                    color = if (badge.unlocked)
                        rarityColor(badge.rarity)
                    else
                        Color.DarkGray,
                    shape = CircleShape
                )
                .graphicsLayer {
                    shadowElevation = if (badge.unlocked) 20f else 0f
                    alpha = 1f
                },
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = if (badge.unlocked) "🏆" else "🔒",
                fontSize = 26.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = badge.name,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = if (badge.unlocked) {
                "Unlocked"
            } else {
                "${badge.requiredSteps - currentSteps} steps remaining"
            },
            fontSize = 10.sp,
            color = if (badge.unlocked) Color(0xFFFFD700) else Color.LightGray
        )
    }
}