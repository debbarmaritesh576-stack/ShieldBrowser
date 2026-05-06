package com.shield.browser.data.repository

import com.shield.browser.data.local.AppDatabase
import com.shield.browser.data.local.entity.AdBlockRuleEntity

class AdBlockRepository(private val db: AppDatabase) {

    private val dao = db.adBlockDao()

    suspend fun insertRules(rules: List<String>) {
        val entities = rules.map { AdBlockRuleEntity(domain = it) }
        dao.insertRules(entities)
    }

    suspend fun getRuleCount(): Long {
        return dao.getRuleCount()
    }
    
    suspend fun clearRules() {
        dao.clearAllRules()
    }
}