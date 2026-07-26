package com.example.domain.model

import java.io.Serializable

data class ExtensionConfig(
    val targetUrl: String,
    val siteName: String,
    val siteNameSlug: String,
    val language: String,
    val packageName: String,
    val versionCode: Int,
    val versionName: String,
    val chosenTemplate: ExtensionTemplate,
    val enabledFeatures: Set<String>,
    val customSelectors: Map<String, String>,
    val detectedSelectors: Map<String, String>,
    val preferences: Map<String, Any>,
    val outputPath: String
) : Serializable {
    val finalSelectors: Map<String, String>
        get() = detectedSelectors + customSelectors
}
