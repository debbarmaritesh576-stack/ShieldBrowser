package com.shield.browser.data.manager

import android.content.Context
import com.shield.browser.data.preferences.AdBlockPreferences
import com.shield.browser.data.source.*
import kotlinx.coroutines.flow.first

class AdBlockManager(private val context: Context) {

    private val preferences = AdBlockPreferences(context)

    suspend fun getAdBlockSource(): AdBlockSource {
        val configIndex = preferences.sourceTypeIndex.first()
        val localPath = preferences.localFilePath.first()
        val remoteUrl = preferences.remoteUrl.first()
        
        val sourceType = getActiveSource(configIndex, localPath, remoteUrl)

        return when (sourceType) {
            is AdBlockSourceType.Default -> AssetsAdBlockSource(context)
            is AdBlockSourceType.Local -> FileAdBlockSource(sourceType.file)
            is AdBlockSourceType.Remote -> RemoteAdBlockSource(sourceType.url)
        }
    }
}