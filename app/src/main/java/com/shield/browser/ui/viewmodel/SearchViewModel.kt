package com.shield.browser.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shield.browser.data.model.SearchSuggestion
import com.shield.browser.data.network.SearchSuggestionProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _suggestions = MutableStateFlow<List<SearchSuggestion>>(emptyList())
    val suggestions: StateFlow<List<SearchSuggestion>> = _suggestions.asStateFlow()

    private val suggestionProvider = SearchSuggestionProvider()

    fun onQueryChanged(query: String) {
        if (query.isBlank()) {
            _suggestions.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                val webSuggestions = suggestionProvider.getSuggestions(query, "google")
                val uiSuggestions = webSuggestions.map { 
                    SearchSuggestion(query = it.query, displayText = it.query) 
                }
                _suggestions.value = uiSuggestions.take(5)
            } catch (e: Exception) {
                _suggestions.value = emptyList()
            }
        }
    }
    
    fun clearSuggestions() {
        _suggestions.value = emptyList()
    }
}