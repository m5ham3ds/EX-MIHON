package com.example.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

    companion object {
        val KEY_HF_TOKEN = stringPreferencesKey("hugging_face_token")
        val KEY_THEME = stringPreferencesKey("app_theme")
        val KEY_LANGUAGE = stringPreferencesKey("app_language")
        val KEY_OUTPUT_PATH = stringPreferencesKey("output_path")
        val KEY_NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications")
    }

    fun getHuggingFaceTokenFlow(): Flow<String> = context.dataStore.data
        .map { it[KEY_HF_TOKEN] ?: "" }

    suspend fun getHuggingFaceToken(): String = getHuggingFaceTokenFlow().first()

    suspend fun saveHuggingFaceToken(token: String) {
        context.dataStore.edit { it[KEY_HF_TOKEN] = token }
    }

    fun getThemeFlow(): Flow<String> = context.dataStore.data
        .map { it[KEY_THEME] ?: "SYSTEM" }

    suspend fun saveTheme(theme: String) {
        context.dataStore.edit { it[KEY_THEME] = theme }
    }

    fun getOutputPathFlow(): Flow<String> = context.dataStore.data
        .map {
            it[KEY_OUTPUT_PATH] ?: "/storage/emulated/0/ADDITIONSTOTACHIYOMI/GeneratedExtensions"
        }
}
