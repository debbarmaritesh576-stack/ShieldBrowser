package com.shield.browser.data.local.dao

import androidx.room.*
import com.shield.browser.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: HistoryEntity)

    @Query("SELECT * FROM history ORDER BY dateVisited DESC")
    fun getAllHistory(): Flow<List<HistoryEntity>>

    @Query("DELETE FROM history WHERE url = :url")
    suspend fun deleteByUrl(url: String): Int
    
    @Query("DELETE FROM history")
    suspend fun clearAllHistory()
    
    @Query("SELECT COUNT(*) FROM history")
    suspend fun getCount(): Long
}