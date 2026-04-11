package com.klen0010.flinders.zootreasurehunt.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore is a modern way to save simple app settings
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

// This class remembers things like how you want to sort your list
class SettingsRepository @Inject constructor (
    @ApplicationContext private val context: Context
){
    companion object {
        val SORT_BY_NAME = booleanPreferencesKey("sort_by_name")
    }

    // A stream of updates for the sort preference
    val sortByNameFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[SORT_BY_NAME] ?: true }

    // Save whether the user wants to sort by name or not
    suspend fun setSortByName(isSortByName: Boolean) {
        context.dataStore.edit { it[SORT_BY_NAME] = isSortByName }
    }
}
