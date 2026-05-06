package com.shield.browser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.shield.browser.data.repository.BookmarkRepository
import com.shield.browser.data.repository.HistoryRepository
import com.shield.browser.ui.theme.ShieldBrowserTheme
import com.shield.browser.ui.screens.BrowserScreen
import com.shield.browser.ui.screens.BookmarksScreen
import com.shield.browser.ui.screens.HistoryScreen

sealed class Screen {
    object Browser : Screen()
    object Bookmarks : Screen()
    object History : Screen()
}

class MainActivity : ComponentActivity() {
    
    private lateinit var bookmarkRepository: BookmarkRepository
    private lateinit var historyRepository: HistoryRepository
    private var currentScreen by mutableStateOf<Screen>(Screen.Browser)
    private var pendingUrl by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val db = ShieldBrowserApp.database
        bookmarkRepository = BookmarkRepository(db)
        historyRepository = HistoryRepository(db)

        setContent {
            ShieldBrowserTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    when (currentScreen) {
                        is Screen.Browser -> {
                            BrowserScreen(
                                initialUrl = pendingUrl ?: "https://www.google.com",
                                bookmarkRepository = bookmarkRepository,
                                historyRepository = historyRepository,
                                onNavigateToBookmarks = { currentScreen = Screen.Bookmarks },
                                onNavigateToHistory = { currentScreen = Screen.History }
                            )
                        }
                        is Screen.Bookmarks -> {
                            BookmarksScreen(
                                bookmarkRepository = bookmarkRepository,
                                onBookmarkClick = { url ->
                                    pendingUrl = url
                                    currentScreen = Screen.Browser
                                }
                            )
                        }
                        is Screen.History -> {
                            HistoryScreen(
                                historyRepository = historyRepository,
                                onHistoryClick = { url ->
                                    pendingUrl = url
                                    currentScreen = Screen.Browser
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}