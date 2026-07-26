package com.example.ui.permissions

import com.example.domain.model.ExtensionConfig

sealed class PermissionsUiEvent {
    data class NavigateToProgress(val config: ExtensionConfig) : PermissionsUiEvent()
    data class ShowValidationError(val message: String) : PermissionsUiEvent()
}
