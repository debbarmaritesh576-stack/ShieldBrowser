package com.shield.browser.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.adBlockDataStore: DataStore<Preferences> by preferencesDataStore(name = "adblock_prefs")

class AdBlockPreferences(private val context: Context) {

    private val SOURCE_TYPE_INDEX = intPreferencesKey("adblock_source_type_index")
    private val LOCAL_FILE_PATH = stringPreferencesKey("adblock_local_file_path")
    private val REMOTE_URL = stringPreferencesKey("adblock_remote_url")

    val sourceTypeIndex: Flow<Int> = context.adBlockDataStore.data.map { prefs ->
        prefs[SOURCE_TYPE_INDEX] ?: 0
    }

    val localFilePath: Flow<String?> = context.adBlockDataStore.data.map { prefs ->
        prefs[LOCAL_FILE_PATH]
    }

    val remoteUrl: Flow<String?> = context.adBlockDataStore.data.map { prefs ->
        prefs[REMOTE_URL]
    }

    suspend fun setSourceType(index: Int) {
        context.adBlockDataStore.edit { it[SOURCE_TYPE_INDEX] = index }
    }

    suspend fun setLocalFilePath(path: String?) {
        context.adBlockDataStore.edit { 
            if (path == null) it.remove(LOCAL_FILE_PATH) else it[LOCAL_FILE_PATH] = path 
        }
    }

    suspend fun setRemoteUrl(url: String?) {
        context.adBlockDataStore.edit { 
            if (url == null) it.remove(REMOTE_URL) else it[REMOTE_URL] = url 
        }
    }
}