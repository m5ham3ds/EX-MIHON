package com.example.ui.permissions

import com.example.domain.model.AnalysisResult

sealed class AnalysisUiState {
    data object Idle : AnalysisUiState()
    data object Loading : AnalysisUiState()
    data class Success(val result: AnalysisResult) : AnalysisUiState()
    data class Error(
        val message: String,
        val cause: Throwable? = null,
        val isRetryable: Boolean = true
    ) : AnalysisUiState()
}
