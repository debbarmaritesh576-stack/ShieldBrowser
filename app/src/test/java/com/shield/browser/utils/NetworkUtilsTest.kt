package com.shield.browser.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NetworkUtilsTest {

    @Test
    fun `test get domain from valid url`() {
        val domain = NetworkUtils.getDomain("https://www.google.com/search")
        assertEquals("google.com", domain)
    }

    @Test
    fun `test get domain from invalid url`() {
        val domain = NetworkUtils.getDomain("not a url")
        assertNull(domain)
    }
}