package com.shield.browser.data.model

data class HistoryEntry(
    val id: Long = 0,
    val title: String,
    val url: String,
    val dateVisited: Long = System.currentTimeMillis()
)