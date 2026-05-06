package com.shield.browser.data.local.dao

import androidx.room.*
import com.shield.browser.data.local.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBookmark(bookmark: BookmarkEntity): Long

    @Delete
    suspend fun deleteBookmark(bookmark: BookmarkEntity)

    @Query("SELECT * FROM bookmarks ORDER BY folder ASC, position ASC, title ASC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks WHERE url LIKE :url OR url LIKE :alternateUrl LIMIT 1")
    suspend fun getBookmarkByUrl(url: String, alternateUrl: String): BookmarkEntity?

    @Query("DELETE FROM bookmarks WHERE url = :url OR url = :alternateUrl")
    suspend fun deleteByUrl(url: String, alternateUrl: String): Int
    
    @Query("SELECT COUNT(*) FROM bookmarks")
    suspend fun getCount(): Long
}