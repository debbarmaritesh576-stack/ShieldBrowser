package com.shield.browser.utils

import android.net.Uri
import com.shield.browser.data.model.UrlDisplayMode

object UrlFormatter {
    fun getDisplayText(url: String, title: String?, isLoading: Boolean, mode: UrlDisplayMode): String {
        if (isLoading) return url
        if (url.startsWith("about:") || url.isEmpty()) return ""
        return when (mode) {
            UrlDisplayMode.DOMAIN_ONLY -> Uri.parse(url).host?.removePrefix("www.") ?: url
            UrlDisplayMode.FULL_URL -> url
        }
    }
}