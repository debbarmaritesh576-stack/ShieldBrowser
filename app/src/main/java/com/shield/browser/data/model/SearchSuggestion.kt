package com.shield.browser.data.model

data class SearchSuggestion(
    val query: String,
    val displayText: String = query
)