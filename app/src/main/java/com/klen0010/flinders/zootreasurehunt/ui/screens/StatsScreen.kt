package com.klen0010.flinders.zootreasurehunt.ui.screens

import android.hardware.SensorManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klen0010.flinders.zootreasurehunt.R
import com.klen0010.flinders.zootreasurehunt.model.Sighting
import androidx.compose.ui.platform.LocalContext
import com.klen0010.flinders.zootreasurehunt.ui.components.MiniMap
import com.klen0010.flinders.zootreasurehunt.viewmodel.ZooViewModel

// This screen shows the user's progress in the treasure hunt
@Composable
fun StatsScreen(
    sightings: List<Sighting>,
    stepCount: Int,
    hasStepCounter: Boolean,
    viewModel: ZooViewModel
) {

    val total = sightings.size
    val found = sightings.count { it.isFound }
    val progress = if (total > 0) found.toFloat() / total else 0f


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = stringResource(id = R.string.stats_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 32.dp, top = 16.dp)
        )

        // Progress Card
        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$found / $total",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(id = R.string.stats_label),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // The progress bar
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                
                Text(
                    text = stringResource(id = R.string.stats_percentage, (progress * 100).toInt()),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Message
        Text(
            text = when {
                progress >= 1f -> stringResource(id = R.string.stats_msg_complete)
                progress >= 0.5f -> stringResource(id = R.string.stats_msg_half)
                progress > 0f -> stringResource(id = R.string.stats_msg_start)
                else -> stringResource(id = R.string.stats_msg_none)
            },
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (!hasStepCounter) {
                    Text("Step counter not supported on this device")
                }else{
                    Text(
                        text = "Steps Taken",
                        style = MaterialTheme.typography.titleMedium
                    )
                }


                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$stepCount",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        val context = LocalContext.current

        Spacer(modifier = Modifier.height(24.dp))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Your Location",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                MiniMap(context, sightings, viewModel)
            }
        }
    }
}