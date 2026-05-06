package com.shield.browser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shield.browser.data.local.dao.*
import com.shield.browser.data.local.entity.*

@Database(
    entities = [
        BookmarkEntity::class,
        HistoryEntity::class,
        AdBlockRuleEntity::class,
        DownloadEntity::class,
        TabEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao
    abstract fun historyDao(): HistoryDao
    abstract fun adBlockDao(): AdBlockDao
    abstract fun downloadDao(): DownloadDao
    abstract fun tabDao(): TabDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shield_browser_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}