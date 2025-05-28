package com.micahnyabuto.statussaver.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.statusDataStore by preferencesDataStore(name = "status_preferences")

object StatusPreferences {
    private val WHATSAPP_SAF_URI = stringPreferencesKey("whatsapp_saf_uri")
    private val WHATSAPP_BUSINESS_SAF_URI = stringPreferencesKey("whatsapp_business_saf_uri")

    suspend fun saveWhatsAppUri(context: Context, uri: String) {
        context.statusDataStore.edit { preferences ->
            preferences[WHATSAPP_SAF_URI] = uri
        }
    }

    suspend fun saveWhatsAppBusinessUri(context: Context, uri: String) {
        context.statusDataStore.edit { preferences ->
            preferences[WHATSAPP_BUSINESS_SAF_URI] = uri
        }
    }

    fun getWhatsAppUri(context: Context): Flow<String?> {
        return context.statusDataStore.data.map { preferences ->
            preferences[WHATSAPP_SAF_URI]
        }
    }

    fun getWhatsAppBusinessUri(context: Context): Flow<String?> {
        return context.statusDataStore.data.map { preferences ->
            preferences[WHATSAPP_BUSINESS_SAF_URI]
        }
    }
}