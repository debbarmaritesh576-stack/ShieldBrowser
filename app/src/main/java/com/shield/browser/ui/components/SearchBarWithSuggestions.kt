package com.shield.browser.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.shield.browser.data.model.SearchSuggestion
import com.shield.browser.ui.viewmodel.SearchViewModel

@Composable
fun SearchBarWithSuggestions(
    viewModel: SearchViewModel,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    val suggestions by viewModel.suggestions.collectAsState()

    Column(modifier = modifier) {
        OutlinedTextField(
            value = query,
            onValueChange = { 
                query = it
                viewModel.onQueryChanged(it)
            },
            label = { Text("Search or enter URL") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { 
                        query = ""
                        viewModel.clearSuggestions()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            }
        )

        if (suggestions.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column {
                    suggestions.forEach { suggestion ->
                        SuggestionItem(
                            suggestion = suggestion,
                            onClick = {
                                query = suggestion.query
                                viewModel.clearSuggestions()
                                onSearch(suggestion.query)
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}