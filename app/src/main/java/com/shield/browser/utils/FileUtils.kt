package com.shield.browser.utils

import android.os.Environment
import java.io.File

object FileUtils {
    fun getDownloadsDir(): File {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    }
}