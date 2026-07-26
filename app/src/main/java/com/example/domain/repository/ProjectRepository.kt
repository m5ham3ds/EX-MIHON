package com.example.domain.repository

import com.example.domain.model.AnalysisResult
import com.example.domain.model.BuildProgress
import com.example.domain.model.ExtensionConfig
import com.example.domain.model.ExtensionTemplate
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun analyzeWebsite(url: String): Flow<Result<AnalysisResult>>
    fun getAvailableTemplates(): Flow<List<ExtensionTemplate>>
    suspend fun saveExtensionConfig(config: ExtensionConfig): Result<Unit>
    fun buildExtension(config: ExtensionConfig): Flow<BuildProgress>
}
