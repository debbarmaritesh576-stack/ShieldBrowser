package com.shield.browser.data.source

import java.io.File

sealed interface AdBlockSourceType {
    data object Default : AdBlockSourceType
    data class Local(val file: File) : AdBlockSourceType
    data class Remote(val url: String) : AdBlockSourceType
}

fun getActiveSource(index: Int, localPath: String?, remoteUrl: String?): AdBlockSourceType {
    return when (index) {
        1 -> {
            val file = localPath?.let { File(it) }
            if (file != null && file.exists() && file.canRead()) AdBlockSourceType.Local(file)
            else AdBlockSourceType.Default
        }
        2 -> {
            if (!remoteUrl.isNullOrEmpty()) AdBlockSourceType.Remote(remoteUrl)
            else AdBlockSourceType.Default
        }
        else -> AdBlockSourceType.Default
    }
}