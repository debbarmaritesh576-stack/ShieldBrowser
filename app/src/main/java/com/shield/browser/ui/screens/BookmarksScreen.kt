package com.shield.browser.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shield.browser.data.model.BookmarkEntry
import com.shield.browser.data.repository.BookmarkRepository
import com.shield.browser.ui.components.BookmarkItem
import com.shield.browser.ui.components.EmptyState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    bookmarkRepository: BookmarkRepository,
    onBookmarkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var bookmarks by remember { mutableStateOf<List<BookmarkEntry>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    
    LaunchedEffect(searchQuery) {
        bookmarkRepository.getAllBookmarks().collect { allBookmarks ->
            bookmarks = if (searchQuery.isEmpty()) {
                allBookmarks
            } else {
                allBookmarks.filter { 
                    it.title.contains(searchQuery, ignoreCase = true) || 
                    it.url.contains(searchQuery, ignoreCase = true) 
                }
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Bookmarks") },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            singleLine = true,
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            }
        )

        if (bookmarks.isEmpty()) {
            EmptyState(message = "No bookmarks found.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                items(bookmarks.size) { index ->
                    val bookmark = bookmarks[index]
                    
                    SwipeToDismissBox(
                        state = rememberSwipeToDismissBoxState(),
                        backgroundContent = {
                            Box(
                                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.errorContainer),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.padding(end = 16.dp))
                            }
                        },
                        enableDismissFromStartToEnd = false,
                        enableDismissFromEndToStart = true,
                        onDismissed = {
                            scope.launch {
                                bookmarkRepository.deleteBookmark(bookmark)
                            }
                        }
                    ) {
                        BookmarkItem(
                            bookmark = bookmark,
                            onClick = { onBookmarkClick(bookmark.url) },
                            onLongClick = { },
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }
            }
        }
    }
}