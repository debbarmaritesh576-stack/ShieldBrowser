package com.shield.browser.data.source

import com.shield.browser.utils.HostsFileParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStreamReader

class FileAdBlockSource(private val file: File) : AdBlockSource {
    private val parser = HostsFileParser()

    override suspend fun loadRules(): Result<Set<String>> {
        return withContext(Dispatchers.IO) {
            try {
                if (!file.exists()) return@withContext Result.failure(FileNotFoundException("File not found"))
                val reader = InputStreamReader(file.inputStream())
                val entities = parser.parse(reader)
                val domains = entities.map { it.domain }.toSet()
                Result.success(domains)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}