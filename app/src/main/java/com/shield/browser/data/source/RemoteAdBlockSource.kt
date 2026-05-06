package com.shield.browser.data.source

import com.shield.browser.utils.HostsFileParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RemoteAdBlockSource(private val urlString: String) : AdBlockSource {
    private val parser = HostsFileParser()

    override suspend fun loadRules(): Result<Set<String>> {
        return withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            try {
                val url = URL(urlString)
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                
                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    return@withContext Result.failure(Exception("HTTP Error: ${connection.responseCode}"))
                }

                val reader = InputStreamReader(connection.inputStream)
                val entities = parser.parse(reader)
                val domains = entities.map { it.domain }.toSet()
                Result.success(domains)
            } catch (e: Exception) {
                Result.failure(e)
            } finally {
                connection?.disconnect()
            }
        }
    }
}