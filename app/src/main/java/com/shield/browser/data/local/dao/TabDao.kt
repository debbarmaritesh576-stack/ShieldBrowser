package com.shield.browser.data.local.dao

import androidx.room.*
import com.shield.browser.data.local.entity.TabEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TabDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTab(tab: TabEntity)

    @Delete
    suspend fun deleteTab(tab: TabEntity)

    @Query("SELECT * FROM tabs ORDER BY lastAccessed DESC")
    fun getAllTabs(): Flow<List<TabEntity>>

    @Query("DELETE FROM tabs")
    suspend fun clearAllTabs()
}