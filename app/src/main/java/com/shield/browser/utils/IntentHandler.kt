package com.shield.browser.utils

import android.content.Intent
import android.net.Uri

object IntentHandler {
    fun extractUrlFromIntent(intent: Intent?): String? {
        if (intent == null) return null
        return when (intent.action) {
            Intent.ACTION_VIEW -> intent.dataString
            Intent.ACTION_WEB_SEARCH -> {
                val query = intent.getStringExtra(android.app.SearchManager.QUERY)
                if (!query.isNullOrBlank()) "https://www.google.com/search?q=${Uri.encode(query)}" else null
            }
            else -> null
        }
    }
}