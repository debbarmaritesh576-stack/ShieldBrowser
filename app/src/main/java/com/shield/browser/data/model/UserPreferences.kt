package com.shield.browser.data.model

data class UserPreferences(
    val searchEngineId: String = "google",
    val urlDisplayMode: UrlDisplayMode = UrlDisplayMode.FULL_URL,
    val isIncognito: Boolean = false,
    val adBlockEnabled: Boolean = true
)