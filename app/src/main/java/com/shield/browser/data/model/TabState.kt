package com.shield.browser.data.model

data class TabState(
    val tabs: List<TabInfo> = emptyList(),
    val currentTabId: String? = null
)