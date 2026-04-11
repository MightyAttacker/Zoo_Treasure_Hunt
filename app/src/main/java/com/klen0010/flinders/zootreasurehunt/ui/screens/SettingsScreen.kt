package com.klen0010.flinders.zootreasurehunt.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.klen0010.flinders.zootreasurehunt.R

// Modernized settings screen with a clean card-based layout
@Composable
fun SettingsScreen(
    isSortByName: Boolean,
    onSortChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Page Title
        Text(
            text = stringResource(id = R.string.settings_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 24.dp, top = 16.dp)
        )

        // Category Label
        Text(
            text = stringResource(id = R.string.settings_sort_label),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )

        // Grouping options in an elevated card for a polished look
        ElevatedCard(
            modifier = Modifier.selectableGroup()
        ) {
            // Sort by Name Option
            ListItem(
                headlineContent = { Text(stringResource(id = R.string.settings_sort_name)) },
                supportingContent = { Text(stringResource(id = R.string.settings_sort_name_desc)) },
                leadingContent = { 
                    Icon(Icons.Default.SortByAlpha, contentDescription = null) 
                },
                trailingContent = {
                    RadioButton(
                        selected = isSortByName,
                        onClick = null // Click handled by ListItem
                    )
                },
                modifier = Modifier.clickable { onSortChange(true) }
            )
            
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // Sort by Date Option
            ListItem(
                headlineContent = { Text(stringResource(id = R.string.settings_sort_date)) },
                supportingContent = { Text(stringResource(id = R.string.settings_sort_date_desc)) },
                leadingContent = { 
                    Icon(Icons.Default.DateRange, contentDescription = null) 
                },
                trailingContent = {
                    RadioButton(
                        selected = !isSortByName,
                        onClick = null // Click handled by ListItem
                    )
                },
                modifier = Modifier.clickable { onSortChange(false) }
            )
        }
    }
}
