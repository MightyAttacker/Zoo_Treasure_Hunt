package com.klen0010.flinders.zootreasurehunt.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.klen0010.flinders.zootreasurehunt.model.Badge


@Composable
fun BadgeUnlockedOverlay(
    badge: Badge,
    onDismiss: () -> Unit
) {
    val scale = remember { Animatable(0.3f) }

    LaunchedEffect(Unit) {

        // zoom in animation
        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        )

        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(200)
        )

        // optional sound hook (see step 5)
        delay(1500)

        onDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f)),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(scale.value)
        ) {

            Text(
                text = "🎉 NEW BADGE!",
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "🏆 ${badge.name}",
                fontSize = 28.sp,
                color = Color(0xFFFFD700),
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "${badge.requiredSteps} steps completed",
                fontSize = 14.sp,
                color = Color.LightGray
            )
        }
    }
}