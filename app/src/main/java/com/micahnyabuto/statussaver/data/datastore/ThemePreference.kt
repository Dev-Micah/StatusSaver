package com.micahnyabuto.statussaver.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.datastore by preferencesDataStore(name = "settings")
object ThemePreference {

    private val Context.dataStore by preferencesDataStore("settings")

    private val THEME_KEY = booleanPreferencesKey("dark_mode")

    suspend fun saveThemePreference(context: Context, isDarkMode: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = isDarkMode
        }
    }

    fun getThemePreference(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[THEME_KEY] == true
        }
    }
}