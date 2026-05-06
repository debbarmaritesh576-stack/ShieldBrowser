package com.shield.browser.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shield.browser.data.model.HistoryEntry
import com.shield.browser.data.repository.HistoryRepository
import com.shield.browser.ui.components.HistoryItem
import com.shield.browser.ui.components.EmptyState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    historyRepository: HistoryRepository,
    onHistoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var history by remember { mutableStateOf<List<HistoryEntry>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    
    LaunchedEffect(searchQuery) {
        historyRepository.getAllHistory().collect { allHistory ->
            history = if (searchQuery.isEmpty()) {
                allHistory
            } else {
                allHistory.filter { 
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
            label = { Text("Search History") },
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

        if (history.isEmpty()) {
            EmptyState(message = "No history found.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                items(history.size) { index ->
                    val item = history[index]
                    
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
                                historyRepository.deleteHistory(item)
                            }
                        }
                    ) {
                        HistoryItem(
                            history = item,
                            onClick = { onHistoryClick(item.url) },
                            onDelete = {
                                scope.launch {
                                    historyRepository.deleteHistory(item)
                                }
                            },
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }
            }
        }
    }
}