package com.shield.browser.data.importer

import com.shield.browser.data.model.BookmarkEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class NetscapeBookmarkImporter {

    suspend fun importBookmarks(inputStream: InputStream): Result<List<BookmarkEntry>> {
        return withContext(Dispatchers.IO) {
            try {
                val bookmarks = mutableListOf<BookmarkEntry>()
                val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
                var line: String?
                var currentFolder = ""

                while (reader.readLine().also { line = it } != null) {
                    val trimmed = line?.trim() ?: continue
                    if (trimmed.startsWith("<DT><H3>", ignoreCase = true)) {
                        currentFolder = extractText(trimmed, "<H3>", "</H3>")
                    } else if (trimmed.startsWith("<DT><A HREF=", ignoreCase = true)) {
                        val url = extractAttr(trimmed, "HREF")
                        val title = extractText(trimmed, ">", "</A>")
                        if (!url.isNullOrEmpty()) {
                            bookmarks.add(BookmarkEntry(title = title ?: url, url = url, folder = currentFolder))
                        }
                    }
                }
                reader.close()
                Result.success(bookmarks)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun extractAttr(line: String, attr: String): String? {
        val start = line.indexOf("$attr=\"", ignoreCase = true)
        if (start == -1) return null
        val valStart = start + attr.length + 2
        val valEnd = line.indexOf("\"", valStart)
        return if (valEnd == -1) null else line.substring(valStart, valEnd)
    }

    private fun extractText(line: String, startTag: String, endTag: String): String {
        val start = line.indexOf(startTag, ignoreCase = true)
        if (start == -1) return ""
        val textStart = start + startTag.length
        val end = line.indexOf(endTag, textStart, ignoreCase = true)
        return if (end == -1) "" else line.substring(textStart, end).trim()
    }
}