package com.shield.browser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabs")
data class TabEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val url: String,
    val favicon: String? = null,
    val lastAccessed: Long = System.currentTimeMillis()
)