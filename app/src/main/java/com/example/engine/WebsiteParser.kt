package com.example.engine

import com.example.domain.model.SiteMetadata
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebsiteParser @Inject constructor() {

    data class ParseResult(
        val metadata: SiteMetadata,
        val suggestedSelectors: Map<String, String>
    )

    fun parse(url: String, html: String): ParseResult {
        val doc = Jsoup.parse(html, url)
        return ParseResult(
            metadata = extractMetadata(doc, url),
            suggestedSelectors = suggestSelectors(doc)
        )
    }

    private fun extractMetadata(doc: Document, url: String): SiteMetadata {
        val isWordPress = doc.select("meta[name=generator]").attr("content").contains("WordPress", ignoreCase = true)
        return SiteMetadata(
            siteName = doc.title().split("-").firstOrNull()?.trim() ?: "Unknown",
            siteTitle = doc.title(),
            detectedLanguage = doc.attr("lang").take(2).ifBlank { "en" },
            isWordPress = isWordPress,
            wordPressVersion = null,
            hasApiEndpoint = false,
            apiEndpointUrl = null
        )
    }

    private fun suggestSelectors(doc: Document): Map<String, String> {
        return emptyMap()
    }
}
