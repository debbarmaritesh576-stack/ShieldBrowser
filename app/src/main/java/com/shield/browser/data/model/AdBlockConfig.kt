package com.shield.browser.data.model

data class AdBlockConfig(
    val sourceTypeIndex: Int, // 0=Default, 1=Local, 2=Remote
    val localFilePath: String?,
    val remoteUrl: String?
)