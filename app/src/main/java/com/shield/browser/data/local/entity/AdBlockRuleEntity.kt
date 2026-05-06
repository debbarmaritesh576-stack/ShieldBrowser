package com.shield.browser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "adblock_rules")
data class AdBlockRuleEntity(
    @PrimaryKey
    val domain: String,
    val source: String = "default"
)