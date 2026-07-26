package com.example.domain.model

import java.io.Serializable

data class ExtensionTemplate(
    val type: TemplateType,
    val displayName: String,
    val version: String,
    val supportedFeatures: List<FeatureItem>,
    val defaultSelectors: Map<String, String>,
    val assetPath: String
) : Serializable
