package com.shield.browser.data.local.dao

import androidx.room.*
import com.shield.browser.data.local.entity.AdBlockRuleEntity

@Dao
interface AdBlockDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRules(rules: List<AdBlockRuleEntity>)

    @Query("SELECT COUNT(*) FROM adblock_rules")
    suspend fun getRuleCount(): Long
    
    @Query("SELECT EXISTS(SELECT 1 FROM adblock_rules WHERE domain = :domain)")
    suspend fun isDomainBlocked(domain: String): Boolean
    
    @Query("DELETE FROM adblock_rules")
    suspend fun clearAllRules()
}