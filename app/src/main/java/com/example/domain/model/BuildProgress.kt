package com.example.domain.model

data class BuildProgress(
    val step: BuildStep,
    val progress: Int,
    val stepProgress: Int,
    val message: String,
    val logLine: String? = null
)
