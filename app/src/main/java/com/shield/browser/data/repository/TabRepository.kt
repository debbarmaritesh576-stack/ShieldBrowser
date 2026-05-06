package com.shield.browser.data.repository

import com.shield.browser.data.local.AppDatabase
import com.shield.browser.data.local.entity.TabEntity
import com.shield.browser.data.model.TabInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TabRepository(private val db: AppDatabase) {

    private val dao = db.tabDao()

    private fun TabEntity.toModel() = TabInfo(
        id = id, title = title, url = url, favicon = favicon
    )

    private fun TabInfo.toEntity() = TabEntity(
        id = id, title = title, url = url, favicon = favicon
    )

    fun getAllTabs(): Flow<List<TabInfo>> {
        return dao.getAllTabs().map { it.map { entity -> entity.toModel() } }
    }

    suspend fun addTab(tab: TabInfo) {
        dao.insertTab(tab.toEntity())
    }

    suspend fun deleteTab(tab: TabInfo) {
        dao.deleteTab(tab.toEntity())
    }
}