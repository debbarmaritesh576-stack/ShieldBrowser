package com.shield.browser.extensions

fun <T> List<T>.takeIfNotEmpty(): List<T>? {
    return if (this.isNotEmpty()) this else null
}