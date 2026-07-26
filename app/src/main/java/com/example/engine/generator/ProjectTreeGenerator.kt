package com.example.engine.generator

import com.example.domain.model.ExtensionConfig
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectTreeGenerator @Inject constructor() {
    fun generate(config: ExtensionConfig) {
        val baseDir = File(config.outputPath, "source")
        baseDir.mkdirs()
        
        val srcDir = File(baseDir, "src/main/kotlin")
        val packageDir = File(srcDir, config.packageName.replace('.', '/'))
        packageDir.mkdirs()
    }
}
