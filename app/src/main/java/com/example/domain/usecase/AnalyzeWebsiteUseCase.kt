package com.example.domain.usecase
import com.example.core.utils.Resource

import com.example.domain.model.AnalysisResult
import com.example.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnalyzeWebsiteUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(url: String): Flow<Resource<AnalysisResult>> {
        return repository.analyzeWebsite(url)
    }
}
