#!/bin/bash
sed -i 's/result.fold(/when (result) {/g' app/src/main/java/com/example/ui/permissions/PermissionsViewModel.kt
sed -i 's/onSuccess = { AnalysisUiState.Success(it) },/is com.example.core.utils.Resource.Success -> AnalysisUiState.Success(result.data)/g' app/src/main/java/com/example/ui/permissions/PermissionsViewModel.kt
sed -i 's/onFailure = { AnalysisUiState.Error(it.message ?: "فشل التحليل", it) }/is com.example.core.utils.Resource.Error -> AnalysisUiState.Error(result.message ?: "فشل التحليل", result.exception)/g' app/src/main/java/com/example/ui/permissions/PermissionsViewModel.kt
sed -i 's/                    )//g' app/src/main/java/com/example/ui/permissions/PermissionsViewModel.kt
