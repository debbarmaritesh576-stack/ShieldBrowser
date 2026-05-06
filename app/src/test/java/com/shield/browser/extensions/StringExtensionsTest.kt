package com.shield.browser.extensions

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StringExtensionsTest {

    @Test
    fun `test isUrl with valid url`() {
        assertTrue("google.com".isUrl())
    }

    @Test
    fun `test isUrl with search query`() {
        assertFalse("hello world".isUrl())
    }

    @Test
    fun `test trimTrailingSlash`() {
        assertEquals("google.com", "google.com/".trimTrailingSlash())
    }
}