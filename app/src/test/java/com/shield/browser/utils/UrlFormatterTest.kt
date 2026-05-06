package com.shield.browser.utils

import com.shield.browser.data.model.UrlDisplayMode
import org.junit.Assert.assertEquals
import org.junit.Test

class UrlFormatterTest {

    @Test
    fun `test full url display`() {
        val result = UrlFormatter.getDisplayText("https://www.google.com/search?q=test", "Google", false, UrlDisplayMode.FULL_URL)
        assertEquals("https://www.google.com/search?q=test", result)
    }

    @Test
    fun `test domain only display`() {
        val result = UrlFormatter.getDisplayText("https://www.google.com/search?q=test", "Google", false, UrlDisplayMode.DOMAIN_ONLY)
        assertEquals("google.com", result)
    }

    @Test
    fun `test loading state shows full url`() {
        val result = UrlFormatter.getDisplayText("https://www.google.com", "Google", true, UrlDisplayMode.DOMAIN_ONLY)
        assertEquals("https://www.google.com", result)
    }
}