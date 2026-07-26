package com.example.ui.permissions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.AnalyzeWebsiteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModel @Inject constructor(
    private val analyzeWebsiteUseCase: AnalyzeWebsiteUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow<AnalysisUiState>(AnalysisUiState.Idle)
    val uiState: StateFlow<AnalysisUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<PermissionsUiEvent>(
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<PermissionsUiEvent> = _events.asSharedFlow()

    private val targetUrl: String
        get() = savedStateHandle.get<String>(KEY_URL) ?: ""

    fun initUrl(url: String) {
        if (targetUrl.isEmpty() && url.isNotEmpty()) {
            savedStateHandle[KEY_URL] = url
            analyzeWebsite(url)
        }
    }

    fun analyzeWebsite(url: String) {
        savedStateHandle[KEY_URL] = url
        viewModelScope.launch {
            analyzeWebsiteUseCase(url)
                .onStart { _uiState.value = AnalysisUiState.Loading }
                .catch { e -> _uiState.value = AnalysisUiState.Error(e.message ?: "خطأ غير متوقع", e) }
                .collect { result ->
                    _uiState.value = when (result) {
                        is com.example.core.utils.Resource.Success -> AnalysisUiState.Success(result.data)
                        is com.example.core.utils.Resource.Error -> AnalysisUiState.Error(result.message, result.exception)
                    }
                }
        }
    }

    fun onFeatureToggled(featureId: String, isEnabled: Boolean) {
        val currentState = _uiState.value
        if (currentState is AnalysisUiState.Success) {
            val updatedFeatures = currentState.result.compatibleFeatures.map {
                if (it.id == featureId) it.copy(isEnabled = isEnabled) else it
            }
            _uiState.value = currentState.copy(
                result = currentState.result.copy(compatibleFeatures = updatedFeatures)
            )
        }
    }

    companion object {
        private const val KEY_URL = "target_url"
    }
}
