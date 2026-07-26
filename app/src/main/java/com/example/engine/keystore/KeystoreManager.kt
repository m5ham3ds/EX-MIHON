package com.example.engine.keystore

import android.content.Context
import com.example.core.utils.FileUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeystoreManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fileUtils: FileUtils
) {
    private val defaultKeystore = File(context.filesDir, "debug.keystore")

    fun ensureKeystoreExists(): File {
        if (!defaultKeystore.exists()) {
            generateDebugKeystore()
        }
        return defaultKeystore
    }

    private fun generateDebugKeystore() {
        // Normally this would execute keytool
        defaultKeystore.writeText("dummy_keystore_content")
    }

    fun getSigningConfig(keystoreFile: File): String = """
        signingConfigs {
            create("release") {
                storeFile = file("${keystoreFile.absolutePath}")
                storePassword = "android"
                keyAlias = "androiddebugkey"
                keyPassword = "android"
            }
        }
        buildTypes {
            release {
                signingConfig = signingConfigs.getByName("release")
                isMinifyEnabled = false
            }
        }
    """.trimIndent()
}
