package com.shield.browser.data.repository

import com.shield.browser.data.local.AppDatabase
import com.shield.browser.data.local.entity.HistoryEntity
import com.shield.browser.data.model.HistoryEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HistoryRepository(private val db: AppDatabase) {

    private val dao = db.historyDao()

    private fun HistoryEntity.toModel() = HistoryEntry(
        id = id, title = title, url = url, dateVisited = dateVisited
    )

    private fun HistoryEntry.toEntity() = HistoryEntity(
        id = id, title = title, url = url, dateVisited = dateVisited
    )

    fun getAllHistory(): Flow<List<HistoryEntry>> {
        return dao.getAllHistory().map { it.map { entity -> entity.toModel() } }
    }

    suspend fun addHistory(entry: HistoryEntry) {
        dao.insertHistory(entry.toEntity())
    }

    suspend fun deleteHistory(entry: HistoryEntry) {
        dao.deleteByUrl(entry.url)
    }
    
    suspend fun clearAllHistory() {
        dao.clearAllHistory()
    }
}