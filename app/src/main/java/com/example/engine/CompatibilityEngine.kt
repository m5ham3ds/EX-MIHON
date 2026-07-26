package com.example.engine

import com.example.domain.model.AnalysisResult
import com.example.domain.model.AnalysisMethod
import com.example.domain.model.TemplateType
import com.example.domain.model.ExtensionTemplate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompatibilityEngine @Inject constructor(
    private val websiteParser: WebsiteParser,
    private val templateEngine: TemplateEngine
) {
    suspend fun analyze(url: String, html: String): AnalysisResult {
        return analyzeWithJsoup(url, html)
    }

    private fun analyzeWithJsoup(url: String, html: String): AnalysisResult {
        val parsedData = websiteParser.parse(url, html)
        val templateType = detectTemplateType(html)
        val template = templateEngine.getBestTemplate(templateType)
        val detectedSelectors = verifySelectorsAgainstHtml(template.defaultSelectors, html)
        
        return AnalysisResult(
            url = url,
            detectedTemplateType = templateType,
            chosenTemplate = template,
            detectedSelectors = detectedSelectors,
            compatibleFeatures = template.supportedFeatures,
            siteMetadata = parsedData.metadata,
            confidenceScore = 0.8f,
            analysisMethod = AnalysisMethod.LOCAL_JSOUP
        )
    }

    private fun detectTemplateType(html: String): TemplateType {
        return TemplateType.values()
            .maxByOrNull { template ->
                template.detectionSignatures.count { sig ->
                    html.contains(sig, ignoreCase = true)
                }
            } ?: TemplateType.WORDPRESS_GENERIC
    }

    private fun verifySelectorsAgainstHtml(selectors: Map<String, String>, html: String): Map<String, String> {
        return selectors
    }
}
