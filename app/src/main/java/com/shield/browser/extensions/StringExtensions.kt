package com.shield.browser.extensions

fun String.isUrl(): Boolean {
    return this.contains(".") && !this.contains(" ")
}

fun String.toSearchQuery(): String {
    return if (this.isUrl()) this else "https://www.google.com/search?q=$this"
}

fun String.trimTrailingSlash(): String {
    return if (this.endsWith("/")) this.substring(0, this.length - 1) else this
}