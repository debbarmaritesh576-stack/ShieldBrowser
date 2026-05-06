package com.shield.browser.data.model

data class BookmarkEntry(
    val id: Long = 0,
    val title: String,
    val url: String,
    val folder: String? = null,
    val dateAdded: Long = System.currentTimeMillis()
)