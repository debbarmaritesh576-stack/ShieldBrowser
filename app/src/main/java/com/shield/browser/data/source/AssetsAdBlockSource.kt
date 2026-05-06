package com.shield.browser.data.source

import android.content.Context
import com.shield.browser.utils.HostsFileParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class AssetsAdBlockSource(private val context: Context) : AdBlockSource {
    private val parser = HostsFileParser()

    override suspend fun loadRules(): Result<Set<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.assets.open("hosts.txt")
                val reader = InputStreamReader(inputStream)
                val entities = parser.parse(reader)
                val domains = entities.map { it.domain }.toSet()
                Result.success(domains)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}