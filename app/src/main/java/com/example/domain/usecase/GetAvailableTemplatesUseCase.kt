package com.example.domain.usecase

import com.example.domain.model.ExtensionTemplate
import com.example.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAvailableTemplatesUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(): Flow<List<ExtensionTemplate>> {
        return repository.getAvailableTemplates()
    }
}
