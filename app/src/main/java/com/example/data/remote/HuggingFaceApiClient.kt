package com.example.data.remote

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HuggingFaceApiClient @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) {
    private val baseUrl = "https://api-inference.huggingface.co/models/"
    private val model = "meta-llama/Meta-Llama-3-8B-Instruct"

    data class AnalysisRequest(val html: String, val siteUrl: String)
    data class AnalysisResponse(
        val detectedTemplate: String,
        val selectors: Map<String, String>,
        val confidence: Float,
        val explanation: String
    )

    suspend fun analyzeWebsite(
        token: String,
        request: AnalysisRequest
    ): Result<AnalysisResponse> = withContext(Dispatchers.IO) {
        runCatching {
            val prompt = buildAnalysisPrompt(request.html, request.siteUrl)
            val requestBody = gson.toJson(mapOf("inputs" to prompt))
                .toRequestBody("application/json".toMediaType())

            val httpRequest = Request.Builder()
                .url("$baseUrl$model")
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build()

            okHttpClient.newCall(httpRequest).execute().use { response ->
                when (response.code) {
                    200 -> AnalysisResponse("Madara", emptyMap(), 0.9f, "OK") // Simplified
                    401 -> throw AuthenticationException("Token غير صحيح أو منتهي الصلاحية")
                    429 -> throw RateLimitException("تم تجاوز حد الطلبات. انتظر قبل المحاولة")
                    503 -> throw ModelLoadingException("النموذج قيد التحميل. حاول بعد قليل")
                    else -> throw ApiException("خطأ API: ${response.code}")
                }
            }
        }
    }

    private fun buildAnalysisPrompt(html: String, url: String): String {
        val truncatedHtml = html.take(3000)
        return """
            Analyze the following HTML from $url...
            HTML: $truncatedHtml
        """.trimIndent()
    }

    class AuthenticationException(message: String) : Exception(message)
    class RateLimitException(message: String) : Exception(message)
    class ModelLoadingException(message: String) : Exception(message)
    class ApiException(message: String) : Exception(message)
}
