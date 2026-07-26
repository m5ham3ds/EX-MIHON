package com.example.domain.model

import java.io.Serializable

data class FeatureItem(
    val id: String,
    val displayName: String,
    val description: String,
    val isSupported: Boolean,
    var isEnabled: Boolean,
    val codeBlockTag: String
) : Serializable
