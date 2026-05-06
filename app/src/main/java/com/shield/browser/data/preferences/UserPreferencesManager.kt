package com.shield.browser.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesManager(private val context: Context) {

    private val SEARCH_ENGINE_KEY = stringPreferencesKey("search_engine_id")
    private val THEME_KEY = stringPreferencesKey("app_theme")
    private val INCOGNITO_KEY = booleanPreferencesKey("is_incognito")
    private val ADBLOCK_ENABLED_KEY = booleanPreferencesKey("adblock_enabled")

    val searchEngineId: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[SEARCH_ENGINE_KEY] ?: "google"
    }

    val appTheme: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[THEME_KEY] ?: "SYSTEM"
    }

    val isIncognito: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[INCOGNITO_KEY] ?: false
    }

    val isAdBlockEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[ADBLOCK_ENABLED_KEY] ?: true
    }

    suspend fun setSearchEngine(id: String) {
        context.dataStore.edit { it[SEARCH_ENGINE_KEY] = id }
    }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { it[THEME_KEY] = theme }
    }

    suspend fun setIncognito(enabled: Boolean) {
        context.dataStore.edit { it[INCOGNITO_KEY] = enabled }
    }

    suspend fun setAdBlockEnabled(enabled: Boolean) {
        context.dataStore.edit { it[ADBLOCK_ENABLED_KEY] = enabled }
    }
}