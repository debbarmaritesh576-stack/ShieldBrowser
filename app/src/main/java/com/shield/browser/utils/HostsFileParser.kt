package com.shield.browser.utils

import com.shield.browser.data.local.entity.AdBlockRuleEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

object HostsFileParser {

    private const val COMMENT_CHAR = '#'
    private const val LOCALHOST = "localhost"
    private val LOCAL_IPS = setOf("127.0.0.1", "0.0.0.0", "::1")

    suspend fun parse(input: InputStreamReader): List<AdBlockRuleEntity> {
        return withContext(Dispatchers.IO) {
            val domains = mutableSetOf<String>()
            
            input.useLines { lines ->
                for (line in lines) {
                    val trimmed = line.trim()
                    if (trimmed.isEmpty() || trimmed.startsWith(COMMENT_CHAR)) continue

                    val commentIndex = trimmed.indexOf(COMMENT_CHAR)
                    val cleanLine = if (commentIndex > 0) trimmed.substring(0, commentIndex).trim() else trimmed

                    val parts = cleanLine.split("\\s+".toRegex())
                    
                    if (parts.size >= 2) {
                        val ip = parts[0]
                        val domain = parts[1].lowercase()
                        if (!LOCAL_IPS.contains(ip) && domain != LOCALHOST && domain.contains(".")) {
                            domains.add(domain)
                        }
                    } else if (parts.size == 1) {
                         val domain = parts[0].lowercase()
                         if (domain != LOCALHOST && domain.contains(".")) {
                             domains.add(domain)
                         }
                    }
                }
            }
            domains.map { AdBlockRuleEntity(domain = it) }
        }
    }
}