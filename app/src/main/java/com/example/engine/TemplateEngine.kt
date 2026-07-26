package com.example.engine

import android.content.Context
import com.example.domain.model.ExtensionTemplate
import com.example.domain.model.TemplateType
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplateEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    fun getBestTemplate(type: TemplateType): ExtensionTemplate {
        return ExtensionTemplate(
            type = type,
            displayName = type.displayName,
            version = "1.0",
            supportedFeatures = emptyList(),
            defaultSelectors = emptyMap(),
            assetPath = type.assetPath
        )
    }
}
