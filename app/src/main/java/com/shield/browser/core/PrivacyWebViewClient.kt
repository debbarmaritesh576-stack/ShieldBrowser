 package com.shield.browser.core

import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.shield.browser.data.model.HistoryEntry
import com.shield.browser.data.repository.HistoryRepository
import com.shield.browser.utils.AdBlockEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrivacyWebViewClient(
    private val onUrlChanged: (String) -> Unit,
    private val onPageStarted: () -> Unit,
    private val onPageFinished: () -> Unit,
    private val onError: (String) -> Unit,
    private val historyRepository: HistoryRepository
) : WebViewClient() {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url?.toString() ?: return false
        onUrlChanged(url)
        return false
    }

    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        val url = request?.url?.toString() ?: return null
        if (AdBlockEngine.isBlocked(url)) {
            return WebResourceResponse("text/plain", "UTF-8", null)
        }
        return super.shouldInterceptRequest(view, request)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        url?.let { 
            onUrlChanged(it) 
            scope.launch {
                try {
                    val entry = HistoryEntry(title = view?.title ?: "Untitled", url = it)
                    historyRepository.addHistory(entry)
                } catch (e: Exception) { e.printStackTrace() }
            }
        }
        onPageStarted()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        onPageFinished()
    }

    override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
        super.onReceivedError(view, errorCode, description, failingUrl)
        onError(description ?: "Unknown Error")
    }
}