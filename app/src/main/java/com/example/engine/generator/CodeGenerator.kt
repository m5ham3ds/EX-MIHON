package com.example.engine.generator

import com.example.core.utils.FileUtils
import com.example.di.IoDispatcher
import com.example.domain.model.ExtensionConfig
import com.example.engine.TemplateEngine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CodeGenerator @Inject constructor(
    private val templateEngine: TemplateEngine,
    private val fileUtils: FileUtils,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun generateFromTemplate(
        config: ExtensionConfig,
        onProgress: (String) -> Unit
    ): Result<List<File>> = withContext(ioDispatcher) {
        runCatching {
            // Simplified logic for creating the final files
            val generatedFiles = mutableListOf<File>()
            val baseDir = File(config.outputPath, "source")
            val srcDir = File(baseDir, "src/main/kotlin")
            val packageDir = File(srcDir, config.packageName.replace('.', '/'))
            
            val mainFile = File(packageDir, "${config.siteName}.kt")
            fileUtils.writeTextFile(mainFile.absolutePath, "package ${config.packageName}\n\nclass ${config.siteName} {}")
            generatedFiles.add(mainFile)
            
            generatedFiles
        }
    }
}
