package com.example.domain.model

import java.io.Serializable

enum class AnalysisMethod { AI_HUGGINGFACE, LOCAL_JSOUP, HYBRID }

data class AnalysisResult(
    val url: String,
    val detectedTemplateType: TemplateType,
    val chosenTemplate: ExtensionTemplate,
    val detectedSelectors: Map<String, String>,
    val compatibleFeatures: List<FeatureItem>,
    val siteMetadata: SiteMetadata,
    val confidenceScore: Float,
    val analysisMethod: AnalysisMethod
) : Serializable
