package com.shield.browser.utils

import com.shield.browser.ShieldBrowserApp

object AdBlockEngine {

    fun isBlocked(url: String): Boolean {
        return try {
            val host = java.net.URL(url).host ?: return false
            val normalizedHost = host.removePrefix("www.")
            
            ShieldBrowserApp.blockedDomains.any { blockedDomain ->
                normalizedHost == blockedDomain || normalizedHost.endsWith(".$blockedDomain")
            }
        } catch (e: Exception) {
            false
        }
    }
}