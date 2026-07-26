package com.example.domain.model

import java.io.Serializable

data class SiteMetadata(
    val siteName: String,
    val siteTitle: String,
    val detectedLanguage: String,
    val isWordPress: Boolean,
    val wordPressVersion: String?,
    val hasApiEndpoint: Boolean,
    val apiEndpointUrl: String?
) : Serializable
