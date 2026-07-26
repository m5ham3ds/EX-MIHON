package com.example.ui.progress

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.example.domain.model.ExtensionConfig

@HiltViewModel
class ProgressViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<BuildUiState>(BuildUiState.Idle)
    val uiState: StateFlow<BuildUiState> = _uiState.asStateFlow()
    
    fun startBuild(config: ExtensionConfig) {
        // Here we would enqueue the WorkManager task.
        // For now, we simulate success for compilation purposes.
        _uiState.value = BuildUiState.Success(
            apkPath = "/storage/emulated/0/ADDITIONSTOTACHIYOMI/GeneratedExtensions/test.apk",
            packageName = config.packageName,
            buildDurationMs = 5000L
        )
    }
}
