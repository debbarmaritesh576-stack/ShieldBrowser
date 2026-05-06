package com.shield.browser.data.repository

import com.shield.browser.data.local.AppDatabase
import com.shield.browser.data.local.entity.DownloadEntity
import com.shield.browser.data.model.DownloadItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DownloadRepository(private val db: AppDatabase) {

    private val dao = db.downloadDao()

    private fun DownloadEntity.toModel() = DownloadItem(
        id = id, fileName = fileName, url = url, status = status, progress = progress, dateAdded = dateAdded
    )

    private fun DownloadItem.toEntity() = DownloadEntity(
        id = id, fileName = fileName, url = url, status = status, progress = progress, dateAdded = dateAdded
    )

    fun getAllDownloads(): Flow<List<DownloadItem>> {
        return dao.getAllDownloads().map { it.map { entity -> entity.toModel() } }
    }

    suspend fun addDownload(item: DownloadItem): Long {
        return dao.insertDownload(item.toEntity())
    }

    suspend fun updateDownload(item: DownloadItem) {
        dao.updateDownload(item.toEntity())
    }
}