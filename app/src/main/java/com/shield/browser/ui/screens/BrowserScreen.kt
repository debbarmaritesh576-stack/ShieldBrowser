package com.shield.browser.ui.screens

import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.shield.browser.core.WebViewManager
import com.shield.browser.core.PrivacyWebViewClient
import com.shield.browser.data.model.BookmarkEntry
import com.shield.browser.data.repository.BookmarkRepository
import com.shield.browser.data.repository.HistoryRepository
import com.shield.browser.ui.components.BottomToolbar
import com.shield.browser.ui.components.LoadingIndicator
import com.shield.browser.ui.components.UrlBar
import kotlinx.coroutines.launch

@Composable
fun BrowserScreen(
    initialUrl: String = "https://www.google.com",
    bookmarkRepository: BookmarkRepository,
    historyRepository: HistoryRepository,
    onNavigateToBookmarks: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var currentUrl by remember { mutableStateOf(initialUrl) }
    var currentTitle by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(false) }
    
    LaunchedEffect(currentUrl) {
        isBookmarked = bookmarkRepository.isBookmark(currentUrl)
    }

    val webView = remember {
        WebView(context).apply {
            WebViewManager.configureWebView(this, context)
            
            webViewClient = PrivacyWebViewClient(
                onUrlChanged = { url ->                     currentUrl = url 
                    canGoBack = this.canGoBack()
                    canGoForward = this.canGoForward()
                },
                onPageStarted = { isLoading = true },
                onPageFinished = { isLoading = false },
                onError = { /* Handle Error */ },
                historyRepository = historyRepository
            )
            
            webChromeClient = object : android.webkit.WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    currentTitle = title
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        webView.loadUrl(initialUrl)
    }

    Scaffold(
        topBar = {
            Column {
                UrlBar(
                    url = currentUrl,
                    title = currentTitle,
                    isLoading = isLoading,
                    onUrlSubmit = { url ->
                        if (url.isNotEmpty()) {
                            val finalUrl = if (url.contains(".") && !url.contains(" ")) url else "https://www.google.com/search?q=$url"
                            webView.loadUrl(finalUrl)
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {
                        scope.launch {
                            if (isBookmarked) {
                                val entry = BookmarkEntry(title = currentTitle ?: "Untitled", url = currentUrl)
                                bookmarkRepository.deleteBookmark(entry)
                                isBookmarked = false
                            } else {                                val entry = BookmarkEntry(title = currentTitle ?: "Untitled", url = currentUrl)
                                bookmarkRepository.addBookmark(entry)
                                isBookmarked = true
                            }
                        }
                    }) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = "Toggle Bookmark",
                            tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                LoadingIndicator(isLoading = isLoading)
            }
        },
        bottomBar = {
            BottomToolbar(
                canGoBack = canGoBack,
                canGoForward = canGoForward,
                onBackClick = { if (webView.canGoBack()) webView.goBack() },
                onForwardClick = { if (webView.canGoForward()) webView.goForward() },
                onRefreshClick = { webView.reload() },
                onHomeClick = { webView.loadUrl("https://www.google.com") },
                onMenuClick = { onNavigateToBookmarks() } // Simplified menu action
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AndroidView(
                factory = { webView },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}