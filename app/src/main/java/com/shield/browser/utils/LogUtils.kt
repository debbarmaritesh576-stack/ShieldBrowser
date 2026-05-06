package com.shield.browser.utils

import android.util.Log

object LogUtils {
    private const val TAG = "ShieldBrowser"
    
    fun d(message: String) {
        Log.d(TAG, message)
    }
    
    fun e(message: String, throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
    }
}