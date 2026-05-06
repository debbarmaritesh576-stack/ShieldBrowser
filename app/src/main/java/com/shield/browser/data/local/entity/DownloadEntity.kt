package com.shield.browser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloads")
data class DownloadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fileName: String,
    val url: String,
    val status: String, // PENDING, DOWNLOADING, COMPLETED, FAILED
    val progress: Int = 0,
    val filePath: String? = null,
    val dateAdded: Long = System.currentTimeMillis()
)