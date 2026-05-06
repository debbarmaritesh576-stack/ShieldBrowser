package com.shield.browser.utils

import java.net.URL

object NetworkUtils {
    fun getDomain(url: String): String? {
        return try {
            URL(url).host?.removePrefix("www.")
        } catch (e: Exception) {
            null
        }
    }
}