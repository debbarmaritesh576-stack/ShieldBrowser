package com.shield.browser.data.model

data class TabInfo(
    val id: String,
    val title: String,
    val url: String,
    val favicon: String? = null
)