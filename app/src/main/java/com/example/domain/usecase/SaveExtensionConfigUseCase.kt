package com.example.domain.usecase

import com.example.domain.model.ExtensionConfig
import com.example.domain.repository.ProjectRepository
import javax.inject.Inject

class SaveExtensionConfigUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(config: ExtensionConfig): Result<Unit> {
        return repository.saveExtensionConfig(config)
    }
}
