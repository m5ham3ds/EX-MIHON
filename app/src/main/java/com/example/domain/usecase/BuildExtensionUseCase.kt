package com.example.domain.usecase

import com.example.domain.model.BuildProgress
import com.example.domain.model.ExtensionConfig
import com.example.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BuildExtensionUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(config: ExtensionConfig): Flow<BuildProgress> {
        return repository.buildExtension(config)
    }
}
