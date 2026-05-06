package com.shield.browser

import android.app.Application
import com.shield.browser.data.local.AppDatabase
import com.shield.browser.data.manager.AdBlockManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShieldBrowserApp : Application() {

    companion object {
        lateinit var database: AppDatabase
        var blockedDomains: Set<String> = emptySet()
    }

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(applicationContext)
        
        // Load AdBlock rules in background
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val manager = AdBlockManager(applicationContext)
                val source = manager.getAdBlockSource()
                val result = source.loadRules()
                result.onSuccess { domains ->
                    blockedDomains = domains
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}