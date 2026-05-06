package com.shield.browser.data.network

import com.shield.browser.data.model.SearchSuggestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URLEncoder

class SearchSuggestionProvider {

    suspend fun getSuggestions(query: String, engineId: String = "google"): List<SearchSuggestion> {
        if (query.isBlank()) return emptyList()
        
        return withContext(Dispatchers.IO) {
            try {
                when (engineId) {
                    "duckduckgo" -> fetchDDG(query)
                    else -> fetchGoogle(query)
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    private fun fetchGoogle(query: String): List<SearchSuggestion> {
        val encoded = URLEncoder.encode(query, "UTF-8")
        val url = "https://suggestqueries.google.com/complete/search?client=firefox&q=$encoded"
        val json = java.net.URL(url).readText()
        val array = JSONArray(json).getJSONArray(1)
        return (0 until array.length()).map { SearchSuggestion(array.getString(it)) }
    }

    private fun fetchDDG(query: String): List<SearchSuggestion> {
        val encoded = URLEncoder.encode(query, "UTF-8")
        val url = "https://duckduckgo.com/ac/?q=$encoded&type=list"
        val json = java.net.URL(url).readText()
        val array = JSONArray(json)
        return (0 until array.length()).map { SearchSuggestion(array.getString(it)) }
    }
}