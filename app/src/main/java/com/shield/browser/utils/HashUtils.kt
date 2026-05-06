package com.shield.browser.utils

import java.io.InputStream
import java.security.MessageDigest

object HashUtils {

    fun computeMD5(inputStream: InputStream): String {
        val md5 = MessageDigest.getInstance("MD5")
        val buffer = ByteArray(8192)
        var bytesRead: Int
        
        try {
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                md5.update(buffer, 0, bytesRead)
            }
        } finally {
            inputStream.close()
        }

        val digest = md5.digest()
        return digest.toHex()
    }

    fun computeMD5(input: String): String {
        val md5 = MessageDigest.getInstance("MD5")
        val digest = md5.digest(input.toByteArray(Charsets.UTF_8))
        return digest.toHex()
    }

    private fun ByteArray.toHex(): String {
        val hexChars = "0123456789ABCDEF"
        val result = StringBuilder(this.size * 2)
        
        for (byte in this) {
            val i = byte.toInt()
            result.append(hexChars[i shr 4 and 0x0F])
            result.append(hexChars[i and 0x0F])
        }
        
        return result.toString()
    }
}