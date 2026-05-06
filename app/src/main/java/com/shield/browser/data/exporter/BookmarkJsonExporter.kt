package com.shield.browser.data.exporter

import android.content.Context
import android.os.Environment
import com.shield.browser.data.model.BookmarkEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter

object BookmarkJsonExporter {

    private const val KEY_URL = "url"
    private const val KEY_TITLE = "title"
    private const val KEY_FOLDER = "folder"
    private const val KEY_DATE_ADDED = "dateAdded"

    suspend fun exportBookmarksToFile(context: Context, bookmarks: List<BookmarkEntry>): Result<File> {
        return withContext(Dispatchers.IO) {
            try {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                var exportFile = File(downloadsDir, "ShieldBookmarks.json")
                
                var counter = 1
                while (exportFile.exists()) {
                    exportFile = File(downloadsDir, "ShieldBookmarks-$counter.json")
                    counter++
                }

                val jsonArray = JSONArray()
                for (bookmark in bookmarks) {
                    val jsonObject = JSONObject().apply {
                        put(KEY_TITLE, bookmark.title)
                        put(KEY_URL, bookmark.url)
                        put(KEY_FOLDER, bookmark.folder ?: "")
                        put(KEY_DATE_ADDED, bookmark.dateAdded)
                    }
                    jsonArray.put(jsonObject)
                }

                FileWriter(exportFile).use { writer ->
                    writer.write(jsonArray.toString(2))
                }

                Result.success(exportFile)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}