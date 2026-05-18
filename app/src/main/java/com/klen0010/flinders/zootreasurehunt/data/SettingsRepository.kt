package com.klen0010.flinders.zootreasurehunt.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.klen0010.flinders.zootreasurehunt.ui.screens.SortOption
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Save app settings
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        val SORT_OPTION = stringPreferencesKey("sort_option")
    }

    // Observe current sort setting
    val sortOptionFlow: Flow<SortOption> = context.dataStore.data
        .map { preferences ->

            when (preferences[SORT_OPTION]) {
                SortOption.DATE.name -> SortOption.DATE
                SortOption.DISTANCE.name -> SortOption.DISTANCE
                else -> SortOption.NAME
            }
        }

    // Save selected sort option
    suspend fun setSortOption(sortOption: SortOption) {
        context.dataStore.edit {
            it[SORT_OPTION] = sortOption.name
        }
    }
}