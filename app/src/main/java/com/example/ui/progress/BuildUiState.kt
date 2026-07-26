package com.example.ui.progress

import com.example.domain.model.BuildStep

sealed class BuildUiState {
    data object Idle : BuildUiState()
    data class InProgress(
        val currentStep: BuildStep,
        val progress: Int,
        val stepMessage: String,
        val logs: List<String>
    ) : BuildUiState()
    data class Success(
        val apkPath: String,
        val packageName: String,
        val buildDurationMs: Long
    ) : BuildUiState()
    data class Failed(
        val failedStep: BuildStep,
        val errorMessage: String,
        val logContent: String
    ) : BuildUiState()
}
