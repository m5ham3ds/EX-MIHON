package com.example.data.repository

import com.example.domain.model.AnalysisResult
import com.example.domain.model.BuildProgress
import com.example.domain.model.ExtensionConfig
import com.example.domain.model.ExtensionTemplate
import com.example.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import com.example.engine.CompatibilityEngine
import com.example.engine.TemplateEngine
import kotlinx.coroutines.CoroutineDispatcher
import com.example.di.IoDispatcher
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request

@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val compatibilityEngine: CompatibilityEngine,
    private val templateEngine: TemplateEngine,
    private val okHttpClient: OkHttpClient,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ProjectRepository {

    override fun analyzeWebsite(url: String): Flow<Result<AnalysisResult>> = flow {
        try {
            val request = Request.Builder().url(url).build()
            val response = okHttpClient.newCall(request).execute()
            val html = response.body?.string() ?: ""
            if (!response.isSuccessful || html.isEmpty()) {
                emit(Result.failure(Exception("Failed to fetch HTML")))
                return@flow
            }
            
            val analysisResult = compatibilityEngine.analyze(url, html)
            val template = templateEngine.getBestTemplate(analysisResult.detectedTemplateType)
            emit(Result.success(analysisResult.copy(chosenTemplate = template)))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(ioDispatcher)

    override fun getAvailableTemplates(): Flow<List<ExtensionTemplate>> = flow {
        // Not fully implemented in this skeleton yet
        emit(emptyList())
    }

    override suspend fun saveExtensionConfig(config: ExtensionConfig): Result<Unit> {
        return Result.success(Unit)
    }

    override fun buildExtension(config: ExtensionConfig): Flow<BuildProgress> = flow {
        // Handled by Worker in actual implementation
    }
}
