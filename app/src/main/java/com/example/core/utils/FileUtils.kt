package com.example.core.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileUtils @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun writeTextFile(path: String, content: String): File {
        val file = File(path)
        file.parentFile?.mkdirs()
        file.writeText(content)
        return file
    }

    fun appendToErrorLog(errorReport: String) {
        val file = File(context.getExternalFilesDir(null), "Error.log")
        file.parentFile?.mkdirs()
        file.appendText(errorReport)
    }
}
