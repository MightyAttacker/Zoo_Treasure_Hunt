package com.klen0010.flinders.zootreasurehunt.ui.screens

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.klen0010.flinders.zootreasurehunt.R
import com.klen0010.flinders.zootreasurehunt.data.distanceMeters
import com.klen0010.flinders.zootreasurehunt.model.Sighting
import com.klen0010.flinders.zootreasurehunt.ui.components.SwipeableSighting

@Composable
fun ListScreen(
    sightings: List<Sighting>,
    userLocation: LatLng,
    onEditClick: (Sighting) -> Unit,
    onDelete: (Sighting) -> Unit
){
    val listState = rememberLazyListState()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
            items(
                items = sightings,
                key = { it.id }
            ) { animal ->

                val distance = if (animal.latitude != null && animal.longitude != null) {
                    distanceMeters(
                        userLocation,
                        LatLng(animal.latitude, animal.longitude)
                    )
                } else null

                SwipeableSighting(
                    sighting = animal,
                    distance = distance,
                    onEditClick = { onEditClick(animal) },
                    onSwipe = { onDelete(animal) }
                )
            }
        }
    }
}