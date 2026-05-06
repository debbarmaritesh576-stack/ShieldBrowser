package com.shield.browser.data.source

interface AdBlockSource {
    suspend fun loadRules(): Result<Set<String>>
}