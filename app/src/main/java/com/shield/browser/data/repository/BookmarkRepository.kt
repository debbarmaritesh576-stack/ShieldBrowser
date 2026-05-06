package com.shield.browser.data.repository

import com.shield.browser.data.local.AppDatabase
import com.shield.browser.data.local.entity.BookmarkEntity
import com.shield.browser.data.model.BookmarkEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookmarkRepository(private val db: AppDatabase) {

    private val dao = db.bookmarkDao()

    private fun BookmarkEntity.toModel() = BookmarkEntry(
        id = id, title = title, url = url, folder = folder, dateAdded = dateAdded
    )

    private fun BookmarkEntry.toEntity() = BookmarkEntity(
        id = id, title = title, url = url, folder = folder, dateAdded = dateAdded
    )

    fun getAllBookmarks(): Flow<List<BookmarkEntry>> {
        return dao.getAllBookmarks().map { it.map { entity -> entity.toModel() } }
    }

    suspend fun isBookmark(url: String): Boolean {
        val normalized = if (url.endsWith("/")) url.substring(0, url.length - 1) else url
        val alternate = if (url.endsWith("/")) url.substring(0, url.length - 1) else "$url/"
        return dao.getBookmarkByUrl(normalized, alternate) != null
    }

    suspend fun addBookmark(entry: BookmarkEntry): Boolean {
        val normalized = if (entry.url.endsWith("/")) entry.url.substring(0, entry.url.length - 1) else entry.url
        val alternate = if (entry.url.endsWith("/")) entry.url.substring(0, entry.url.length - 1) else "${entry.url}/"
        
        if (dao.getBookmarkByUrl(normalized, alternate) != null) return false
        
        val id = dao.insertBookmark(entry.toEntity())
        return id != -1L
    }

    suspend fun deleteBookmark(entry: BookmarkEntry): Boolean {
        val normalized = if (entry.url.endsWith("/")) entry.url.substring(0, entry.url.length - 1) else entry.url
        val alternate = if (entry.url.endsWith("/")) entry.url.substring(0, entry.url.length - 1) else "${entry.url}/"
        return dao.deleteByUrl(normalized, alternate) > 0
    }
}