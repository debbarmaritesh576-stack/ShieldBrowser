package com.shield.browser.data.model

data class DownloadItem(
    val id: Long = 0,
    val fileName: String,
    val url: String,
    val status: String, // PENDING, DOWNLOADING, COMPLETED, FAILED
    val progress: Int = 0,
    val dateAdded: Long = System.currentTimeMillis()
)