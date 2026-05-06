package com.shield.browser.data.model

data class SearchEngine(
    val id: String,
    val name: String,
    val queryUrl: String,
    val suggestionUrl: String? = null,
    val isDefault: Boolean = false
)